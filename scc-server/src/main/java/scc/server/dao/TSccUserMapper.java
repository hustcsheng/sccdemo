package scc.server.dao;

import java.util.List;
import scc.server.dao.po.TSccUser;

public interface TSccUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSccUser record);

    TSccUser selectByPrimaryKey(Integer id);

    List<TSccUser> selectAll();

    int updateByPrimaryKey(TSccUser record);

    TSccUser selectByNameAndPassword(TSccUser tSccUser);

}