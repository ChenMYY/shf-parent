package com.atguigu.service;

import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-11  14:35
 */
public interface PermissionService {
    /**
     * 查询roleId对应的角色的权限分配信息
     * @param roleId
     * @return
     */
    List<Map<String, Object>> findZNodes(Long roleId);

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void assignPermission(Long roleId, List<Long> permissionIds);

    /**
     * 查询用户的左侧菜单
     * @param adminId
     * @return
     */
    List<Permission> findMenu(Long adminId);

    /**
     * 根据adminId查询用户的权限列表
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);
}
