package scc.server.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import scc.server.bean.Param;
import scc.server.bean.SCCSystem;
import scc.server.bean.SCCUser;
import scc.server.util.RedisStringUtils;

import java.util.List;
import java.util.Map;

@Component
public class SysServiceImpl implements SysService {

    @Autowired
    RedisStringUtils redisStringUtils;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private Gson gson;


    @Override
    public SCCUser queryUserInfo(String token) {
        String value = redisStringUtils.getValue(RedisStringUtils.USER_CACHE_PREFIX + token);
        SCCUser sccUser = gson.fromJson(value,SCCUser.class);
        return sccUser;
    }

    @Override
    public List<SCCSystem> querySysInfo() {
        List<Map<String,Object>> dbRes = sqlSessionTemplate.selectList("sysMapper.querySysInfo");

        return convertdb2Obj(dbRes);
    }

    private List<SCCSystem> convertdb2Obj(List<Map<String, Object>> dbRes) {
        if(CollectionUtils.isEmpty(dbRes)){
            return null;
        }
        List<SCCSystem> result = Lists.newArrayList();
        dbRes.forEach(k->{
            result.add(new SCCSystem(((Integer) k.get("sysid")),
                    k.get("sysname").toString()));
        });
        return  result;
    }

    @Override
    public List<Param> queryParamInfo(String sysId, String envName) {

        List<Map<String,Object>> res = sqlSessionTemplate.selectList("sysMapper.queryParamInfo",
                ImmutableMap.of("SysId",sysId,"EnvName",envName));

        //省略转换方法
        if(CollectionUtils.isEmpty(res)){
            return null;
        }
        List<Param> result = Lists.newArrayList();
        res.forEach(k->{
            int id = (int) k.get("id");
            String paramKey = k.get("paramKey").toString();
            String paramValue = k.get("paramValue").toString();
            result.add(new Param(id,paramKey,paramValue));
        });
        return result;
    }

    @Override
    public int updateParam(String paramId, String paramValue) {
        int affectConunt = sqlSessionTemplate.update("sysMapper.updateParam",
        ImmutableMap.of("Id",paramId,"ParamValue",paramValue));
        return affectConunt;
    }
}
