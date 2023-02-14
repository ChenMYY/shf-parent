package com.atguigu.mapper;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-11  08:59
 */
public interface UserFollowMapper {
    /**
     * 根据userId和houseId查询关注记录
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    /**
     * 添加关注信息
     * @param userFollow
     */
    void insert(UserFollow userFollow);

    /**
     * 查询用户的关注列表
     * @param userId
     * @return
     */
    List<UserFollowVo> findListPage(Long userId);

    /**
     * 删除关注记录
     * @param userFollow
     */
    void delete(UserFollow userFollow);
}
