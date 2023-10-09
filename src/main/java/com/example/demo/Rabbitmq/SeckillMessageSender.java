package com.example.demo.Rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SeckillMessageSender
 * @Description TODO
 * @Date 2023/10/9 17:46
 */
@Service
@Slf4j
public class SeckillMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(SeckillMessage message) {
        log.info("发送消息");
        rabbitTemplate.convertAndSend("seckillExchange", "seckillRoutingKey", message);
    }
}
