package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.UserInfo;
import com.atguigu.mapper.UserInfoMapper;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-10  14:35
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public UserInfo findByPhone(String phone) {
        return userInfoMapper.findByPhone(phone);
    }

    @Override
    public UserInfo findByNickName(String nickName) {
        return userInfoMapper.findByNickName(nickName);
    }

    @Override
    public void insert(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
