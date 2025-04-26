package com.mfl.websocket;

import com.mfl.model.Player;
import com.mfl.model.Room;
import com.mfl.service.MatchService;
import com.mfl.util.RandomIdUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import java.util.concurrent.*;


@ServerEndpoint("/join-game")
public class MatchingGame {
    final static MatchService matchService = new MatchService();

    // 待处理的请求玩家
    private static final ConcurrentHashMap<Session, Player> pendingPlayers = new ConcurrentHashMap<>();

    // 就绪玩家队列
    private static final ConcurrentLinkedQueue<Player> waitingPlayers = new ConcurrentLinkedQueue<>();

    // 房间队列
    public static final ConcurrentHashMap<Integer, Room> activeRoom = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session) {
        pendingPlayers.put(session, new Player("", 0, session));
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject json = new JSONObject(message);
            String type = json.getString("type");
            switch (type) {
                // 提交自己信息
                case "JOIN_QUEUE": {
                    Player player = pendingPlayers.get(session);
                    if (player != null) {
                        player.setNickname(json.getString("nickname"));
                        player.setScore(json.getInt("score"));
                        System.out.println(player.getNickname());
                        waitingPlayers.add(player);
                        pendingPlayers.remove(session); // 移出暂存
                        session.getAsyncRemote().sendText("{\"type\":\"JOIN_SUCCESS\"}");
                    }
                    break;
                }
                // 获取对手信息，创建房间
                case "PLAYER_INFO":{
                    if(waitingPlayers.size()>=2){

                        int[][] border = new int[15][15];
                        int randomId;
                        do {
                            randomId = RandomIdUtils.generateRandomId();
                        } while (activeRoom.containsKey(randomId));
                        Player p1=waitingPlayers.poll();
                        Player p2=waitingPlayers.poll();
                        Room room = Room.createRoom(randomId,p1,p2,border);
                        matchService.sendOpponentMessage(p1,p2,randomId);
                        activeRoom.put(room.getId(), room);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClose
    public void onClose(Session session) {
        pendingPlayers.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private void sendError(Session session, String message) {

    }


}