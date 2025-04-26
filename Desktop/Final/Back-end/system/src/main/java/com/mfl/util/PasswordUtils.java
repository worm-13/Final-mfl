package com.mfl.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

        // 密码加密
        public static String hashPassword(String plainPassword) {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        }

        // 验证登录密码或者更改密码验证
        public static boolean checkPassword(String plainPassword, String hashedPassword) {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        }
}
