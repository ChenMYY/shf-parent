package com.atguigu.base;

import com.atguigu.constants.SHFConstant;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2023-02-05  08:59
 */
public abstract class BaseServiceImpl<T> {
    /**
     * 获取持久层的对象
     * @return
     */
    public abstract BaseMapper<T> getEntityMapper();
    @Transactional
    public List<T> findAll() {
        return getEntityMapper().findAll();
    }

    @Transactional
    public void insert(T t) {
        getEntityMapper().insert(t);
    }

    @Transactional
    public T getById(Long id) {
        return getEntityMapper().getById(id);
    }

    @Transactional
    public void update(T t) {
        getEntityMapper().update(t);
    }

    @Transactional
    public void delete(Long id) {
        getEntityMapper().delete(id);
    }

    @Transactional
    public PageInfo<T> findPage(Map<String, String> filters) {
        //1. 获取pageNum，如果没有pageNum那么默认值为1
        int pageNum = CastUtil.castInt(filters.get("pageNum"), SHFConstant.PaginationConstant.DEFAULT_PAGE_NUM);
        //2. 获取pageSize，如果没有pageSize那么默认值为5
        int pageSize = CastUtil.castInt(filters.get("pageSize"), SHFConstant.PaginationConstant.DEFAULT_PAGE_SIZE);
        //3. 使用分页插件开启分页
        PageHelper.startPage(pageNum,pageSize);
        //4. 调用持久层的方法根据搜索条件进行搜索
        List<T> roleList = getEntityMapper().findPage(filters);
        //5. 封装分页数据
        //10 表示最多显示10个页码
        return new PageInfo<>(roleList,SHFConstant.PaginationConstant.DEFAULT_NAVIGATION_PAGES);
    }
}
