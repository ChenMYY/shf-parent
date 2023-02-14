package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-03  10:51
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    private static final String PAGE_CREATE = "admin/create";
    @Reference
    private AdminService adminService;

    private final String PAGE_INDEX = "admin/index";
    private final String PAGE_EDIT = "admin/edit";
    private final String LIST_ACTION = "redirect:/admin";

    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('admin.show')")
    @RequestMapping
    public String index(@RequestParam Map<String,String> filters, Model model){
        //1. 调用业务层的方法做分页查询
        PageInfo<Admin> page = adminService.findPage(filters);
        //2. 将分页查询到的数据存储到请求域
        model.addAttribute("page",page);
        //3. 将搜索条件存储到请求域:因为页面上要回显搜索条件
        model.addAttribute("filters",filters);
        //3. 返回页面的逻辑视图
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('admin.create')")
    @PostMapping("/save")
    public String save(Admin admin, Model model){
        //将admin的密码进行加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        //1. 调用业务层的方法保存用户信息
        adminService.insert(admin);
        //2. 显示成功页面
        return successPage(model,"新增用户信息成功");
    }

    @PreAuthorize("hasAuthority('admin.create')")
    @GetMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long adminId,Model model){
        //1. 调用业务层的方法根据adminId查询admin
        Admin admin = adminService.getById(adminId);
        //2. 将admin存储到请求域
        model.addAttribute("admin",admin);
        //3. 返回页面的逻辑视图
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/update")
    public String update(Admin admin, Model model){
        //1. 调用业务层的方法修改
        adminService.update(admin);
        //2. 显示成功页面
        return successPage(model,"修改用户信息成功");
    }

    @PreAuthorize("hasAuthority('admin.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long adminId){
        //1. 调用业务层的方法根据id删除
        adminService.delete(adminId);
        //2. 重定向访问首页
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){
        //1. 将用户id存储到请求域
        model.addAttribute("id",id);
        return PAGE_UPLOAD_SHOW;
    }

    @PreAuthorize("hasAuthority('admin.edit')")
    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file")MultipartFile multipartFile,
                         Model model) throws IOException {
        //1. 将客户端上传的图片保存到七牛云中
        //1.1 要获取一个唯一的名字，以这个唯一的名字(和原名字的后缀一样)保存到七牛云
        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
        //1.2 构建文件存储到服务器中的路径
        String filePath = "shf/"+ FileUtil.getRandomDirPath(SHFConstant.FileConstant.DEFAULT_DIR_LEVEL,SHFConstant.FileConstant.DEFAULT_DIR_LEVEL) + uuidName;
        //1.3 将文件以filePath上传到七牛云
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),filePath);

        //2. 修改admin的信息(head_url),改成当前这张图片的url
        //2.1 获取当前上传的这张图片的url
        String url = QiniuUtils.getUrl(filePath);
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);
        //2.2 调用业务层的update方法修改admin信息
        adminService.update(admin);

        //上传完之后，显示成功页面
        return successPage(model,"上传图片成功");
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //1. 将adminId存储到请求域
        model.addAttribute("adminId",id);
        //2. 查询出当前管理员未分配的角色列表unAssignRoleList以及当前管理员已分配的角色列表assignRoleList，并存储到请求域
        Map<String, List<Role>> roleListMap = roleService.findRoleListMap(id);
        model.addAllAttributes(roleListMap);
        return PAGE_ASSIGN_SHOW;
    }

    @PreAuthorize("hasAuthority('admin.assign')")
    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model){
        //调用业务层的方法给管理员分配角色
        roleService.assignRole(adminId,roleIds);

        return successPage(model,"分配角色信息成功");
    }
}
