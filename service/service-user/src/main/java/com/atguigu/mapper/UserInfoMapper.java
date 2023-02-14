package com.atguigu.mapper;

import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-10  14:35
 */
public interface UserInfoMapper {
    UserInfo findByPhone(String phone);

    UserInfo findByNickName(String nickName);

    void insert(UserInfo userInfo);
}
