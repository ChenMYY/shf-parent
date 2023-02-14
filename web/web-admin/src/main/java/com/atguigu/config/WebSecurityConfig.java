package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2023-02-13  08:51
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShfAccessDeniedHandler accessDeniedHandler;
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //为了指定内存中的账号密码，用于与用户输入的账号密码进行校验
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("12345678"))
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/static/**","/login").permitAll()  //允许匿名用户访问的路径
            .anyRequest().authenticated()    // 其它页面全部需要验证
            .and()
            .formLogin()
            .loginPage("/login")    //用户未登录时，访问任何需要权限的资源都转跳到该路径，即登录页面，此时登陆成功后会继续跳转到第一次访问的资源页面（相当于被过滤了一下）
            .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径
            .and()
            .logout()
            .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
            .logoutSuccessUrl("/login");//用户退出后要被重定向的url

        //关闭跨域请求伪造
        http.csrf().disable();

        //允许iframe嵌套显示
        http.headers().frameOptions().disable();

        //配置权限拒绝页面
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
