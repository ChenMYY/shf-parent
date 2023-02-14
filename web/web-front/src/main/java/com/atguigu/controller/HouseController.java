package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.en.HouseImageType;
import com.atguigu.entity.*;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 * @author Leevi
 * 日期2023-02-10  09:29
 * 1. POJO:封装与数据库表有关的数据
 * 2. BO:业务对象(里面有一些业务相关操作方法)
 * 3. VO:封装与视图相关的数据
 * 4. DTO:用于服务间的数据传输
 */
@RestController
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private UserFollowService userFollowService;
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable("pageNum") Integer pageNum,
                       @PathVariable("pageSize") Integer pageSize,
                       @RequestBody HouseQueryVo houseQueryVo){
        //1. 调用业务层的方法查询
        PageInfo<HouseVo> page = houseService.findListPage(pageNum,pageSize,houseQueryVo);

        return Result.ok(page);
    }

    @GetMapping("/info/{houseId}")
    public Result info(@PathVariable("houseId") Long houseId, HttpSession session){
        //1. 查询房源信息
        House house = houseService.getById(houseId);
        //2. 查询小区信息
        Community community = communityService.getById(house.getCommunityId());
        //3. 查询经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerList(houseId);
        //4. 查询房源图片列表
        List<HouseImage> houseImage1List = houseImageService.findImageList(houseId, HouseImageType.RESOURCE.getType());

        //5. 将上述查询出来的四个数据存储到Map中
        Map<String,Object> map = new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);
        //判断当前用户是否已关注该房源:
        /*Boolean isFollow = false;
        UserInfo userInfo  = (UserInfo) session.getAttribute("USER");
        if (userInfo != null) {
            //2. 如果当前已登录，那么就以当前用户的id和当前房源的id到user_follow表中查询数据,如果能查询到数据表示已关注
            if (userFollowService.findByUserIdAndHouseId(userInfo.getId(),houseId) != null) {
                isFollow = true;
            }
        }
        map.put("isFollow",isFollow);*/

        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        map.put("isFollow",userInfo != null && userFollowService.findByUserIdAndHouseId(userInfo.getId(),houseId) != null);
        return Result.ok(map);
    }
}
