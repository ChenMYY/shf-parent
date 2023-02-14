package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-07  11:15
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;
    @Override
    public BaseMapper<House> getEntityMapper() {
        return houseMapper;
    }

    @Override
    public void publishOrUnPublish(Long id, Integer status) {
        //发布或者取消发布，其实就是修改房源的status
        House house = new House();
        house.setId(id);
        house.setStatus(status);

        houseMapper.update(house);
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        //1. 使用分页插件开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2. 调用持久层方法根据条件查询
        List<HouseVo> houseVoList = houseMapper.findListPage(houseQueryVo);
        return new PageInfo<>(houseVoList, SHFConstant.PaginationConstant.DEFAULT_NAVIGATION_PAGES);
    }
}
