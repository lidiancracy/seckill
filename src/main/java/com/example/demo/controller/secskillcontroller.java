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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @ClassName secskillcontroller
 * @Description TODO
 * @Date 2023/10/5 14:07
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
public class secskillcontroller {
    @Autowired
    private TGoodsService tGoodsService; // 假设你有这个服务处理商品逻辑

    @Autowired
    private TOrderService tOrderService; // 假设你有这个服务处理订单逻辑

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    public String doSeckill(Long goodsId, Model model) {
        log.info("goodid为" + goodsId);
        TUser user = UserContext.getUser();
        goodsvo goods = tGoodsService.findGoodsVoById(goodsId);

        if (goods == null || goods.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.STOCK_MIAOSHA.getMsg());
            return "seckill_fail";
        }

        // 检查是否已经秒杀过了
        if (tOrderService.checkRepeatSeckill(user.getId(), goodsId)) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "seckill_fail";
        }

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

}
