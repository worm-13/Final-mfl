package com.mfl.model;

public class Room {
    private int id;
    private Player player1;
    private Player player2;
    private int[][] border=new int[15][15];
    private char currentTurn;

    public Room(int id, Player player1, Player player2, int[][] border) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.border = border;
        this.currentTurn = 'B';
    }

    public static Room createRoom(int id, Player player1, Player player2, int[][] border) {
        return new Room(id, player1, player2, border);
    }




    public char getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(char currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int[][] getBorder() {
        return border;
    }

    public void setBorder(int[][] border) {
        this.border = border;
    }
}
