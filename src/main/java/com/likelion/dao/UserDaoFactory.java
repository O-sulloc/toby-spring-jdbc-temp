package com.likelion.dao;

import com.likelion.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDaoFactory {

    @Bean
    public UserDao awsUserDao(){

        return new UserDao(new AwsConnectionMaker());
    }
}
