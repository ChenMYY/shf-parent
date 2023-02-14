package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-07  14:51
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    /**
     * 根据房源id查询经纪人列表
     * @param houseId
     * @return
     */
    List<HouseBroker> findHouseBrokerList(Long houseId);
}
