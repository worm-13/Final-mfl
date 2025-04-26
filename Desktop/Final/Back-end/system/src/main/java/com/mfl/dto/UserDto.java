package com.mfl.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;

public class UserDto {
    public String nickname;
    public String phoneNumber;
    public String email;
    public String password;
    public boolean isPhone;
    public int session;
    public int wins;
    public int score;

    // 通过手机注册
    public UserDto getUser(HttpServletRequest req){
        try {
            // 1. 读取请求体中的 JSON 数据
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonStr = sb.toString(); // 读取完毕转化为String

            // 2. 解析 JSON
            JSONObject json = new JSONObject(jsonStr);
            UserDto newUser = new UserDto();
            newUser.nickname=json.getString("nickname");
            newUser.password=json.getString("password");
            newUser.phoneNumber=json.getString("phone");
            newUser.email=json.getString("email");
            return newUser;

        } catch (Exception e) {
            // 错误处理
            e.printStackTrace();

        }
        return null;
    }

    // 登录
    public UserDto loginUser(HttpServletRequest req) {
        try {
            // 1. 读取请求体中的 JSON 数据
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonStr = sb.toString(); // 读取完毕转化为String

            // 2. 解析 JSON
            JSONObject json = new JSONObject(jsonStr);
            UserDto newUser = new UserDto();
            newUser.password=json.getString("password");
            newUser.phoneNumber=json.getString("phone");
            newUser.email=json.getString("email");
            newUser.isPhone=json.getBoolean("isPhoneLogin");
            System.out.println(newUser.email);
            System.out.println(newUser.phoneNumber);
            System.out.println(newUser.isPhone);
            return newUser;
        } catch (Exception e) {
            // 错误处理
            e.printStackTrace();
            System.out.println("解析前端请求数据错误");
        }
        return null;
    }
}
