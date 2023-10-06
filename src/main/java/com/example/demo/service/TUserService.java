package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.TUser;
import com.example.demo.result.Result;
import com.example.demo.vo.Responsebean;
import com.example.demo.vo.loginvo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 用户表(TUser)表服务接口
 *
 * @author makejava
 * @since 2023-10-03 15:13:35
 */
public interface TUserService extends IService<TUser> {


    Result<String> loginservice(loginvo loginvo, HttpServletResponse response);
}
