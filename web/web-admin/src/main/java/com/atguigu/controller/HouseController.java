package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.constants.SHFConstant;
import com.atguigu.en.HouseImageType;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
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
 * 日期2023-02-07  11:10
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";

    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;
    @RequestMapping
    public String index(@RequestParam Map<String,String> filters, Model model){
        //1. 调用业务层的方法分页查询数据，并存储到请求域
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page",page);
        //2. 将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        //3. 初始化页面数据
        initData(model);
        return PAGE_INDEX;
    }

    /**
     * 初始化页面数据
     * @param model
     */
    private void initData(Model model) {
        //3. 查询页面初始化数据，并存储到请求域
        //3.1 所有的小区
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList",communityList);
        //3.2 所有户型
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.HOUSE_TYPE);
        model.addAttribute("houseTypeList",houseTypeList);
        //3.3 所有楼层
        List<Dict> floorList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.FLOOR);
        model.addAttribute("floorList",floorList);
        //3.4 所有建筑结构
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.BUILD_STRUCTURE);
        model.addAttribute("buildStructureList",buildStructureList);
        //3.5 所有朝向
        List<Dict> directionList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.DIRECTION);
        model.addAttribute("directionList",directionList);
        //3.6 所有装修情况
        List<Dict> decorationList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.DECORATION);
        model.addAttribute("decorationList",decorationList);
        //3.7 所有房屋用途
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(SHFConstant.DictConstant.HOUSE_USE);
        model.addAttribute("houseUseList",houseUseList);
    }

    @GetMapping("/create")
    public String create(Model model){
        //初始化页面数据
        initData(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(House house,Model model){
        //1. 由于当前是新增房源，并没有对其进行发布，所以设置房源状态为0表示未发布(用枚举)
        house.setStatus(HouseStatus.UNPUBLISHED.getCode());
        //2. 调用业务层的方法保存房源信息
        houseService.insert(house);
        return successPage(model,"保存房源信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询房源信息,并存储到请求域
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        //2. 初始化页面数据
        initData(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(House house,Model model){
        //1. 调用业务层的方法保存更新
        houseService.update(house);
        return successPage(model,"修改房源信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法删除
        houseService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        //1. 调用业务层的方法发布或者取消发布房源房源
        houseService.publishOrUnPublish(id,status);
        return LIST_ACTION;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,Model model){
        //1. 查询当前房源信息，存储到请求域
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        //2. 查询当前房源所属的小区信息，存储到请求域
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community",community);
        //3. 查询当前房源的房源图片列表、房产图片列表存储到请求域
        //3.1 查询房源图片列表存储到请求域
        List<HouseImage> houseImage1List = houseImageService.findImageList(id, HouseImageType.RESOURCE.getType());
        model.addAttribute("houseImage1List",houseImage1List);
        //3.2 查询房产图片列表存储到请求域
        List<HouseImage> houseImage2List = houseImageService.findImageList(id, HouseImageType.PROPERTY.getType());
        model.addAttribute("houseImage2List",houseImage2List);
        //4. 查询当前房源的经纪人列表，存储到请求域
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerList(id);
        model.addAttribute("houseBrokerList",houseBrokerList);
        //5. 查询当前房源的房东列表，存储到请求域
        List<HouseUser> houseUserList = houseUserService.findHouseUserList(id);
        model.addAttribute("houseUserList",houseUserList);
        return PAGE_SHOW;
    }
}
