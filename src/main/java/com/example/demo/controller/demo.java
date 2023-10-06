package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName demo
 * @Description TODO
 * @Date 2023/10/3 14:36
 */
@Controller
@RequestMapping("/demo")
public class demo {
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","lidian");
        return "hello";
    }
}
