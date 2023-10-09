package com.example.demo;

import cn.hutool.db.nosql.redis.RedisDS;
import com.example.demo.pojo.TUser;
import com.example.demo.result.Result;
import com.example.demo.service.TUserService;
import com.example.demo.vo.loginvo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletResponse;


import javax.servlet.http.Cookie;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.*;

import static cn.hutool.core.util.IdUtil.randomUUID;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String token = randomUUID();
        System.out.println(token);

        // 使用Hutool的Redis工具类存储token
        redisTemplate.opsForValue().set("name", "John Doe");
        redisTemplate.opsForValue().set("age", "30");


    }

    @Autowired
    TUserService tUserService;

    @Test
    public void insertUsers() {
        for (int i = 0; i < 1000; i++) {
            TUser user = new TUser();
            user.setId(new BigInteger(generatePhoneNumber()));
            user.setNickname("User" + i);
            user.setHead("http://example.com/avatar/" + user.getId() + ".jpg");
            user.setRegisterDate(new Date());
            user.setLastLoginDate(new Date());
            user.setLoginCount(1);

            // 使用你的服务来插入这个用户到数据库中
            tUserService.save(user);
        }
    }

    private String generatePhoneNumber() {
        Random random = new Random();
        int secondDigit = random.nextInt(7) + 3;  // generates a number between 3 and 9
        String remainingDigits = String.format("%08d", random.nextInt(100_000_000));  // generates a random 8-digit number
        return "1" + secondDigit + remainingDigits;
    }


    @Autowired
    TUserService userService;

    @Test
    public void simulateLoginAndGetTokens() throws Exception {
        List<TUser> users = userService.list();  // 假设你的TUserService有这个方法来获取所有用户
        Map<String, String> tokens = new HashMap<>();

        for (TUser user : users) {
            loginvo vo = new loginvo();
            vo.setMobile(user.getId().toString());  // ID即手机号
            vo.setPassword("123456");  // 固定密码

            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
            Result<String> result = userService.loginservice(vo, mockResponse);

            String token = null;
            for (Cookie cookie : mockResponse.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }

            if (token != null) {
                tokens.put(vo.getMobile(), token);
            }
        }

        // 将手机号和token保存到txt文件
        try (PrintWriter out = new PrintWriter("tokens.txt")) {
            for (Map.Entry<String, String> entry : tokens.entrySet()) {
                out.println(entry.getKey() + "\t" + entry.getValue());
            }
        }
    }


}



