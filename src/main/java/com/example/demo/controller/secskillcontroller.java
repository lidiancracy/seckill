package com.example.demo.controller;

import com.example.demo.interception.UserContext;
import com.example.demo.pojo.TOrder;
import com.example.demo.pojo.TUser;
import com.example.demo.result.CodeMsg;
import com.example.demo.service.TGoodsService;
import com.example.demo.service.TOrderService;
import com.example.demo.vo.goodsvo;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName secskillcontroller
 * @Description TODO
 * @Date 2023/10/5 14:07
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
/**
 * 秒杀接口 消息队列前 692
 *
 */
public class secskillcontroller {
    @Autowired
    private TGoodsService tGoodsService; // 假设你有这个服务处理商品逻辑

    @Autowired
    private TOrderService tOrderService; // 假设你有这个服务处理订单逻辑

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    public String doSeckill(Long goodsId, Model model) {

        TUser user = UserContext.getUser();
        goodsvo goods = tGoodsService.findGoodsVoById(goodsId);


        // 预减库存
//        String lockKey = "seckilling:goods:" + goodsId;
//        Boolean isLocked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 5, TimeUnit.SECONDS); // 尝试获取锁，最多锁定5秒
//
//        if (!isLocked) {
//            // 不能获取锁，说明有其他请求正在处理，可以直接返回或重试
//            model.addAttribute("errmsg", "秒杀太火爆，请稍后再试");
//            return "seckill_fail";
//        }
//
//        Integer currentStock = (Integer) redisTemplate.opsForValue().get("seckillinit:goods:" + goodsId);
//
//        if (currentStock <= 0) {
//            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
//            redisTemplate.delete(lockKey); // 释放锁
//            return "seckill_fail";
//        } else {
//            redisTemplate.opsForValue().decrement("seckillinit:goods:" + goodsId);
//            redisTemplate.delete(lockKey); // 释放锁
//            // 之后的逻辑...
//        }

        Integer currentStock = (Integer) redisTemplate.opsForValue().get("seckillinit:goods:" + goodsId);

        if (currentStock <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
            return "seckill_fail";
        } else {
            redisTemplate.opsForValue().decrement("seckillinit:goods:" + goodsId);
            // 之后的逻辑...
        }

        if (goods == null || goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
            return "seckill_fail";
        }

        // 检查是否已经秒杀过了
//        if (tOrderService.checkRepeatSeckill(user.getId(), goodsId)) {
//            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "seckill_fail";
//        }

        // 执行秒杀逻辑（减库存，生成订单等）
        TOrder od = tOrderService.executeSeckill(user, goods);

        if (od == null) {
            model.addAttribute("errmsg", "秒杀失败");
            return "seckill_fail";
        }

        model.addAttribute("orderInfo", od);
        model.addAttribute("goods",goods);
        System.out.println("order信息"+od.toString());
        System.out.println("order信息"+goods.toString());
        return "order_detail";
    }


    @PostConstruct  // 或在其他初始化方法中
    public void loadGoodsStockToRedis() {
        List<goodsvo> goodsList = tGoodsService.findgoodsvo();
        for (goodsvo goods : goodsList) {
            redisTemplate.opsForValue().set("seckillinit:goods:" + goods.getId(), goods.getStockCount());
        }
    }


}
