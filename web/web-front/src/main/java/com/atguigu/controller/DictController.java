package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2023-02-10  09:09
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;
    @GetMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode") String dictCode){
        List<Dict> dictList = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictList);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        List<Dict> dictList = dictService.findDictListByParentId(parentId);

        return Result.ok(dictList);
    }
}
