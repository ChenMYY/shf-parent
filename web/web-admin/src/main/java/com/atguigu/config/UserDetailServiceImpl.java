package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2023-02-13  09:24
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //校验证号:根据username到acl_admin查找,username就是客户端输入的账号
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            //说明username错误
            throw new UsernameNotFoundException(username + "用户名不正确");
        }
        //查询当前用户有哪些权限,然后告诉SpringSecurity
        List<Permission> permissionList = permissionService.findPermissionListByAdminId(admin.getId());
        //SpringSecurity需要的是GrantedAuthority类型的集合
        //那么我们就要将permissionList映射成List<GrantedAuthority>
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            grantedAuthorityList = permissionList.stream()
                    .filter(permission -> {
                        return permission.getType() == 2;
                    })
                    .map(permission -> {
                        //创建SimpleGrantedAuthority的时候要传入一个字符串用于标示权限
                        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getCode());
                        return simpleGrantedAuthority;
                    })
                    .collect(Collectors.toList());
        }
        return new User(username, admin.getPassword(), grantedAuthorityList);
    }
}
