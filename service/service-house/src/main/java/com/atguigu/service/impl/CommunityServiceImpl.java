package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-07  08:59
 */
@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Override
    public BaseMapper<Community> getEntityMapper() {
        return communityMapper;
    }

    @Override
    public void delete(Long id) {
        //1. 判断当前要删除的这个小区内是否有房源，如果有则不能删除，如果没有才能删除
        //1.1 在hse_house表中根据community_id查询房源数量
        Long count = houseMapper.findCountByCommunityId(id);
        //1. 判断count是否大于0
        if (count > 0) {
            //说明当前要删除的小区内有房源，则不能删除
            throw new RuntimeException("this community contains house,can not delete!!!");
        }
        //说明当前要删除的小区内没有房源，则调用父类的方法删除
        super.delete(id);
    }
}
