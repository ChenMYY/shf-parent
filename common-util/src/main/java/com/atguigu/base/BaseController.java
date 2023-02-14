package com.atguigu.base;

import org.springframework.ui.Model;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2023-02-05  09:05
 */
public class BaseController {
    private final String PAGE_SUCCESS = "common/successPage";
    public String successPage(Model model,String messagePage){
        //2. 将修改成功信息存储到请求域
        model.addAttribute("messagePage",messagePage);
        //3. 显示成功页面
        return PAGE_SUCCESS;
    }
}
