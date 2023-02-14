package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * @author Leevi
 * 日期2023-02-07  14:36
 */
public enum HouseImageType {
    RESOURCE(1,"房源图片"),PROPERTY(2,"房产图片");
    private Integer type;
    private String message;

    HouseImageType(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
