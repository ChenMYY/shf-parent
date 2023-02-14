package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-03  10:41
 */
public interface AdminService extends BaseService<Admin> {

    /**
     * 根据username查找
     * @param username
     * @return
     */
    Admin getByUsername(String username);
}
