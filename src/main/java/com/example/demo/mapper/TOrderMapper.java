package com.example.demo.mapper;

import com.example.demo.pojo.TOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

/**
* @author 83799
* @description 针对表【t_order(订单表)】的数据库操作Mapper
* @createDate 2023-10-05 12:03:10
* @Entity com.example.demo.pojo.TOrder
*/
public interface TOrderMapper extends BaseMapper<TOrder> {


    @Select("SELECT * FROM t_order WHERE user_id = #{userId} AND goods_id = #{goodsId}")
    TOrder findByUserIdAndGoodsId(@Param("userId") BigInteger userId, @Param("goodsId") Long goodsId);
}




