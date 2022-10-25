package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Map;

public class UserDao {
    private DataSource dataSource;

    private JdbcContext jdbcContext;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource); //
    }

    public void add(final User user) throws SQLException {
        //익명 내부 클래스 적용. 람다식
        this.jdbcContext.worktWithStatementStrategy(conn -> {
            PreparedStatement ps = conn.prepareStatement("insert into users values (?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            return ps;
        });
    }

    public User getUserOne(String id) throws SQLException, ClassNotFoundException {
        Connection conn = dataSource.getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users where id=?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null; //id에 해당하는 값 없을 때 예외처리 하기 위해서. 일단 user를 Null로 초기화
        if(rs.next()){
            //rs.next()가 true면(=값이 있으면,=있는 Id라면)

            user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
        }

        rs.close();
        ps.close();
        conn.close();

        if(user == null) throw new EmptyResultDataAccessException(1); //없는 아이디면 예외처리

        return user;
    }

    public void getDeleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users"); //여기서 쿼리만 넘기면 jdbcContext에 있는 executeSql 메서드가 처리해줌
    }

    public int getCount() throws SQLException {
        //db에 데이터 몇 개 있는지 확인하고 싶음

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            ps = conn.prepareStatement("SELECT count(*) FROM users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }



}
