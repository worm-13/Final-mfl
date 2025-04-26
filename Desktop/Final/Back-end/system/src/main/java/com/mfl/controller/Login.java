package com.mfl.controller;

import com.mfl.dto.JsonDto;
import com.mfl.dto.UserDto;
import com.mfl.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // 设置响应内容类型和编码
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 允许跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*"); // 指定允许的前端域名
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            UserService userService = new UserService();
            PrintWriter out = response.getWriter();
            UserDto dto = userService.loginUser(request, response);

            if(dto!=null){
                String data=JsonDto.responseLoginJson(dto,"登录成功",200);
                out.print(data);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }else {
                response.setStatus(401);
                out.print("{}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
