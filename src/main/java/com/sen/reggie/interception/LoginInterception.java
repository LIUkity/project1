package com.sen.reggie.interception;

import com.alibaba.fastjson.JSON;
import com.sen.reggie.common.R;
import com.sen.reggie.entity.Employee;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//@Component
public class LoginInterception implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行了拦截器的preHandle方法");
        System.out.println(request.getSession().getAttribute("employee"));
        try{
            if (request.getSession().getAttribute("employee")!=null){

                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("请先登陆！");
        //response.sendRedirect("/backend/page/login/login.html");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("执行了拦截器的postHandle方法");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //System.out.println("执行了拦截器的afterCompletion方法");
    }


}
