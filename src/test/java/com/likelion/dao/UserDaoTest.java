package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDaoFactory().awsUserDao();
        String id = "JunitId3";

        userDao.add(new User(id, "Jname3","Jpw3"));

        User findUser =userDao.getUserOne(id);
        assertEquals("Jname3", findUser.getName());
    }

}