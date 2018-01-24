package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 第二十五颗星星
 * @create 2018-01-22 14:43
 * @desc 登录注册页面展示Controller
 **/
@Controller
@RequestMapping("/page")
public class PageController {
    @RequestMapping("/register")
    public String showRegister(){
        return "register";
    }

    @RequestMapping("/login")
    public String showLogin(){
        return "login";
    }

}