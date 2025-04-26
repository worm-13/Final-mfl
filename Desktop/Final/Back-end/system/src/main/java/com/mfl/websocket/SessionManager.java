package com.mfl.websocket;


import jakarta.websocket.Session;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class SessionManager {
    private static final Map<Session, String> sessions =
            Collections.synchronizedMap(new WeakHashMap<>());

    public static void addSession(Session session) {
        sessions.put(session, "active");
    }

    public static void removeSession(Session session) {
        sessions.remove(session);
    }

    public static boolean contains(Session session) {
        return sessions.containsKey(session);
    }
}
