package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-05  08:46
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据username查询
     * @param username
     * @return
     */
    Admin getByUsername(String username);
}
