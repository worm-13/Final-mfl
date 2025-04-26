package com.mfl.controller;

import com.mfl.model.HistoryRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/history")
public class History extends HttpServlet {
    private String jdbcUrl;
    private String dbUser;
    private String dbPassword;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // 初始化响应内容类型
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<HistoryRecord> historyRecords = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String sql = "SELECT * FROM game_history WHERE black = ? OR white = ? ORDER BY time DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    HistoryRecord record = new HistoryRecord(
                            resultSet.getString("black"),
                            resultSet.getString("white"),
                            resultSet.getString("border"),
                            resultSet.getString("winner"),
                            resultSet.getString("time")
                    );
                    historyRecords.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal Server Error");
            return;
        }

        // 将结果转换为 JSON 并写回客户端
        PrintWriter out = response.getWriter();
        out.write(convertToJSON(historyRecords));
        out.flush();
    }

    private String convertToJSON(List<HistoryRecord> historyRecords) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < historyRecords.size(); i++) {
            HistoryRecord record = historyRecords.get(i);
            jsonBuilder.append(String.format(
                    "{\"black\":\"%s\", \"white\":\"%s\", \"border\":%s, \"winner\":\"%s\", \"time\":\"%s\"}",
                    record.getBlack(),
                    record.getWhite(),
                    record.getBorder(),
                    record.getWinner(),
                    record.getTime()
            ));
            if (i < historyRecords.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
