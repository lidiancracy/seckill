package com.example.demo.exception;

/**
 * @ClassName GoodsSoldOutException
 * @Description TODO
 * @Date 2023/10/9 21:08
 */
public class GoodsSoldOutException extends RuntimeException {
    public GoodsSoldOutException(String message) {
        super(message);
    }
}

