package scc.server.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import scc.server.bean.Param;
import scc.server.bean.RequestResult;
import scc.server.bean.SCCSystem;
import scc.server.bean.SCCUser;
import scc.server.service.SysService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SysController {

    @Autowired
    SysService sysService;

    //4、更新配置项
    @PostMapping("/updateparam/{paramId}/{paramValue}")
    public RequestResult updateParam(@PathVariable String paramId,
                                     @PathVariable String paramValue){
        int affectCount = sysService.updateParam(paramId,paramValue);
        return new RequestResult("0",null,affectCount);

    }

    //3、查询配置项参数
    @PostMapping("/queryparaminfo/{sysId}/{envName}")
    public RequestResult queryParm(@PathVariable String sysId,
                                   @PathVariable String envname){
        List<Param> params = sysService.queryParamInfo(sysId,envname);
        return new RequestResult("0",null,params);
    }

    //1、查询用户信息
    @PostMapping("/queryuserinfo")
    public RequestResult queryForUserInfo(HttpServletRequest request){
        String token = request.getParameter("token");
        if(StringUtils.isEmpty(token)){
            return new RequestResult("1","token为空，非法请求",null);
        }
        SCCUser user = sysService.queryUserInfo(token);
        return new RequestResult("0",null,user);
    }


    //2、查询子系统信息
    @PostMapping("/querysysinfo")
    public RequestResult queryForSystemNames(){
        List<SCCSystem> sccSystemList = sysService.querySysInfo();
        return new RequestResult("0",null,sccSystemList);
    }
}
