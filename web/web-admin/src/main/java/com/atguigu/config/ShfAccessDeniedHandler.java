package com.atguigu.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2023-02-13  11:13
 */
@Component
public class ShfAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //1. 获取你是谁
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //2. 获取你做了啥
        String uri = httpServletRequest.getRequestURI();
        //3. 通过日志记录下来
        System.out.println(user.getUsername() + "尝试做" + uri + "被拒绝");
        //4. 访问拒绝页面
        httpServletResponse.sendRedirect("/auth");
    }
}
