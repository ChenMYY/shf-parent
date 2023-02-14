package com.atguigu.helper;

import com.atguigu.entity.Permission;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.helper
 *
 * @author Leevi
 * 日期2023-02-11  16:07
 */
public class PermissionHelper {
    /**
     * 构建父子级菜单
     * @param permissionList
     * @return
     */
    public static List<Permission> build(List<Permission> permissionList) {
        //过滤掉二级和三级菜单
        if (!CollectionUtils.isEmpty(permissionList)) {
            List<Permission> menu = permissionList.stream()
                    .filter(permission -> permission.getParentId() == 0)
                    .collect(Collectors.toList());
            //给menu中的每一个Permission设置子菜单
            for (Permission permission : menu) {
                //每一个permission就是一个一级菜单
                permission.setLevel(1);
                //给每一个permission设置子菜单
                permission.setChildren(getChildren(permissionList, permission));
            }
            return menu;
        }else {
            return permissionList;
        }
    }

    /**
     * 获取子菜单列表
     * @param permissionList
     * @param permission
     * @return
     */
    private static List<Permission> getChildren(List<Permission> permissionList, Permission permission) {
        List<Permission> children = new ArrayList<>();
        for (Permission childPermission : permissionList) {
            if (childPermission.getParentId().equals(permission.getId())) {
                childPermission.setLevel(permission.getLevel() + 1);
                //给childPermission设置子菜单集合
                childPermission.setChildren(getChildren(permissionList,childPermission));
                children.add(childPermission);
            }
        }
        return children;
    }
}
