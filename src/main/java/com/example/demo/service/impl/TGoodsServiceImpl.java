package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.pojo.TGoods;
import com.example.demo.service.TGoodsService;
import com.example.demo.mapper.TGoodsMapper;
import com.example.demo.vo.goodsvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 83799
* @description 针对表【t_goods(商品表)】的数据库操作Service实现
* @createDate 2023-10-05 12:03:10
*/
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods>
    implements TGoodsService{
    @Autowired
    TGoodsMapper tGoodsMapper;
    @Override
    public List<goodsvo> findgoodsvo() {

        return tGoodsMapper.findvos();
    }

    @Override
    public goodsvo findGoodsVoById(Long goodsId) {
        return tGoodsMapper.findvobyid(goodsId);
    }
}




