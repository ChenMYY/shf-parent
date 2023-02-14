package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2023-02-06  11:27
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public BaseMapper<Dict> getEntityMapper() {
        return dictMapper;
    }

    @Override
    public List<Map<String, Object>> findZnodes(Long parentId) {
        //1. 调用持久层的方法根据父节点id查询子节点列表
        List<Dict> dictList = dictMapper.findDictListByParentId(parentId);
        //2. 将List<Dict> 映射成 List<Map<String, Object>>并返回
        /*List<Map<String,Object>> znodes = new ArrayList<>();
        for (Dict dict : dictList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",dict.getId());
            hashMap.put("name",dict.getName());
            hashMap.put("isParent",!StringUtils.isEmpty(dict.getDictCode()));
            znodes.add(hashMap);
        }*/

        //使用Stream流式操作
        List<Map<String, Object>> znodes = dictList.stream()
                .map(dict -> {
                    //每一个dict对应一个map对象
                    Map<String, Object> map = new HashMap<>();
                    //往map对象中存储三个键值对
                    map.put("id", dict.getId());
                    map.put("name", dict.getName());
                    //怎么判断一个节点是否是父节点
                    map.put("isParent",dictMapper.findChildCountByParentId(dict.getId()) > 0);
                    return map;
                })
                .collect(Collectors.toList());
        return znodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        return dictMapper.findDictListByParentDictCode(parentDictCode);
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findDictListByParentId(parentId);
    }
}
