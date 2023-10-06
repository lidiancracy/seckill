package com.example.demo.service;

import com.example.demo.pojo.TGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.vo.goodsvo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 83799
* @description 针对表【t_goods(商品表)】的数据库操作Service
* @createDate 2023-10-05 12:03:10
*/
public interface TGoodsService extends IService<TGoods> {

    List<goodsvo> findgoodsvo();

    goodsvo findGoodsVoById(Long goodsId);
}
