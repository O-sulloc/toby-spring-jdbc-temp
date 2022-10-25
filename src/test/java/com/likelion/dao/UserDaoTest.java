package com.likelion.dao;

import com.likelion.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {
    @Autowired
    ApplicationContext context;
    //Spring 도입 – 98p Test Code를 ApplicationContext사용하게 수정 의존성 주입
    UserDao userDao;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp(){
        this.userDao = context.getBean("awsUserDao", UserDao.class);
        this.user1 = new User("JUnitIdsu1", "김정현", "1234");
        this.user2 = new User("JUnitIdsu2", "kjh", "5678");
        this.user3 = new User("JUnitIdsu3", "jhk", "9012");
    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        //UserDao userDao = context.getBean("awsUserDao", UserDao.class); 필요없음. beforeEach

        userDao.getDeleteAll();
        userDao.getCount();
        assertEquals(0,userDao.getCount());

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
        userDao.getCount();
        assertEquals(3,userDao.getCount());

        User findUser = userDao.getUserOne(user1.getId());
        assertEquals(user1.getName(), findUser.getName());
    }

    //@Test
    void noSuchId() throws SQLException, ClassNotFoundException {
        assertThrows(EmptyResultDataAccessException.class, ()->{
            userDao.getUserOne("NoId");
        });
    }
}