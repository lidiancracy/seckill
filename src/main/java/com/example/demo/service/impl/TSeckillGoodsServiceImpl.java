package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.pojo.TSeckillGoods;
import com.example.demo.service.TSeckillGoodsService;
import com.example.demo.mapper.TSeckillGoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author 83799
* @description 针对表【t_seckill_goods(秒杀商品表)】的数据库操作Service实现
* @createDate 2023-10-05 12:03:10
*/
@Service
public class TSeckillGoodsServiceImpl extends ServiceImpl<TSeckillGoodsMapper, TSeckillGoods>
    implements TSeckillGoodsService{

}




