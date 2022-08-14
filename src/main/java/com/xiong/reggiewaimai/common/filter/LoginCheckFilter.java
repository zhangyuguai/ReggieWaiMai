package com.xiong.reggiewaimai.common.filter;

import com.alibaba.fastjson.JSON;
import com.xiong.reggiewaimai.common.BaseContext;
import com.xiong.reggiewaimai.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xsy
 * @date 2022/8/10
 */
@Slf4j
public class LoginCheckFilter extends HttpFilter {

    //路径匹配
    private static final AntPathMatcher ANT_PATH_MATCHER =new AntPathMatcher();

    private static final  String[]  allowPath={
            "/backend/**",
            "/front/**",
            "/employee/login",
            "/employee/logout",
            "/user/sendMsg",
            "/user/login"
    };

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI();
        Boolean flag = isAllow(allowPath, uri);
        //允许通行的请求
        if(flag){
            chain.doFilter(request,response);
            return ;
        }

        log.info("拦截到的uri{}",uri);

        //查看是否登录
        Long employee = (Long) request.getSession().getAttribute("employee");
        //已经登录
        if(employee!=null){
            //设置当前登录用户id到threadLocal当中
            BaseContext.setCurrentId(employee);
            chain.doFilter(request,response);
            return;
        }

        //查看user是否已登录
        Long user = (Long) request.getSession().getAttribute("user");

        if ((user!=null)){
            //设置当前登录用户id到threadLocal当中
            BaseContext.setCurrentId(user);
            chain.doFilter(request,response);
            return;
        }

        //未登录,向前端写回json形式的流数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    //匹配路径
    public static Boolean isAllow(String[] allowPath,String uri){
        for (String s : allowPath) {
            boolean flag = ANT_PATH_MATCHER.match(s, uri);
            if(flag){
                return true;
            }
        }
        return false;
    }
}
