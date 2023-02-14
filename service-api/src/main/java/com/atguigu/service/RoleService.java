package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-03  10:41
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 根据adminId查询管理员未分配和已分配的角色列表
     * @param adminId
     * @return
     */
    Map<String, List<Role>> findRoleListMap(Long adminId);

    /**
     * 给管理员分配角色
     * @param adminId
     * @param roleIds
     */
    void assignRole(Long adminId, List<Long> roleIds);
}
