package com.mfl.websocket;


import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.Collections;

public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        if (clazz == MatchingGame.class) {
            return (T) new MatchingGame();
        }
        return super.getEndpointInstance(clazz);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        response.getHeaders().put("Access-Control-Allow-Origin", Collections.singletonList("*"));
        response.getHeaders().put("Access-Control-Allow-Methods", Collections.singletonList("GET, POST, PUT, DELETE, OPTIONS"));
        response.getHeaders().put("Access-Control-Allow-Headers", Collections.singletonList("Content-Type"));
    }
}
