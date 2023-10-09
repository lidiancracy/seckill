package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class LoginSimulation {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String LOGIN_URL = "http://localhost:8080/login/doLogin";

    @Test
    public void simulateLoginAndSaveTokens() throws Exception {
        List<String> mobiles = fetchMobilesFromDB();

        try (PrintWriter writer = new PrintWriter(new FileWriter("tokens.csv"))) {
            for (String mobile : mobiles) {
                String token = loginAndGetToken(mobile, "d3b1294a61a07da9b49b6e22b2cbd7f9");

                if (token != null) {
                    writer.println(mobile + "," + token);
                }
            }
        }
    }

    private List<String> fetchMobilesFromDB() throws Exception {
        List<String> mobiles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id FROM t_user ");
            while (rs.next()) {
                mobiles.add(rs.getString("id"));
//                System.out.println(rs.getString("id"));
            }
        }
        return mobiles;
    }

    private String loginAndGetToken(String mobile, String password) throws Exception {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String body = "mobile=" + mobile + "&password=" + password;
        conn.getOutputStream().write(body.getBytes());

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String cookieHeader = conn.getHeaderField("Set-Cookie");
            if (cookieHeader != null) {
                String[] cookies = cookieHeader.split(";");
                for (String cookie : cookies) {
                    if (cookie.trim().startsWith("token=")) {
                        return cookie.trim().substring("token=".length());
                    }
                }
            }
        }
        return null;
    }
}
