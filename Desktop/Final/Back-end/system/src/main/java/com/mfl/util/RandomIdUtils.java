package com.mfl.util;

import java.util.Random;

public class RandomIdUtils {
    public static int generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000) + 1; // 生成1到1000之间的随机整数
    }
}
