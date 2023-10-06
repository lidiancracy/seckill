package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.TUser;
import com.example.demo.result.Result;
import com.example.demo.service.TUserService;
import com.example.demo.vo.loginvo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @ClassName logincontroller
 * @Description TODO
 * @Date 2023/10/3 15:20
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class logincontroller {
    @Autowired
    TUserService userService;

    @RequestMapping("/lg")
    public String login() {
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public Result<String> dologin(@Valid loginvo loginvo, HttpServletResponse response) {
//        log.info(loginvo.toString());
        return userService.loginservice(loginvo, response);

    }

/**
 * logout
 */
//    public void logout(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("token".equals(cookie.getName())) {
//                    String token = cookie.getValue();
//
//                    // Get the user data from Redis using the token
//                    String userJson = redisTemplate.opsForValue().get("user:" + token);
//                    if (userJson != null) {
//                        TUser user = JSON.parseObject(userJson, TUser.class);
//
//                        // Extract the mobile from the user data
//                        String mobile = user.getMobile();
//
//                        // Delete the two keys from Redis
//                        redisTemplate.delete("user:" + token);
//                        redisTemplate.delete("user:id:" + mobile);
//                    }
//
//                    // Clear the cookie (optional but recommended)
//                    cookie.setMaxAge(0);
//                    cookie.setValue(null);
//                    cookie.setPath("/");
//                }
//            }
//        }
//    }

}
