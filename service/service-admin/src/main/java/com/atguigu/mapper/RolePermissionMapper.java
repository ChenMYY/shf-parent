package com.atguigu.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-11  14:41
 */
public interface RolePermissionMapper {
    /**
     * 查询角色已分配的权限id的集合
     * @param roleId
     * @return
     */
    List<Long> findPermissionIdListByRoleId(Long roleId);

    /**
     * 根据roleId删除权限分配信息
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void insertBatch(@Param(("roleId")) Long roleId, @Param("permissionIds") List<Long> permissionIds);
}
