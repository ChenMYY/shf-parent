package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-11  09:23
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;
    @GetMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        UserFollow userFollow = new UserFollow();
        //设置houseId
        userFollow.setHouseId(houseId);
        //设置userId
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        userFollow.setUserId(userInfo.getId());

        userFollowService.addFollow(userFollow);

        return Result.ok();
    }

    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findPageList(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session){
        //分页查询当前用户的关注记录
        //1. 获取当前用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        //2. 调用业务层的方法查询当前用户的关注的分页信息
        PageInfo<UserFollowVo> page = userFollowService.findListPage(pageNum,pageSize,userInfo.getId());

        return Result.ok(page);
    }

    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        //调用业务层的方法取消关注记录(根据userId和id一起去取消)
        UserFollow userFollow = new UserFollow();
        userFollow.setId(id);
        userFollow.setUserId(userInfo.getId());
        userFollowService.cancelFollow(userFollow);

        return Result.ok();
    }
}
