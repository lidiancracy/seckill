package com.example.demo.pojo;

import java.math.BigInteger;
import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(TUser)表实体类
 *
 * @author makejava
 * @since 2023-10-03 15:13:35
 */
@SuppressWarnings("serial")
@Data
@TableName("t_user")
@AllArgsConstructor
@NoArgsConstructor
public class TUser {
//用户ID,手机号码    private Long id;

    private BigInteger id;
    private String nickname;
    //MD5(MD5(pass明文+固定salt)+salt)
    private String password;

    private String salt;
    //头像
    private String head;
    //注册时间
    private Date registerDate;
    //最后一次登录事件
    private Date lastLoginDate;
    //登录次数
    private Integer loginCount;


}
