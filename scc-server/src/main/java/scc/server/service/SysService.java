package scc.server.service;

import scc.server.bean.Param;
import scc.server.bean.SCCSystem;
import scc.server.bean.SCCUser;

import java.util.List;

public interface SysService {

    SCCUser queryUserInfo(String token);

    List<SCCSystem> querySysInfo();

    List<Param> queryParamInfo(String sysId, String envName);

    int updateParam(String paramId, String paramValue);
}
