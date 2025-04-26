package com.mfl.service;

import com.mfl.config.Jdbc;
import com.mfl.dao.UserDao;
import com.mfl.dto.UserDto;
import com.mfl.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;

public class UserService {
        UserDao userDao = new UserDao();

        public boolean registerUser(HttpServletRequest req, HttpServletResponse resp) {
            UserDto dto = new UserDto();
            dto=dto.getUser(req);
            try {
                userDao.insertUserByPhone(dto, Jdbc.getConnection());
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public UserDto loginUser(HttpServletRequest req, HttpServletResponse resp) {
            UserDto dto = new UserDto();
            dto=dto.loginUser(req);

            if(!dto.isPhone) {
                try{
                    System.out.println(100);
                    User user=userDao.queryUserByEmail(dto,Jdbc.getConnection()); // 查询用户
                    dto.phoneNumber=user.getPhoneNumber();
                    dto.email=user.getEmail();
                    dto.nickname=user.getNickname();
                    dto.session=user.getSession();
                    dto.wins=user.getWins();
                    dto.score=user.getScore();
                    return dto;
                }catch(SQLException e) {
                    System.out.println(e.getMessage());
                }
            }else {
                try {
                    User user=userDao.queryUserByPhone(dto,Jdbc.getConnection()); // 查询用户
                    if(user!=null) {
                        dto.phoneNumber = user.getPhoneNumber();
                        dto.email = user.getEmail();
                        dto.nickname = user.getNickname();
                        dto.session = user.getSession();
                        dto.wins = user.getWins();
                        dto.score = user.getScore();
                        return dto;
                    }else{
                        return null;
                    }
                }catch(Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return null;
        }
}
