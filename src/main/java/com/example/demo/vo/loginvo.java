package com.example.demo.vo;

import com.example.demo.annation.MobileValidate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName loginvo
 * @Description TODO
 * @Date 2023/10/3 16:01
 */
@Data
public class loginvo {
    @NotNull(message = "手机号不能为空")
    @MobileValidate
    private  String mobile;
    @NotBlank(message = "密码不能为空")
    private  String password;
}
