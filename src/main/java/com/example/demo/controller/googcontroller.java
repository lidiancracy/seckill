package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.interception.UserContext;
import com.example.demo.pojo.TUser;
import com.example.demo.service.TGoodsService;
import com.example.demo.vo.goodsvo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName googcontroller
 * @Description TODO
 * @Date 2023/10/4 13:59
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class googcontroller {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TGoodsService tGoodsService;

    @RequestMapping("tolist")
    public String tolist(Model model) {
        TUser user = UserContext.getUser();
        model.addAttribute("user", user);
        List<goodsvo> findgoodsvo = tGoodsService.findgoodsvo();
        log.info(findgoodsvo.toString());
        model.addAttribute("goodsList",findgoodsvo);

        return "goodslist";
    }

    @GetMapping("/detail/{goodsId}")
    public String getGoodsDetail(@PathVariable Long goodsId, Model model, HttpSession session) {
        goodsvo goods = tGoodsService.findGoodsVoById(goodsId);
        log.info(goods.toString());
        if (goods == null) {
            return "redirect:/tolist";
        }

        // 添加商品到模型中
        model.addAttribute("goods", goods);

        // 计算秒杀状态
        long now = System.currentTimeMillis();
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();

        int miaoshaStatus = 0; // 0表示未开始, 1表示进行中, 2表示已结束
        long remainSeconds = 0; // 倒计时的秒数

        if (now < startAt) { // 秒杀未开始
            miaoshaStatus = 0;
            remainSeconds = (startAt - now) / 1000;
        } else if (now > endAt) { // 秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1; // 表示已结束，不需要倒计时
        } else { // 秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0; // 秒杀进行中, 不需要倒计时
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        // 获取当前登录的用户，并添加到模型中
        TUser user = UserContext.getUser();// 假设你在session中保存的用户键是"user"
        model.addAttribute("user", user);

        return "goods_detail";
    }






}
