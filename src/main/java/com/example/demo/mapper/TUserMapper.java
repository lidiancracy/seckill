package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.TUser;
import org.apache.ibatis.annotations.Select;


/**
 * 用户表(TUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-03 15:13:35
 */
public interface TUserMapper extends BaseMapper<TUser> {

    @Select("SELECT * FROM t_user WHERE id = #{mobile}")
    TUser findByMobile(String mobile);
}
