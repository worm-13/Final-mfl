package com.mfl.model;

import jakarta.websocket.Session;

public class Player {
    private String nickname;
    private int score;
    private Session session;
    private int win=0;
    public Player(String nickname, int score, Session session) {
        this.nickname = nickname;
        this.score = score;
        this.session = session;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }
}
