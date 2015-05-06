package com.example.session.dao;

import com.example.session.dao.mapper.SessionMapper;
import com.example.session.model.Session;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(SessionMapper.class)
public interface SessionDao {

    @SqlUpdate("drop table if exists SESSION; create table SESSION(ACCESSTOKEN VARCHAR(50) PRIMARY KEY, CALLERID VARCHAR(50));")
    void createTable();

    @SqlQuery("select * from SESSION where ACCESSTOKEN = :accessToken")
    Session findByAccessToken(@Bind("accessToken") String accessToken);

    @SqlUpdate("delete from SESSION where ACCESSTOKEN = :accessToken")
    int deleteByAccessToken(@Bind("accessToken") String accessToken);

    @SqlUpdate("insert into SESSION (ACCESSTOKEN, CALLERID) values (:accessToken, :callerId)")
    int insert(@BindBean Session session);
}