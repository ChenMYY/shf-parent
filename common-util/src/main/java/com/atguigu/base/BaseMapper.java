package com.atguigu.base;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2023-02-05  08:49
 */
public interface BaseMapper<T> {
    /**
     * 查询所有
     * @return
     */
    List<T> findAll();

    /**
     * 新增
     * @param t
     */
    void insert(T t);

    /**
     * 根据roleId查询
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改
     * @param t
     */
    void update(T t);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Long id);

    /**
     * 根据搜索条件搜索
     * @param filters
     * @return
     */
    List<T> findPage(Map<String, String> filters);
}
