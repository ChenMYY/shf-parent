package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-11  14:39
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public List<Map<String, Object>> findZNodes(Long roleId) {
        //1. 查询所有权限
        List<Permission> allPermissionList = permissionMapper.findAll();
        //2. 查询当前角色已分配的权限id的集合
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        //3. 将allPermissionList 转成 List<Map<String, Object>>
        List<Map<String, Object>> zNodes = allPermissionList.stream()
                .map(permission -> {
                    //1. 创建Map
                    Map<String, Object> map = new HashMap<>();
                    //2. 设置Map的五个属性
                    map.put("id", permission.getId());
                    map.put("name", permission.getName());
                    map.put("pId", permission.getParentId());
                    map.put("checked", assignPermissionIdList.contains(permission.getId()));
                    if (permission.getType() == 1) {
                        //给一级菜单、二级菜单设置open属性为true
                        map.put("open", true);
                    }
                    return map;
                })
                .collect(Collectors.toList());
        return zNodes;
    }

    @Override
    public void assignPermission(Long roleId, List<Long> permissionIds) {
        //1. 删除roleId对应的角色的所有分配信息
        rolePermissionMapper.deleteByRoleId(roleId);
        //2. 批量新增
        if (!CollectionUtils.isEmpty(permissionIds)) {
            rolePermissionMapper.insertBatch(roleId,permissionIds);
        }
    }

    @Override
    public List<Permission> findMenu(Long adminId) {
        List<Permission> permissionList = null;
        //1.判断当前用户是否是超级管理员
        if (adminId == 1) {
            //如果是超级管理员那么他必须拥有所有权限
            permissionList = permissionMapper.findAll();
        }else {
            //如果不是超级管理员，查询adminId对应的管理员有哪些权限
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }

        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<Permission> findPermissionListByAdminId(Long adminId) {
        //判断当前用户是否是超级管理员
        if (adminId == 1) {
            return permissionMapper.findAll();
        }
        return permissionMapper.findPermissionListByAdminId(adminId);
    }
}
