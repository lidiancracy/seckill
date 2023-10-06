package com.example.demo.config;

import com.example.demo.interception.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName webconfig
 * @Description TODO
 * @Date 2023/10/6 18:25
 */
@Configuration
public class webconfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)  // Use the autowired instance
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login/**",
                        "/bootstrap/**",
                        "/img/**",
                        "/jquery-validation/**",
                        "/js/**",
                        "/layer/**"
                );
    }


}
