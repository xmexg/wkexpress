package org.xmexg.wkexpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.entity.Ordering;
import org.xmexg.wkexpress.service.ExpresspointService;
import org.xmexg.wkexpress.service.OrderingService;

import java.util.List;

/**
 * 测试用例
 */
@RestController
@RequestMapping("/test")
public class Test {

    @Autowired
    private OrderingService OrderingService;
    @Autowired
    private ExpresspointService expresspointService;

    @RequestMapping("/showallorder")
    public String showallorder(){
        List<Ordering> orderings = OrderingService.getAll();
        String msg = orderings == null ? "查询失败" : orderings.toString();
        return msg;
    }

    @RequestMapping("/point")
    public String[] point(){
        return WkexpressApplication.tempData.expresspoint;
    }

    @RequestMapping("/pointsql")
    public String[] pointsql(){
        String[] exp = expresspointService.getAll();
        for (String s : exp) {
            System.out.print(s);
        }
        return exp;
    }
}
