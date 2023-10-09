package com.example.demo;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName genuser
 * @Description TODO
 * @Date 2023/10/7 18:33
 */


public class genuser {

    private static final String URL = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Test
    public void createUsers() throws Exception {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO t_user (id, nickname, head, register_date, last_login_date, login_count) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < 500; i++) {
                    String phoneNumber = generatePhoneNumber();
                    statement.setLong(1, Long.parseLong(phoneNumber));
                    statement.setString(2, "User" + phoneNumber);
                    statement.setString(3, "http://example.com/avatar/" + phoneNumber + ".jpg");
                    statement.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
                    statement.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
                    statement.setInt(6, 1);
                    statement.executeUpdate();
                }
            }
        }
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int secondDigit = random.nextInt(8) + 3;  // generates a number between 3 and 9
        String remainingDigits = String.format("%09d", random.nextInt(1_000_000_000));  // generates a random 9-digit number
        return "1" + secondDigit + remainingDigits;
    }
}
