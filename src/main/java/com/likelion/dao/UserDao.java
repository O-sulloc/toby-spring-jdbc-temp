package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
import java.util.Map;

public class UserDao {
    private ConnectionMaker cm;

    public UserDao(){
        this.cm = new AwsConnectionMaker();
    }

    public UserDao(ConnectionMaker cm) {
        this.cm = cm;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection conn = cm.makeConnection();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users(id, name, password) VALUES (?,?,?)"
        );

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        int status = ps.executeUpdate(); //상태 확인

        ps.close();
        conn.close();
    }


    public User getUserOne(String id) throws SQLException, ClassNotFoundException {
        Connection conn = cm.makeConnection();

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
        //db에 데이터 다 지우고 싶음

        Connection conn = cm.makeConnection();

        PreparedStatement ps = conn.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public int getCount() throws SQLException {
        //db에 데이터 몇 개 있는지 확인하고 싶음

        Connection conn = new AwsConnectionMaker().makeConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        conn.close();

        return count;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();
        //userDao.add();
        //userDao.getCount();
        //System.out.println(userDao.getCount());
        //User user=userDao.getUserOne("idtest");
        //System.out.println(user.getName());
    }
}
