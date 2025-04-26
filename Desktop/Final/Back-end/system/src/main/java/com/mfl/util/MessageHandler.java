package com.mfl.util;

import com.mfl.model.Room;
import org.json.JSONObject;

public class MessageHandler {
    public static JSONObject createMoveResponse(int row, int col, String player) {
        JSONObject response = new JSONObject();
        response.put("type", "MOVE_CONFIRM");
        response.put("row", row);
        response.put("col", col);
        response.put("player", player);
        return response;
    }

    public static JSONObject createGameResultResponse(String winner) {
        JSONObject response = new JSONObject();
        response.put("type", "GAME_OVER");
        response.put("winner", winner);
        return response;
    }

    public static void sendToAllInRoom(Room room, String message) {


            if(room.getPlayer1().getSession().isOpen()) {
                System.out.println(11111);
                room.getPlayer1().getSession().getAsyncRemote().sendText(message);
            }
            if(room.getPlayer2().getSession().isOpen()) {
                System.out.println(2222);
                room.getPlayer2().getSession().getAsyncRemote().sendText(message);
            }

    }
}
