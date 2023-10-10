package com.example.demo.vo;

import com.example.demo.pojo.TGoods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName goodsvo
 * @Description TODO
 * @Date 2023/10/5 12:39
 */
@Data
public class goodsvo extends TGoods {
    private BigDecimal seckillPrice;

    private Integer stockCount;


    private Date startDate;
    private Date endDate;
}
