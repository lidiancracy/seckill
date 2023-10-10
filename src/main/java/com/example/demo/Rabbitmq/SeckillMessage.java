package com.example.demo.Rabbitmq;

import com.example.demo.pojo.TUser;
import com.example.demo.vo.goodsvo;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SeckillMessage
 * @Description TODO
 * @Date 2023/10/9 17:46
 */
@Data
public class SeckillMessage implements Serializable {
    private TUser user;
    private goodsvo goods;
    private String taskId;
}

