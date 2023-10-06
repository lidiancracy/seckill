package com.example.demo.exception;

import com.example.demo.result.CodeMsg;

/**
 * @ClassName globalexception
 * @Description TODO
 * @Date 2023/10/3 21:42
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public CustomException(CodeMsg codeMsg) {
        super(codeMsg.toString());  // 将CodeMsg的信息传递给RuntimeException的message属性
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

}

