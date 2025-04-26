package com.mfl.websocket;


import com.mfl.config.Jdbc;
import com.mfl.dao.ChessDao;
import com.mfl.dao.UserDao;
import com.mfl.model.Room;
import com.mfl.service.GameService;
import com.mfl.util.MessageHandler;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ServerEndpoint("/gaming")
public class GameWebSocket {

    @OnOpen
    public void onOpen(Session session) {


        System.out.println("New connection to gaming: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject json = new JSONObject(message);
            String type = json.getString("type");

            switch (type) {
                case "PLAYER_INFO":
                    int roomId = json.getInt("roomId");
                    String nickname = json.getString("nickname");
                    // 将新的连接与房间和玩家关联起来
                    Room room =MatchingGame.activeRoom.get(roomId);
                    if (room != null) {
                        if (room.getPlayer1().getNickname().equals(nickname)) {
                            room.getPlayer1().setSession(session);
                        } else if (room.getPlayer2().getNickname().equals(nickname)) {
                            room.getPlayer2().setSession(session);
                        }
                    }
                    break;
                case "MOVE":
                    Move(session, json);
                    break;
                default:
                    System.out.println("Unknown message type: " + type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Move(Session session, JSONObject json) throws Exception {
        int roomId = json.getInt("roomId");
        int row = json.getInt("row");
        int col = json.getInt("col");
        String player = json.getString("player");

        Room room = MatchingGame.activeRoom.get(roomId);
        System.out.println(player);
        System.out.println(room.getCurrentTurn());
        if (room != null) {
            // 检查是否是当前玩家的回合
            if ((player.equals("B") && room.getCurrentTurn() == 'B') || (player.equals("W") && room.getCurrentTurn() == 'W')) {
                // 调用游戏服务来处理移动
                boolean success = GameService.handleMove(room, row, col, player.equals("B") ? 1 : 2);

                if (success) {
                    System.out.println("落子成功");
                    // 广播移动信息给房间内的所有玩家
                    JSONObject response = MessageHandler.createMoveResponse(row, col, player);
                    MessageHandler.sendToAllInRoom(room, response.toString());

                    // 切换玩家
                    char nextPlayer = player.equals("B") ? 'W' : 'B';
                    room.setCurrentTurn(nextPlayer);

                    // 广播回合切换信息
                    JSONObject turnResponse = new JSONObject();
                    turnResponse.put("type", "TURN_CHANGE");
                    turnResponse.put("player", String.valueOf(nextPlayer));
                    MessageHandler.sendToAllInRoom(room, turnResponse.toString());

                    // 检查是否有人获胜
                    if (GameService.checkWin(room.getBorder())) {
                        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String time = LocalDateTime.now().format(formatter);
                        String winner = player.equals("B") ? room.getPlayer1().getNickname() : room.getPlayer2().getNickname();
                        if (winner.equals(room.getPlayer1().getNickname())) {
                            room.getPlayer1().setWin(1);
                        }else {
                            room.getPlayer2().setWin(1);
                        }
                        UserDao.updatePlayerStats(room.getPlayer1(), Jdbc.getConnection());
                        UserDao.updatePlayerStats(room.getPlayer2(),Jdbc.getConnection());
                        JSONObject resultResponse = MessageHandler.createGameResultResponse(winner);
                        MessageHandler.sendToAllInRoom(room, resultResponse.toString());

                        // 清理房间
                        MatchingGame.activeRoom.remove(roomId);

                        // 打印最终棋盘
                        printFinalBoard(room.getBorder());

                        // 插入对局记录
                        ChessDao.chessHistory(room,time,Jdbc.getConnection());
                    }
                }
            } else {
                System.out.println("Not your turn111111");
            }
        }else {
            System.out.println("房间不存在!!!");
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed in gaming: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // 打印最终棋盘
    private void printFinalBoard(int[][] border) {
        System.out.println("Final Board:");
        for (int[] row : border) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}