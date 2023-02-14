package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-07  14:47
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {
    /**
     * 根据房源id和type查询图片列表
     * @param houseId
     * @param type
     * @return
     */
    List<HouseImage> findImageList(@Param("houseId") Long houseId, @Param("type") Integer type);
}
