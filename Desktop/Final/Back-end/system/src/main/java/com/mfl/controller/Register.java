package com.mfl.controller;

import com.mfl.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {
        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response){
            // 设置响应内容类型和编码
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // 允许跨域请求
            response.setHeader("Access-Control-Allow-Origin", "*"); // 指定允许的前端域名
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");

            UserService userService = new UserService();
            boolean check = userService.registerUser(request,response);
            if (check){
                response.setStatus(HttpServletResponse.SC_CREATED);
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        }

        @Override
        protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        }
}
