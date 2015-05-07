package com.example.person.dao;

import com.example.api.model.Person;
import com.example.person.dao.mapper.PersonMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(PersonMapper.class)
public interface PersonDao {

    @SqlUpdate("drop table if exists PERSON; create table PERSON(ID INT PRIMARY KEY auto_increment, FIRSTNAME VARCHAR(50), LASTNAME VARCHAR(50));")
    void createTable();

    @SqlQuery("select * from PERSON")
    List<Person> getAll();

    @SqlQuery("select * from PERSON where ID = :id")
    Person findById(@Bind("id") int id);

    @SqlUpdate("delete from PERSON where ID = :id")
    int deleteById(@Bind("id") int id);

    @SqlUpdate("update PERSON set FIRSTNAME = :firstName, LASTNAME = :lastName where ID = :id")
    int update(@BindBean Person person);

    @SqlUpdate("insert into PERSON (FIRSTNAME, LASTNAME) values (:firstName, :lastName)")
    int insert(@BindBean Person person);
}