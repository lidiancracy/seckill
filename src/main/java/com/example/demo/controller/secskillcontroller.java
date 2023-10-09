package com.example.demo.controller;

import com.example.demo.Rabbitmq.SeckillMessage;
import com.example.demo.Rabbitmq.SeckillMessageSender;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    @Autowired
    SeckillMessageSender seckillMessageSender;
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    public String doSeckill(Long goodsId, Model model) {
        TUser user = UserContext.getUser();
        goodsvo goods = tGoodsService.findGoodsVoById(goodsId);
        // 检查用户是否已经参与过秒杀
        if (tOrderService.checkRepeatSeckill(user.getId(), goodsId)) {
            model.addAttribute("errmsg", "您已经参与过此次秒杀，请勿重复操作。");
            return "seckill_fail";
        }

        // 获取并预减Redis库存
        Integer currentStock = (Integer) redisTemplate.opsForValue().get("seckillinit:goods:" + goodsId);

        if (currentStock == null || currentStock <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
            return "seckill_fail";
        }

        // 如果库存充足则预减库存
        redisTemplate.opsForValue().decrement("seckillinit:goods:" + goodsId);

        if (goods == null || goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
            return "seckill_fail";
        }

        // 发送消息至RabbitMQ进行异步处理
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoods(goods);
        seckillMessageSender.sendSeckillMessage(message);

        // 创建唯一任务ID并将其与初始状态关联
        String taskId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("seckill:task:" + taskId, "waiting");

// 将任务ID添加到模型以供前端使用
        model.addAttribute("taskId", taskId);

        // 返回用户等待页面
        return "queueing";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> seckillStatus(@RequestParam String taskId) {
        String status = (String) redisTemplate.opsForValue().get("seckill:task:" + taskId);
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        return result;
    }

    @PostConstruct  // 或在其他初始化方法中
    public void loadGoodsStockToRedis() {
        List<goodsvo> goodsList = tGoodsService.findgoodsvo();
        for (goodsvo goods : goodsList) {
            redisTemplate.opsForValue().set("seckillinit:goods:" + goods.getId(), goods.getStockCount());
        }
    }


}
