package scc.server.controller;


import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import scc.server.bean.RequestResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import scc.server.bean.SCCUser;
import scc.server.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UserController {

    private static final String LOGIN_STATUS_FAIL = "1";
    private static final String LOGIN_STATUS_SUCCESS = "0";
    private static final String LOGIN_STATUS_EXPIRE = "2";

    @Autowired
    IUserService userService;

    //1、登陆失败
    @PostMapping("/loginfail}")
    public RequestResult loginFail(){
        return new RequestResult(LOGIN_STATUS_FAIL,"用户名/用户密码错误",null);
    }

    //2、登陆成功
    @PostMapping("/loginsuccess/{token}")
    public RequestResult loginSuccess(@PathVariable String token){
        return new RequestResult(LOGIN_STATUS_SUCCESS,"登陆成功",
                ImmutableMap.of("token",token));

    }

    //3、登陆超时
    @PostMapping("/loginexpire")
    public RequestResult loginExpire(@PathVariable String token){
        return new RequestResult(LOGIN_STATUS_EXPIRE,"登陆超时", null);

    }

    @RequestMapping("/index")
    void handleFoo(HttpServletResponse response) throws IOException {
        response.sendRedirect("index");
    }

    @PostMapping("/login")
    public RequestResult login(HttpServletRequest request,HttpServletResponse response){
        SCCUser sccUser = userService.login(request.getParameter("username"),request.getParameter("password"));
        if(sccUser != null){
            return new RequestResult(LOGIN_STATUS_SUCCESS,"登陆成功", null);

        }else{
            return new RequestResult(LOGIN_STATUS_FAIL,"登陆失败", null);

        }

    }

}
