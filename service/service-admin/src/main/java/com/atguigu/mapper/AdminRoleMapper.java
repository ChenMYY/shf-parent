package com.atguigu.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-11  11:38
 */
public interface AdminRoleMapper {
    /**
     * 根据adminId查询管理员已分配的角色id集合
     * @param adminId
     * @return
     */
    List<Long> findRoleIdListByAdminId(Long adminId);

    /**
     * 根据adminId删除管理员与角色的关联信息
     * @param adminId
     */
    void deleteByAdminId(Long adminId);

    /**
     * 批量给管理员分配角色
     * @param adminId
     * @param roleIds
     */
    void insertBatch(@Param("adminId") Long adminId, @Param("roleIds") List<Long> roleIds);
}
