package com.atguigu.entity.vo;


import lombok.Data;

/**
 * 注册对象
 */
@Data
public class RegisterVo {

    // 昵称
    private String nickName;

    // 手机号
    private String phone;

    // 密码
    private String password;

    // 验证码
    private String code;
}
