package com.mfl.model;

public class HistoryRecord {
    private String black;
    private String white;
    private String border;
    private String winner;
    private String time;

    public HistoryRecord(String black, String white, String border, String winner, String time) {
        this.black = black;
        this.white = white;
        this.border = border;
        this.winner = winner;
        this.time = time;
    }

    public String getBlack() {
        return black;
    }

    public String getWhite() {
        return white;
    }

    public String getBorder() {
        return border;
    }

    public String getWinner() {
        return winner;
    }

    public String getTime() {
        return time;
    }
}
