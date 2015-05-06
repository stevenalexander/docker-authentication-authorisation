package com.example.session.dao.mapper;

import com.example.session.model.Session;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionMapper implements ResultSetMapper<Session>
{
    public Session map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        return new Session(resultSet.getString("ACCESSTOKEN"), resultSet.getString("CALLERID"));
    }
}
