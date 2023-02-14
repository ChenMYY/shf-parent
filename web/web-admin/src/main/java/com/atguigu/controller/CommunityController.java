package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
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
 * 日期2023-02-07  08:53
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";
    private static final String PAGE_CREATE = "community/create";

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;
    @RequestMapping
    public String index(@RequestParam Map<String,String> filters, Model model){
        //1. 调用业务层的方法查询分页信息
        PageInfo<Community> page = communityService.findPage(filters);
        //2. 将分页信息存储到请求域
        model.addAttribute("page",page);
        //3. 将搜索条件存储到请求域
        //判断:如果客户端没有带areaId和plateId那么我们可以赋默认值为0
        if (!filters.containsKey("areaId")) {
            filters.put("areaId","0");
        }

        if (!filters.containsKey("plateId")) {
            filters.put("plateId","0");
        }
        model.addAttribute("filters",filters);
        //4. 查询北京的所有区域(hse_dict)，并且存储到请求域
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create(Model model){
        //1. 查询北京的所有区域存储到请求域
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);
        //2. 返回逻辑视图
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Community community,Model model){
        //1. 调用业务层的方法保存小区信息
        communityService.insert(community);
        //2. 显示成功页面
        return successPage(model,"保存小区信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询小区信息
        Community community = communityService.getById(id);
        //2. 将查询到的小区信息存储到请求域
        model.addAttribute("community",community);
        //3. 查询北京的所有区域，并存储到请求域
        List<Dict> areaList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.BEIJING);
        model.addAttribute("areaList",areaList);
        //4. 返回视图
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Community community,Model model){
        //1. 调用业务层的方法修改小区信息
        communityService.update(community);
        //2. 显示成功页面
        return successPage(model,"修改小区信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法根据id删除小区
        communityService.delete(id);
        //2. 重定向查询小区的首页
        return LIST_ACTION;
    }
}
