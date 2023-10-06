package com.example.demo.mapper;

import com.example.demo.pojo.TGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.vo.goodsvo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 83799
* @description 针对表【t_goods(商品表)】的数据库操作Mapper
* @createDate 2023-10-05 12:03:10
* @Entity com.example.demo.pojo.TGoods
*/
public interface TGoodsMapper extends BaseMapper<TGoods> {
    @Select("SELECT g.*, sg.seckill_price, sg.stock_count, sg.start_date, sg.end_date " +
            "FROM t_goods g " +
            "LEFT JOIN t_seckill_goods sg ON g.id = sg.goods_id")
    List<goodsvo> findvos();
    @Select("SELECT " +
            "g.id, g.goods_name, g.goods_title, g.goods_img, g.goods_detail, g.goods_price, g.goods_stock, " +
            "sg.seckill_price AS seckillPrice, sg.stock_count AS stockCount, sg.start_date AS startDate, sg.end_date AS endDate " +
            "FROM t_goods g " +
            "LEFT JOIN t_seckill_goods sg ON g.id = sg.goods_id " +
            "WHERE g.id = #{goodsId}")
    goodsvo findvobyid(Long goodsId);
    @Update("UPDATE t_seckill_goods SET stock_count = stock_count - 1 WHERE goods_id = #{id} AND stock_count > 0")
    int reduceStock(Long id);


}




