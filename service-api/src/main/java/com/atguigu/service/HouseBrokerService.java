package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-07  14:42
 */
public interface HouseBrokerService extends BaseService<HouseBroker> {
    /**
     * 根据房源id查询经纪人列表
     * @param houseId
     * @return
     */
    List<HouseBroker> findHouseBrokerList(Long houseId);
}
