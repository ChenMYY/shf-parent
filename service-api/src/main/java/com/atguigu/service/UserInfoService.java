package com.atguigu.service;

import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-10  14:26
 */
public interface UserInfoService {
    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    UserInfo findByPhone(String phone);

    /**
     * 根据昵称查找用户
     * @param nickName
     * @return
     */
    UserInfo findByNickName(String nickName);

    /**
     * 保存用户信息
     * @param userInfo
     */
    void insert(UserInfo userInfo);
}
