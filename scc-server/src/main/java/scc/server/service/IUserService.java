package scc.server.service;

import scc.server.bean.SCCUser;

/**
 * 用户业务逻辑接口
 */
public interface IUserService {

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    SCCUser login(String username, String password);

    /**
     * 判断token是否存在
     * @param token
     * @return
     */
    boolean userExistInCache(String token);
}
