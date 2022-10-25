package com.likelion.dao;

import com.likelion.domain.User;

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
        Connection conn = new AwsConnectionMaker().makeConnection();

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
        Map<String, String> env = System.getenv();

        String dbHost = env.get("DB_HOST");
        String dbUser = env.get("DB_USER");
        String dbPassword = env.get("DB_PASSWORD");

        Class.forName("com.mysql.cj.jdbc.Driver");
        //드라이버를 메모리에 로딩하는 과정. 생략 가능

        Connection conn = DriverManager.getConnection(dbHost, dbUser, dbPassword);

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users where id=?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();
        //userDao.add();

        //User user=userDao.getUserOne("idtest");
        //System.out.println(user.getName());
    }
}
