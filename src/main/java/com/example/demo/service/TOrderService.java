package com.example.demo.service;

import com.example.demo.pojo.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.TUser;
import com.example.demo.vo.goodsvo;

import java.math.BigInteger;

/**
* @author 83799
* @description 针对表【t_order(订单表)】的数据库操作Service
* @createDate 2023-10-05 12:03:10
*/
public interface TOrderService extends IService<TOrder> {

    TOrder executeSeckill(TUser user, goodsvo goods);

    boolean checkRepeatSeckill(BigInteger id, Long goodsId);
}
