package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constants.SHFConstant;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-11  08:58
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public void addFollow(UserFollow userFollow) {
        userFollowMapper.insert(userFollow);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId) {
        //1. 开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2. 调用持久层的方法查询
        List<UserFollowVo> userFollowVoList = userFollowMapper.findListPage(userId);
        return new PageInfo<>(userFollowVoList, SHFConstant.PaginationConstant.DEFAULT_NAVIGATION_PAGES);
    }

    @Override
    public void cancelFollow(UserFollow userFollow) {
        userFollowMapper.delete(userFollow);
    }
}
