package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmexg.wkexpress.service.UserService;

@RestController
@RequestMapping("/user")
public class User {

    @Autowired
    private UserService login;

    //微信小程序登录,从前端收到code,然后发送到后端,后端再发送到微信服务器,获取openid
    @RequestMapping("/login")
    public String login(String code){
        String result = login.loginByCode(code);
        System.out.println(code);
        System.out.println(result);
        return result;
    }
}
