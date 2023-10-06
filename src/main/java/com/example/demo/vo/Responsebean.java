package com.example.demo.vo;

import lombok.Data;

/**
 * @ClassName Responsebean
 * @Description TODO
 * @Date 2023/10/3 15:40
 */
@Data
public class Responsebean {
    private  long code;
    private  String msg;
    private  Object obj;
    // 成功的方法，返回默认的成功消息和数据
    public static Responsebean success(Object data) {
        return new Responsebean(responseenum.SUCCESS.getCode(), responseenum.SUCCESS.getMsg(), data);
    }

    // 成功的方法，允许自定义消息
    public static Responsebean success(String msg, Object data) {
        return new Responsebean(responseenum.SUCCESS.getCode(), msg, data);
    }

    // 构造方法
    private Responsebean(long code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.obj = data;
    }
}
