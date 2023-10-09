package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.TGoodsMapper;
import com.example.demo.mapper.TSeckillOrderMapper;
import com.example.demo.pojo.TOrder;
import com.example.demo.pojo.TSeckillOrder;
import com.example.demo.pojo.TUser;
import com.example.demo.result.CodeMsg;
import com.example.demo.service.TOrderService;
import com.example.demo.mapper.TOrderMapper;
import com.example.demo.vo.goodsvo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
* @author 83799
* @description 针对表【t_order(订单表)】的数据库操作Service实现
* @createDate 2023-10-05 12:03:10
*/
@Service
@Slf4j
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder>
    implements TOrderService{
    @Autowired
    TOrderMapper tOrderMapper;
    @Autowired
    TGoodsMapper tGoodsMapper;
    @Autowired
    TSeckillOrderMapper tSeckillOrderMapper;
    @Override
    public TOrder executeSeckill(TUser user, goodsvo goods) {
        // 1. 减库存
        int result = tGoodsMapper.reduceStock(goods.getId());
        if (result < 1) {
            throw new CustomException(CodeMsg.STOCK_MIAOSHA);
        }

        // 2. 创建订单
        TOrder order = new TOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
         // 假设用户有一个默认地址
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1); // 假设每次只能秒杀1个商品
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1); // 假设是pc渠道
        order.setStatus(0);  // 新建未支付状态
        order.setCreateDate(new Date());

        // 3. 保存订单到数据库
        tOrderMapper.insert(order);

        TSeckillOrder seckillOrder = new TSeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());

        // 5. 保存秒杀订单到数据库
        tSeckillOrderMapper.insert(seckillOrder);

        return order;
    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean checkRepeatSeckill(BigInteger userId, Long goodsId) {
        // 使用用户ID和商品ID生成唯一的键
        String redisKey = "seckill:" + userId + ":" + goodsId;

        // 从 Redis 中检查是否有记录
        Boolean hasRecord = (Boolean) redisTemplate.opsForValue().get(redisKey);

        log.info("redis中有没有" + hasRecord);

        if (hasRecord != null && hasRecord) {
            return true;
        } else {
            // 从数据库中查询
            TOrder order = tOrderMapper.findByUserIdAndGoodsId(userId, goodsId);
            if (order != null) {
                // 将结果存入 Redis，同时设置一个合理的过期时间，例如一天
                redisTemplate.opsForValue().set(redisKey, true, 3, TimeUnit.HOURS);
                return true;
            } else {
                return false;
            }
        }
    }


}




