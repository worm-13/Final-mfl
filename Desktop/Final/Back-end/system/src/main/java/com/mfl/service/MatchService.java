package com.mfl.service;

import com.mfl.model.Player;
import org.json.JSONObject;

public class MatchService {
    public MatchService() {}

    public void sendOpponentMessage(Player player1, Player player2,int roomId) {
        // 构造包含对手信息的消息
        JSONObject messageForPlayer1 = new JSONObject()
                .put("type", "OPPONENT_INFO")
                .put("data", new JSONObject()
                        .put("nickname", player2.getNickname())
                        .put("score", player2.getScore())
                        .put("roomId", roomId)
                        .put("role","黑棋")
                );

        JSONObject messageForPlayer2 = new JSONObject()
                .put("type", "OPPONENT_INFO")
                .put("data", new JSONObject()
                        .put("nickname", player1.getNickname())
                        .put("score", player1.getScore())
                        .put("roomId",roomId)
                        .put("role","白棋")
                );


        // 发送消息给两位玩家
        player1.getSession().getAsyncRemote().sendText(messageForPlayer1.toString());
        player2.getSession().getAsyncRemote().sendText(messageForPlayer2.toString());
        System.out.println("已发送消息");
    }
}
