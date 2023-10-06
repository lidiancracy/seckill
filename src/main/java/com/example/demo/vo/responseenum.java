package com.example.demo.vo;

import lombok.Data;

/**
 * @ClassName responseenum
 * @Description TODO
 * @Date 2023/10/3 15:42
 */

public enum responseenum {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权")
    // ... 其他状态码和消息可以继续添加

    ;
    private long code;
    private String msg;

    responseenum(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
