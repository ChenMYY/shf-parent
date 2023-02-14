package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-07  10:29
 */
public interface HouseMapper extends BaseMapper<House> {
    /**
     * 根据小区id查询房源数量
     * @param communityId
     * @return
     */
    Long findCountByCommunityId(Long communityId);

    /**
     *用户前台搜索房源列表
     * @param houseQueryVo
     * @return
     */
    List<HouseVo> findListPage(HouseQueryVo houseQueryVo);
}
