package com.example.person.dao.mapper;

import com.example.api.model.Person;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements ResultSetMapper<Person>
{
    public Person map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        Person person = new Person(resultSet.getString("FIRSTNAME"), resultSet.getString("LASTNAME"));
        person.setId(resultSet.getInt("ID"));

        return person;
    }
}
