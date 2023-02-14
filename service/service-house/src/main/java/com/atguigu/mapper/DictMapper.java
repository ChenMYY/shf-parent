package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Dict;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2023-02-06  11:28
 */
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 根据父节点id查询子节点列表
     * @param parentId
     * @return
     */
    List<Dict> findDictListByParentId(Long parentId);

    /**
     * 根据父节点id查询子节点数量
     * @param parentId
     * @return
     */
    Long findChildCountByParentId(Long parentId);

    /**
     * 根据父节点的dictCode查询子节点列表
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);
}
