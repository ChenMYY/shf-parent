package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-07  11:15
 */
public interface HouseService extends BaseService<House> {
    /**
     * 发布或者取消发布房源
     * @param id
     * @param status
     */
    void publishOrUnPublish(Long id, Integer status);

    /**
     * 用户前台搜索房源分页信息
     * @param pageNum
     * @param pageSize
     * @param houseQueryVo
     * @return
     */
    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);
}
