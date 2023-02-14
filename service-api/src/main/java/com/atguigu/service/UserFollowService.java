package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2023-02-11  08:50
 */
public interface UserFollowService {
    /**
     * 根据userId和houseId查询关注记录
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(Long userId, Long houseId);

    /**
     * 添加关注
     * @param userFollow
     */
    void addFollow(UserFollow userFollow);

    /**
     * 查询房源的关注列表分页信息
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId);

    /**
     * 取消关注
     * @param userFollow
     */
    void cancelFollow(UserFollow userFollow);
}
