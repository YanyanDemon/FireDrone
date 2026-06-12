package com.firedrone.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * 命令行工具：生成 BCrypt 密码哈希，用于手动插入数据库。
 * 在 IDEA 中右键运行 main 方法即可。
 */
public class BCryptHashGenerator {
    public static void main(String[] args) {
        String password = args.length > 0 ? args[0] : "admin123";
        int cost = 12;

        String hash = BCrypt.withDefaults().hashToString(cost, password.toCharArray());
        System.out.println("密码: " + password);
        System.out.println("哈希: " + hash);
    }
}

