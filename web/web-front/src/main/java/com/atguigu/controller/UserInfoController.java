package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.LoginVo;
import com.atguigu.entity.vo.RegisterVo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-10  11:15
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private UserInfoService userInfoService;
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone, HttpSession session){
        //先判断你到底是不是我们的用户
        Jedis jedis = jedisPool.getResource();
        //1. 验证码安全保护:例如同一个手机号一分钟只能不能重复发送
        if (jedis.set("lock:"+phone,"a","nx","ex",60) == null) {
            return Result.fail("一分钟之内不允许重复发送验证码");
        }
        //1. 24小时之内，同一个手机号最多只能发送5次
        if (jedis.llen("code:count:"+phone) >= 5) {
            //表示这个手机号接下来想发送第六次验证码
            //判断,当前时间戳和list中的第一个时间戳对比，是否超过了一天
            long firstSendTime = Long.parseLong(jedis.lindex("code:count:" + phone, 0));
            //当前时间戳
            long currentTime = System.currentTimeMillis();
            if (currentTime - firstSendTime <= 24*60*60*1000) {
                //说明一天内已经发送了五次
                return Result.fail("一天内最多只能发送五次验证码");
            }else {
                //说明这次发送验证码，距离list中的第一次已经超过一天了
                //将list中的第一个拿出来
                jedis.lpop("code:count:"+phone);
                //将现在的时间放到list最后
                jedis.rpush("code:count:"+phone,String.valueOf(currentTime));
            }
        }else {
            //说明现在还没有发五个验证码
            jedis.rpush("code:count:"+phone,String.valueOf(System.currentTimeMillis()));
        }
        //3. 生成一个随机的验证码
        String code = "1111";
        //4. 使用云短信API发送验证码给phone
        //5. 保存验证码(供后续校验),指定过期时间
        jedis.setex("code:register:"+phone, SHFConstant.RedisConstant.CODE_REGISTER_EX,code);

        jedis.close();
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo){
        //1. 校验验证码
        //1.1 用户输入的验证码
        String registerCode = registerVo.getCode();
        Jedis jedis = jedisPool.getResource();
        String redisCode = jedis.get("code:register:" + registerVo.getPhone());
        //判断:你使用这个验证码校验了几次
        if (!registerCode.equalsIgnoreCase(redisCode)) {
            //删除验证码
            jedis.del("code:register:" + registerVo.getPhone());
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //2. 校验用户手机号是否已被注册（不能重复注册）:根据phone到数据库查询
        UserInfo dbPhoneUserInfo = userInfoService.findByPhone(registerVo.getPhone());
        if (dbPhoneUserInfo != null) {
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        //3. 校验昵称是否被占用:根据昵称到数据库查询
        UserInfo dbNickNameUserInfo = userInfoService.findByNickName(registerVo.getNickName());
        if (dbNickNameUserInfo != null) {
            return Result.build(null,ResultCodeEnum.NICKNAME_REGISTER_ERROR);
        }
        //4. 保存用户信息:将信息保存到数据库
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerVo,userInfo);
        userInfo.setStatus(1);
        //对密码进行加密
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpSession session){
        //1. 校验phone是否正确
        UserInfo dbPhoneUserInfo = userInfoService.findByPhone(loginVo.getPhone());
        if (dbPhoneUserInfo == null) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        //2. 校验账号是否被锁定
        if (dbPhoneUserInfo.getStatus() == 0) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }
        //3. 校验password是否正确
        //3.1 数据库中的密码
        String dbPassword = dbPhoneUserInfo.getPassword();
        //3.2 用户传过来的密码
        String userInfoPassword = loginVo.getPassword();
        if (!MD5.encrypt(userInfoPassword).equals(dbPassword)) {
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //4. 登录成功，将用户的信息(nickName、phone)响应给客户端
        Map map = new HashMap();
        map.put("nickName",dbPhoneUserInfo.getNickName());
        map.put("phone",dbPhoneUserInfo.getPhone());
        //5. 服务器端保存登录状态
        session.setAttribute("USER",dbPhoneUserInfo);
        return Result.ok(map);
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session){
        //清空登录状态
        session.invalidate();
        return Result.ok();
    }
}
