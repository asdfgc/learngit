package com.example.demo.filer;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @WebFilter用于将一个类声明为过滤器
 * filterName属性用于指定过滤器的name
 * urlPatterns用于指定一组过滤器的URL匹配模式
 * value等价于urlPatterns属性，但是2者不可同时使用
 *
 *
 */
@WebFilter(filterName = "ayUserFilter",urlPatterns = "/*")
public class AyUserFilter implements Filter {


    /**
     * 过滤器
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


        System.out.println("---------> init");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("-----> doFilter");

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

        System.out.println("------> destroy");
    }




}
