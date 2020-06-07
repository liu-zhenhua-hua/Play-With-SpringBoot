package org.cherry.bookstore.component;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tony on 2020/6/7.
 */

/*
    登录检查
 */

public class LoginHandlerInterceptor implements HandlerInterceptor {
    //方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Object user = request.getSession().getAttribute("loginUserName");
        if(user == null){
            //未登录
            request.setAttribute("msg","You don't have authorized Login First ");
            request.getRequestDispatcher("/").forward(request,response);
            return false;
        }else{
            //已登录
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
