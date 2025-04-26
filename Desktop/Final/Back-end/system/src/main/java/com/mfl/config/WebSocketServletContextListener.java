package com.mfl.config;

import com.mfl.websocket.MatchingGame;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.websocket.server.ServerContainer;
import jakarta.websocket.server.ServerEndpointConfig;


public class WebSocketServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServerContainer container = (ServerContainer) sce.getServletContext().getAttribute("javax.websocket.server.ServerContainer");
        try {
            ServerEndpointConfig gameEndpointConfig = ServerEndpointConfig.Builder.create(MatchingGame.class, "/game").build();
            container.addEndpoint(gameEndpointConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup if needed
    }
}