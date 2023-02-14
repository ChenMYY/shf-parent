package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-03  10:42
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    public BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    @Override
    public Map<String, List<Role>> findRoleListMap(Long adminId) {
        //1. 查询出所有角色
        List<Role> allRoleList = roleMapper.findAll();
        //2. 查询出当前管理员已分配的角色的id集合
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //3. 判断assignRoleIdList是否为空
        //3.1 声明两个集合，用于存储未分配和已分配的角色
        List<Role> unAssignRoleList = new ArrayList<>();
        List<Role> assignRoleList = new ArrayList<>();
        if (CollectionUtils.isEmpty(assignRoleIdList)) {
            //如果assignRoleIdList为空,那么所有的角色都是未分配的
            unAssignRoleList = allRoleList;
        }else {
            //如果assignRoleIdList不为空，则说明有已分配的角色，那么遍历allRoleList判断哪个角色是已分配、哪个角色是未分配
            for (Role role : allRoleList) {
                //判断role的id是否在assignRoleIdList中
                if (assignRoleIdList.contains(role.getId())) {
                    //说明当前role是已分配的
                    assignRoleList.add(role);
                }else {
                    //说明当前role是未分配的
                    unAssignRoleList.add(role);
                }
            }
        }
        //3. 将它俩封装到Map中，并返回
        Map<String,List<Role>> map = new HashMap<>();
        map.put("unAssignRoleList",unAssignRoleList);
        map.put("assignRoleList",assignRoleList);

        return map;
    }

    @Override
    public void assignRole(Long adminId, List<Long> roleIds) {
        //1. 删除当前adminId对应的所有分配信息
        adminRoleMapper.deleteByAdminId(adminId);
        //2. 以adminId和roleIds作为数据，新增关联记录
        if (!CollectionUtils.isEmpty(roleIds)) {
            adminRoleMapper.insertBatch(adminId,roleIds);
        }
    }
}
