package com.example.demo;

import cn.hutool.db.nosql.redis.RedisDS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


import java.time.Duration;

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

}
