package com.atguigu.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.manage.utils.HttpClientUtil;
import com.atguigu.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 拦截代码
        // 判断被拦截的请求的访问的方法的注解(是否时需要拦截的)
        HandlerMethod hm = (HandlerMethod)handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        //是否拦截
        if (methodAnnotation == null){
            //没有加注解，无需拦截
            return true;
        }

        String token = "";

        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if (StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }

        //普通登录在页面上通过ajax传递token；微博登录在重定向上携带制作的token
        //主动登录在这获取到token
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        //获取注解，验证是否必须登录，必须登录为true,不必须登录为false
        boolean loginSuccess = methodAnnotation.loginSuccess();

        // 如果先前登陆过，携带了token，调用认证中心进行验证token的真伪
        String success = "failed";
        Map<String, String> successMap = new HashMap<>();
        if (StringUtils.isNotBlank(token)){

            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip

            if (StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();//从request中获取ip
                if (StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }

            }

            String successJson = HttpClientUtil.doGet("http://passport.gmall.com:8085/verify?token=" + token + "&currentIp=" + ip);
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }

        if(loginSuccess){
            //必须登录
            if (!"success".equals(success)){
                //重定向回认证中心登录
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect("http://passport.gmall.com:8085/index?ReturnUrl=" + requestURL);
                return false;
            }
            // 需要将token携带的用户信息写入
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickname", successMap.get("nickname"));
            //验证通过，覆盖cookie中的token
            if(StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
            }

        }else {
            //不必须登录，但仍需验证
            if ("success".equals(success)){

                // 需要将token携带的用户信息写入
                request.setAttribute("memberId",successMap.get("memberId"));
                request.setAttribute("nickname",successMap.get("nickname"));

                if (StringUtils.isNotBlank(token)){
                    //验证通过，覆盖cookie中的token
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }

            }

        }

        return true;
    }

}
