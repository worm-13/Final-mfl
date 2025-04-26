package com.mfl.service;

import com.mfl.model.Room;

public class GameService {


    public static boolean handleMove(Room room, int row, int col, int player) {
        // 检查位置是否有效且为空
        if (row >= 0 && row < room.getBorder().length && col >= 0 && col < room.getBorder()[0].length && room.getBorder()[row][col] == 0) {
            room.getBorder()[row][col] = player;
            return true;
        }
        return false;
    }

    public static boolean checkWin(int[][] border) {
        // 实现五子棋胜利检测逻辑
        for (int i = 0; i < border.length; i++) {
            for (int j = 0; j < border[i].length; j++) {
                if (border[i][j] != 0) {
                    int piece = border[i][j];
                    // 检查水平方向
                    if (j <= border[i].length - 5) {
                        if (border[i][j] == piece && border[i][j + 1] == piece && border[i][j + 2] == piece && border[i][j + 3] == piece && border[i][j + 4] == piece) {
                            return true;
                        }
                    }
                    // 检查垂直方向
                    if (i <= border.length - 5) {
                        if (border[i][j] == piece && border[i + 1][j] == piece && border[i + 2][j] == piece && border[i + 3][j] == piece && border[i + 4][j] == piece) {
                            return true;
                        }
                    }
                    // 检查对角线方向（左上到右下）
                    if (i <= border.length - 5 && j <= border[i].length - 5) {
                        if (border[i][j] == piece && border[i + 1][j + 1] == piece && border[i + 2][j + 2] == piece && border[i + 3][j + 3] == piece && border[i + 4][j + 4] == piece) {
                            return true;
                        }
                    }
                    // 检查对角线方向（右上到左下）
                    if (i <= border.length - 5 && j >= 4) {
                        if (border[i][j] == piece && border[i + 1][j - 1] == piece && border[i + 2][j - 2] == piece && border[i + 3][j - 3] == piece && border[i + 4][j - 4] == piece) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
