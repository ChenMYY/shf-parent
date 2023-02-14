package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-11  14:39
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询管理员拥有的权限列表
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);
}
