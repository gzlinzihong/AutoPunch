package edu.gdpu.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ilanky
 * @date 2020年 05月09日 05:17:39
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if(user==null){
            request.setAttribute("msg","请先登陆！");
            request.getRequestDispatcher("/").forward(request,response);
            return false;
        }else {
            return true;
        }
    }
}
