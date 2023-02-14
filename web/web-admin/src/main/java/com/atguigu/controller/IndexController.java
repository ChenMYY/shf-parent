package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-11  15:23
 */
@Controller
public class IndexController {
    private static final String PAGE_INDEX = "frame/index";
    @Reference
    private PermissionService permissionService;
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @RequestMapping("/")
    public String index(Model model){
        //获取当前登录的用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //根据username获取admin
        Admin admin = adminService.getByUsername(user.getUsername());
        Long adminId = admin.getId();

        //查询当前登录的用户有哪些权限，保存到请求域
        List<Permission> menu = permissionService.findMenu(adminId);
        model.addAttribute("permissionList",menu);
        //将当前用户信息存储到请求域
        model.addAttribute("admin",admin);
        //将当前用户的角色列表存储到请求域
        Map<String, List<Role>> roleListMap = roleService.findRoleListMap(adminId);
        model.addAttribute("roleList",roleListMap.get("assignRoleList"));
        return PAGE_INDEX;
    }
}
