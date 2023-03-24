package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmexg.wkexpress.entity.Schnet;
import org.xmexg.wkexpress.service.SchoolnetService;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;


/**
 * 处理校园网业务
 */
@RestController
@RequestMapping("/schoolnet")
public class Schoolnet {

    @Autowired
    private SchoolnetService schoolnetService;
    @Autowired
    private UserService userService;
    @Autowired
    private MySqlTools mySqlTools;

    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody Schnet schnet, @RequestHeader("token") String token){

        return "pass";
    }



}
