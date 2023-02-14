package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-03  10:51
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;
    private final String PAGE_INDEX = "role/index";
    private final String PAGE_EDIT = "role/edit";
    private final String LIST_ACTION = "redirect:/role";
    @RequestMapping
    public String index(@RequestParam Map<String,String> filters, Model model){
        //1. 调用业务层的方法做分页查询
        PageInfo<Role> page = roleService.findPage(filters);
        //2. 将分页查询到的数据存储到请求域
        model.addAttribute("page",page);
        //3. 将搜索条件存储到请求域:因为页面上要回显搜索条件
        model.addAttribute("filters",filters);
        //3. 返回页面的逻辑视图
        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Role role, Model model){
        //1. 调用业务层的方法保存角色信息
        roleService.insert(role);
        //2. 显示成功页面
        return successPage(model,"新增角色信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long roleId,Model model){
        //1. 调用业务层的方法根据roleId查询role
        Role role = roleService.getById(roleId);
        //2. 将role存储到请求域
        model.addAttribute("role",role);
        //3. 返回页面的逻辑视图
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Role role, Model model){
        //1. 调用业务层的方法修改
        roleService.update(role);
        //2. 显示成功页面
        return successPage(model,"修改角色信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long roleId){
        //1. 调用业务层的方法根据id删除
        roleService.delete(roleId);
        //2. 重定向访问首页
        return LIST_ACTION;
    }

    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long roleId,Model model){
        //1. 查询JSON数组数据以zNodes作为key存储到请求域
        //数组中的每一个JSON应该具备哪些key:id、name、pId、checked、open(只有一级、二级菜单才有)
        List<Map<String,Object>> zNodes = permissionService.findZNodes(roleId);
        //2. 将zNodes转成JSON再存储到请求域
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        //3. 将roleId存储到请求域
        model.addAttribute("roleId",roleId);

        return PAGE_ASSIGN_SHOW;
    }

    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,
                                   @RequestParam("permissionIds") List<Long> permissionIds,
                                   Model model){
        //调用业务层的方法保存分配权限信息
        permissionService.assignPermission(roleId,permissionIds);

        return successPage(model,"保存分配权限信息成功");
    }
}
