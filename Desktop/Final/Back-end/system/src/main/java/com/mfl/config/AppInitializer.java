package com.mfl.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener
public class AppInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Jdbc.list(); // 初始化连接池
            System.out.println("连接池已初始化！");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("连接池初始化失败！");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 可以在这里释放资源，如关闭所有连接
        System.out.println("应用已停止，释放资源...");
    }
}
