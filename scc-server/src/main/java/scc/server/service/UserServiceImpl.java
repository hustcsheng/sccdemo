package scc.server.service;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import scc.server.bean.SCCUser;
import scc.server.dao.TSccUserMapper;
import scc.server.dao.po.TSccUser;
import scc.server.util.RedisStringUtils;
import scc.server.util.SnowflakeIdGenertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

@Component
public class UserServiceImpl implements IUserService {


    @Autowired
    TSccUserMapper tSccUserMapper;

    @Autowired
    private RedisStringUtils redisStringUtils;

    @Autowired
    Gson gson;

    @Override
    public SCCUser login(String username, String password) {

        if(StringUtils.isAllBlank(username,password)){
            return null;
        }

        TSccUser tSccUser = new TSccUser();
        tSccUser.setUsername(username);
        tSccUser.setPassword(password);
        //数据库查询
        TSccUser userResult = tSccUserMapper.selectByNameAndPassword(tSccUser);
        if(userResult == null){
            return null; //没有查到用户
        }else{
            //更新数据库登陆时间
//            updateDbUser(userResult);
            //更新缓存
//            updateCacheUser(userResult);
            SCCUser user = new SCCUser(userResult.getUsername(), userResult.getLastLoginDate(), userResult.getLastLoginTime());
            return user;
        }
    }

    @Override
    public boolean userExistInCache(String token) {
        if(StringUtils.isAllBlank(token)){
            return false;
        }
        String value = redisStringUtils.getValue(RedisStringUtils.USER_CACHE_PREFIX+token);
        if(StringUtils.isEmpty(value)){
            redisStringUtils.setKey(RedisStringUtils.USER_CACHE_PREFIX+token,value,EXPIRE_SECONDS);
            return true;
        }

        return false;

    }

    /**
     * 更新缓存信息
     * @param user
     */
    private final int EXPIRE_SECONDS = 60*60*24;
    private void updateCacheUser(SCCUser user) {
        SnowflakeIdGenertor util = new SnowflakeIdGenertor();
        user.setUniqueId(util.nextId());//生产分布式唯一id
        redisStringUtils.setKey(RedisStringUtils.USER_CACHE_PREFIX+user.getUserName(),
                gson.toJson(user),EXPIRE_SECONDS);
    }

    /**
     * 更新上次登陆时间
     * @param user
     */
    private void updateDbUser(SCCUser user){
        //更新javaBean的最新登陆时间
        user.setLastLoginDateTime(DateFormatUtils.format(new Date(),"yyyyMMdd HH:mm:ss"));

        //保存的到数据库
        String[] dateTime = user.getLastLoginDateTime().split(" ");

//        sst.update("userMapper.updateUserLoginTime",
//            ImmutableMap.of("UserName",user.getUserName(),
//                    "LastLoginDate",dateTime[0],
//                    "LastLoginTime",dateTime[1]));
    }

    private SCCUser convertDb2Bean(Map<String,String> res){
        return new SCCUser(res.get("username"),res.get("lastlogindate"),res.get("lastlogintime"));
    }
}
