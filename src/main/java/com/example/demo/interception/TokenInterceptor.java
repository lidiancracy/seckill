package com.example.demo.interception;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.TUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TokenInterceptor
 * @Description TODO
 * @Date 2023/10/6 18:24
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();

                    // 验证Redis中是否包含该token
                    String userJson = redisTemplate.opsForValue().get("user:token:" + token);
                    if (userJson != null) {
                        TUser user = JSON.parseObject(userJson, TUser.class);
                        UserContext.setUser(user);

                        // 重置token映射的过期时间
                        redisTemplate.expire("user:token:" + token, 30, TimeUnit.MINUTES);

                        // 重置手机号映射的过期时间
                        redisTemplate.expire("user:id:" + user.getId(), 30, TimeUnit.MINUTES);

                        return true;
                    }
                }
            }
        }

        // Token无效或不存在, 这里决定如何处理
        response.sendRedirect("/login/lg"); //重定向到登录页面
        return false;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser(); // 清理UserContext
    }


}
