package com.mfl.dao;

import com.mfl.config.Jdbc;
import com.mfl.model.Room;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChessDao {
    public static void chessHistory(Room room,String time, Connection conn) throws SQLException {
        String sql="insert into chessHistory (black,white,border,winner,time) values(?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        JSONArray jsonArray = new JSONArray();
        for (int[] row : room.getBorder()) {
            JSONArray rowArray = new JSONArray();
            for (int num : row) {
                rowArray.put(num);
            }
            jsonArray.put(rowArray);
        }
        pstmt.setString(1,room.getPlayer1().getNickname());
        pstmt.setString(2,room.getPlayer2().getNickname());
        pstmt.setString(3,jsonArray.toString());
        if (room.getPlayer1().getWin()==0){
            pstmt.setString(4,room.getPlayer2().getNickname());
        }else {
            pstmt.setString(4,room.getPlayer1().getNickname());
        }
        pstmt.setString(5,time);
        pstmt.executeUpdate();
        pstmt.close();
        Jdbc.returnConnection(conn);
    }
}
