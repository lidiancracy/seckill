package com.example.demo.Rabbitmq;

import com.example.demo.exception.GoodsSoldOutException;
import com.example.demo.pojo.TOrder;
import com.example.demo.service.TOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @ClassName RabbitMqListener
 * @Description TODO
 * @Date 2023/10/9 17:47
 */
@Service
@Slf4j
public class RabbitMqListener {

    @Autowired
    TOrderService tOrderService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = "seckillQueue")
    public void receiveSeckillMessage(SeckillMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("接收到消息队列的数据: {}", message);
        String taskId = message.getTaskId(); // 直接从消息中获取taskId

        try {
            tOrderService.executeSeckill(message.getUser(), message.getGoods(), taskId);
            log.info("成功处理用户 {} 的秒杀请求", message.getUser().getId());
            channel.basicAck(tag, false);  // 手动确认消息

        } catch (GoodsSoldOutException e) {
            log.info(e.getMessage());
            redisTemplate.opsForValue().set("seckill:task:" + taskId, "sold_out");
            channel.basicAck(tag, false);  // 商品已售罄，确认消息并从队列中移除

        } catch (Exception e) {
            log.error("处理消息时出现异常: ", e);
            redisTemplate.opsForValue().set("seckill:task:" + taskId, "failed");
            channel.basicNack(tag, false, true);  // 其他异常，拒绝消息并请求RabbitMQ重新入队
        }
    }


}

