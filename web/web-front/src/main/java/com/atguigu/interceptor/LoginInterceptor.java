package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包名:com.atguigu.interceptor
 *
 * @author Leevi
 * 日期2023-02-11  09:10
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 校验当前是否已登录
        if (request.getSession().getAttribute("USER") == null) {
            //1.1 如果未登录，则告诉客户端请给我滚到登录页面去
            //1.1.1 构建要响应的Result对象
            Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
            //1.1.2 将result对象转成JSON字符串
            String jsonStr = JSON.toJSONString(result);
            //1.1.3 将接送字符串响应给客户端
            response.getWriter().write(jsonStr);
            return false;
        }
        //1.2 如果已登录，则放行
        return true;
    }
}
