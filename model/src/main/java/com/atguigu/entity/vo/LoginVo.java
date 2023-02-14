package com.atguigu.entity.vo;


import lombok.Data;

/**
 * 登录对象
 */
@Data
public class LoginVo {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
}
