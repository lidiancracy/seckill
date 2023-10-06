package com.example.demo.utils;


import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName md5util
 * @Description TODO
 * @Date 2023/10/3 14:50
 */
@Component
public class md5util {
    // 基于输入进行加密处理
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";		// 盐
    // 一次加密
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);	// 构造加密串

        return md5(str);
    }
    // 二次加密
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    // 组合加密
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        String s = inputPassToDbPass("123456","123456");
        System.out.println(s);
//        System.out.println(inputPassToFormPass("15534062853"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
//		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
    }
}
