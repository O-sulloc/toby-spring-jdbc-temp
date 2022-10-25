package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserDao {
    private DataSource dataSource;

    //jdbc template p262
    private JdbcTemplate jdbcTemplate; //스프링이 제공하는 JDBC 코드용 기본 템플릿

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    public void add(final User user) throws SQLException {
        //익명 내부 클래스 적용. 람다식
        this.jdbcTemplate.update("insert into users values (?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User getUserOne(String id) throws SQLException, ClassNotFoundException {
        String sql = "select * from users where id=?";
        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getString("id"), rs.getNString("name"), rs.getNString("password"));

                return user;
            }
        };

        return this.jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void getDeleteAll() throws SQLException {
        //this.jdbcContext.executeSql("delete from users"); //여기서 쿼리만 넘기면 jdbcContext에 있는 executeSql 메서드가 처리해줌
        //이것도 이제 안 씀. jdbcTemplate으로 처리할 것임.

        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAllUsers(){
        String sql = "select * from users order by id";
        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));

                return user;
            }
        };
        return this.jdbcTemplate.query(sql, rowMapper);
    }
}
