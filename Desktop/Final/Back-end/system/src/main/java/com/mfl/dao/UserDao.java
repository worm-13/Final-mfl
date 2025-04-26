package com.mfl.dao;

import com.mfl.config.Jdbc;
import com.mfl.dto.UserDto;
import com.mfl.model.Player;
import com.mfl.model.Room;
import com.mfl.model.User;
import com.mfl.util.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    // 用户注册
    public void insertUserByPhone(UserDto userregisterDto, Connection conn)throws SQLException {
        String sql = "insert into users (phoneNumber,email,nickname,password,session,wins,score) values(?,?,?,?,0,0,0)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userregisterDto.phoneNumber);
        pstmt.setString(2, userregisterDto.email);
        pstmt.setString(3,userregisterDto.nickname);
        pstmt.setString(4, PasswordUtils.hashPassword(userregisterDto.password));
        System.out.println(userregisterDto.phoneNumber);
        int a=0;
        if(a==0) {
            pstmt.executeUpdate();
        }
        pstmt.close();
        Jdbc.returnConnection(conn);
    }


    // 用户通过手机号登录
    public User queryUserByPhone(UserDto userregisterDto, Connection conn){
        String sql = "select * from users where phoneNumber =?";
        User user=new User("","","",0,0,0);
        try{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userregisterDto.phoneNumber);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            if (PasswordUtils.checkPassword(userregisterDto.password, rs.getString("password"))) {
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setWins(rs.getInt("wins"));
                user.setSession(rs.getInt("session"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setScore(rs.getInt("score"));
            }else {
                return null;
            }
        }
            pstmt.close();
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }finally{
            Jdbc.returnConnection(conn);
        }

        return user;
    }

    // 用户通过邮箱登录
    public User queryUserByEmail(UserDto userregisterDto, Connection conn) {
        String sql = "select * from users where email=?";
        User user=new User("","","",0,0,0);
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userregisterDto.email);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (PasswordUtils.checkPassword(userregisterDto.password, rs.getString("password"))) {
                    user.setNickname(rs.getString("nickname"));
                    user.setEmail(rs.getString("email"));
                    user.setWins(rs.getInt("wins"));
                    user.setSession(rs.getInt("session"));
                    user.setPhoneNumber(rs.getString("phoneNumber"));
                    user.setScore(rs.getInt("score"));
                } else {
                    return null;
                }
            }
            pstmt.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            Jdbc.returnConnection(conn);
        }
        return user;
    }

    public boolean checkAccount(String column, UserDto userregisterDto, Connection conn)throws SQLException {
        String sql = "select COUNT(*) from users where ? =?";
        PreparedStatement pstmt =conn.prepareStatement(sql);
        pstmt.setString(1, column);
        if(column.equals("phoneNumber")) {
            pstmt.setString(2, userregisterDto.phoneNumber);
        }else if(column.equals("email")) {
            pstmt.setString(2, userregisterDto.email);
        }else if(column.equals("nickname")) {
            pstmt.setString(2, userregisterDto.nickname);
        }
        ResultSet rs = pstmt.executeQuery();
        int count = 0;
        while (rs.next()) {
            count++;
        }
        System.out.println(count);
        if(count==1) {
            return true;
        }
        return false;
    }

    public static void updatePlayerStats(Player player, Connection conn)throws SQLException {
        String sql = "update users set score =score+?,wins =wins+?,session=session+1 where nickname =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        if (player.getWin()==0){
            pstmt.setInt(1, 15);
            pstmt.setInt(2, 1);
        }else {
            pstmt.setInt(1, 0);
            pstmt.setInt(2, 0);
        }
        pstmt.setString(3, player.getNickname());
        pstmt.executeUpdate();
        pstmt.close();
        Jdbc.returnConnection(conn);
    }
}
