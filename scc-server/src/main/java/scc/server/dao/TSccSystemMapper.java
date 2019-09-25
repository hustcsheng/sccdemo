package scc.server.dao;

import java.util.List;
import scc.server.dao.po.TSccSystem;

public interface TSccSystemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSccSystem record);

    TSccSystem selectByPrimaryKey(Integer id);

    List<TSccSystem> selectAll();

    int updateByPrimaryKey(TSccSystem record);
}