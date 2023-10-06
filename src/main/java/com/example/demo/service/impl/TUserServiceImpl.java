package com.example.demo.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.exception.CustomException;
import com.example.demo.interception.UserContext;
import com.example.demo.mapper.TUserMapper;
import com.example.demo.pojo.TUser;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.TUserService;
import com.example.demo.utils.md5util;

import com.example.demo.vo.loginvo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static cn.hutool.core.util.IdUtil.randomUUID;


/**
 * 用户表(TUser)表服务实现类
 *
 * @author makejava
 * @since 2023-10-03 15:13:35
 */
@Service("tUserService")
@Slf4j
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {
    @Autowired
    TUserMapper tUserMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Result<String> loginservice(loginvo loginvo, HttpServletResponse response) {
        String mobile = loginvo.getMobile();
        String password = loginvo.getPassword();
//        log.info(mobile+password);
        // 2. 从数据库中查询用户
        TUser user = tUserMapper.findByMobile(mobile);
        if (user == null) {
            throw new CustomException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 3. 验证密码是否匹配 (此处简化为直接比较，实际上通常使用加密后的密码进行比较)
        if (!user.getPassword().equals(md5util.formPassToDBPass(loginvo.getPassword(),user.getSalt()))) {
            throw new CustomException(CodeMsg.PASSWORD_ERROR);
        }
        UserContext.setUser(user);
        // 生成token并存入Redis，设置有效期为30分钟
        String token = randomUUID().toString();
        // 使用RedisTemplate存储token
        String userJson = JSON.toJSONString(user);
        redisTemplate.opsForValue().set("user:" + token, userJson, 30, TimeUnit.MINUTES);

        // 存储token到cookie中
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60);  // 设置有效期为30分钟
        response.addCookie(cookie);

        // 4. 返回登录成功
        return Result.success("登录成功");
    }



}
