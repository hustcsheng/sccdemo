package scc.server.filter;

import scc.server.bean.SCCUser;
import scc.server.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoginFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private IUserService IUserService;

    public LoginFilter(IUserService IUserService) {
        this.IUserService = IUserService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("注册登陆过滤器");
    }

    //不需要身份验证的URI(配置文件读取)
    private static final String[] EXCLUDE_CHECK_URI = {"/queryparaminfo"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path  = httpRequest.getRequestURI();
        logger.info("path:{}",path);
        if(path.contains("/login")){
            boolean isExclude = false;
            for(String excludeURI : EXCLUDE_CHECK_URI){
                if (path.contains(excludeURI)){
                    isExclude = true;
                    break;
                }
            }
            if(isExclude){
                //1、不需要验证身份
                filterChain.doFilter(request,response);
            }else if(IUserService.userExistInCache(httpRequest.getParameter("token"))){
                //验证通过
                filterChain.doFilter(request,response);
            }else{
                //验证未通过，跳转到登陆页面
                httpRequest.getRequestDispatcher("/loginexpire").forward(request,response);
            }
        }else{
            //登陆请求
            SCCUser sccUser = IUserService.login(request.getParameter("username"),
                    request.getParameter("password"));
            if(sccUser == null){
                //2、未通过验证，重定向到登陆页
                httpRequest.getRequestDispatcher("/loginfail").forward(request,response);
            }else{
                //1、正确登陆
                httpRequest.getRequestDispatcher("/loginsuccess").forward(request,response);
            }


        }
    }

    @Override
    public void destroy() {
        logger.info("注销登陆过滤器");

    }
}
