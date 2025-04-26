package com.mfl.model;

public class User {
    private String nickname;            // 昵称
    private String phoneNumber;         // 电话号码
    private String email;               // 邮箱
    private String password;            //密码
    private int session;                // 场次
    private int wins;                   // 胜场
    private int score;                  // 段位分数，可以用于判断加分

    public User(String nickname, String phoneNumber, String email, int session, int wins,int score) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.session = session;
        this.wins = wins;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
