package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.pojo.TSeckillOrder;
import com.example.demo.service.TSeckillOrderService;
import com.example.demo.mapper.TSeckillOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author 83799
* @description 针对表【t_seckill_order(秒杀订单表)】的数据库操作Service实现
* @createDate 2023-10-05 12:03:10
*/
@Service
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder>
    implements TSeckillOrderService{

}




