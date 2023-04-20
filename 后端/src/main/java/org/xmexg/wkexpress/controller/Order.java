package org.xmexg.wkexpress.controller;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.entity.Ordering;
import org.xmexg.wkexpress.entity.User;
import org.xmexg.wkexpress.service.ExpresspointService;
import org.xmexg.wkexpress.service.OrderingService;
import org.xmexg.wkexpress.service.UserService;
import org.xmexg.wkexpress.tools.MySqlTools;

import java.sql.Timestamp;
import java.util.List;

/**
 * 处理快递订单业务
 */
@RestController
@RequestMapping("/order")
public class Order {

    @Autowired
    private OrderingService orderingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpresspointService expresspointService;
    @Autowired
    private MySqlTools mysqltools;


    @PostMapping("/addOrder")
    public String addOrder(@RequestBody Ordering ordering,@RequestHeader("token") String token){
        //检查token是否合法
        //未登录
        if (token == null || token.equals("")) {
            System.out.println("未登录");
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;//登录信息过期
        }

        //查询用户没有未支付的订单
        String user_openid = user.getUser_openid();
        boolean haveUnpaidOrder = orderingService.unpaidOrderNum(user_openid) >= WkexpressApplication.tempData.MaxAllowUnpaidOrder;
        if (haveUnpaidOrder) {
            return "您有未支付的订单,请先支付";
        }

        //查询是否已有该订单(检查订单号)
        //订单号生成规则:user_id+快递点编码+纯数字取货码
        String pickcode = ordering.getPickcode();
        //通过正则表达只保留纯数字
        String regexnotnum = "[^0-9]";//正则表达式,匹配非数字
        pickcode = pickcode.replaceAll(regexnotnum, "");//替换非数字为空 123abc456 -> 123456
        String orderid = user.getUser_id() + ordering.getPickup() + pickcode;
        boolean haveOrder = orderingService.getOrderByOrderid(orderid) != null;
        if (haveOrder) {
            return "该订单已存在,订单号:" + orderid;
        }

        //检查订单信息是否完整
        if(ordering.getPickup() == null || ordering.getPickcode() == null || ordering.getPickup().equals("") || ordering.getPickcode().equals("") || ordering.getPickname() == null || ordering.getPickname().equals("") || ordering.getPhone() == null || ordering.getPhone().equals("")){
            return WkexpressApplication.tempData.Order_incomplete;
        }

        //准备保存订单,填写订单信息
        //转换快递点编码为快递点名称
        int pickup = Integer.parseInt(ordering.getPickup());
        if (WkexpressApplication.tempData.expresspoint != null) {
            ordering.setPickup(WkexpressApplication.tempData.expresspoint[pickup]);
        } else {
            WkexpressApplication.tempData.expresspoint = expresspointService.getAll();
            ordering.setPickup(WkexpressApplication.tempData.expresspoint[pickup]);
        }
        ordering.setOrderid(orderid);
        //时间精确到秒
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ordering.setOrdertime(timestamp);
        ordering.setOpenid(user_openid);

        boolean save =  orderingService.addOrder(ordering);
        return save ? "保存成功" : "保存失败";
    }

    //测试使用,显示所有订单
//    @RequestMapping("/showallorder")
    public String showallorder(String token){
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;//登录信息过期
        }
        if( user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin){
            return "您没有权限查看该信息";
        }
        
        List<Ordering> orderings = orderingService.getAll();
        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
        return msg;
    }

    //订单页面显示用户订单,任何人可查看,普通用户查看只会显示关键内容,派送员和管理员查看显示更多关键信息
//    @RequestMapping("/showorder")//旧的
//    public String showorder(Integer page){
//        List<Ordering> orderings = orderingService.client_orderPage_getOrder(page);
//        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
//        return msg;
//    }
    @PostMapping("/showorder")
    public String showorder(@RequestParam String token, @RequestParam Integer page){
//        System.out.println("订单页token:"+token+" page:"+page);
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;
        }
        List<Ordering> orderings = null;
        if( user.getUser_type() == WkexpressApplication.tempData.Usertype_Admin || user.getUser_type() == WkexpressApplication.tempData.Usertype_Delivery){
            //管理员和派送员显示详细信息
            System.out.println("管理员和派送员显示详细信息");
            orderings = orderingService.client_orderPage_getOrder_manager(page);
        }
        if( user.getUser_type() == WkexpressApplication.tempData.Usertype_Customer){
            //顾客显示简要信息
            orderings = orderingService.client_orderPage_getOrder(page);
        }
        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
        System.out.println(msg);
        return msg;
    }

    //派送员或管理员在前台点击接单后,在此接口接收信息,将订单状态改为已接单状态
    @PostMapping("/deliveryorder")
    public String deliveryorder(@RequestParam String token, @RequestParam String orderid, @RequestParam float orderamount){
        System.out.println("token:"+token+"\torderid:"+orderid+"\torderamount:"+orderamount);
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        orderid = mysqltools.TransactSQLInjection(orderid);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;
        }
        if( user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin && user.getUser_type() != WkexpressApplication.tempData.Usertype_Delivery){
            return WkexpressApplication.tempData.NoPermissionToOperate;//没有权限操作
        }

        //检查价格
        if(orderamount < WkexpressApplication.tempData.Order_amount_min || orderamount > WkexpressApplication.tempData.Order_amount_max){
            return WkexpressApplication.tempData.Order_amount_error;//价格错误
        }

        //检查订单是否未被接单
        Ordering order = orderingService.getOrderByOrderid(orderid);
        if(order.getShipper() != null){
            return WkexpressApplication.tempData.Order_accepted;//订单已被接单
        }

        //更新订单信息
        order.setOrderamount(orderamount);
        order.setShipper(user.getUser_openid());
        boolean updateok = orderingService.updateOrder(order);
        return updateok ? WkexpressApplication.tempData.Deliveryorder_Success : WkexpressApplication.tempData.Deliveryorder_Fail;
    }


    //已弃用
    //查看具体的订单,只有管理员和派送员可查看
//    @RequestMapping("/showorderdetailbyorderid")
    public String showorderdetailbyorderid(String token, String orderid){
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        orderid = mysqltools.TransactSQLInjection(orderid);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;
        }
        if( user.getUser_type() != WkexpressApplication.tempData.Usertype_Admin && user.getUser_type() != WkexpressApplication.tempData.Usertype_Delivery){
            return WkexpressApplication.tempData.NoPermissionToView;//没有权限查看该信息
        }

        Ordering ordering = orderingService.getOrderByOrderid(orderid);
        String msg = ordering == null ? "" : JSON.toJSONString(ordering);
        return msg;
    }

    //用户查看自己的订单
    @RequestMapping("/myorder")
    public String myorder(String token,Integer page){
        //检查token是否合法
        //未登录
        if (token == null) {
            return WkexpressApplication.tempData.NotLogin;
        }
        //过滤参数
        token = mysqltools.TransactSQLInjection(token);
        //检查是否存在该用户
        User user = userService.getUserByToken(token);
        if (user == null) {
            return WkexpressApplication.tempData.Penetration;//能发送数据,说明登录了,用户登录时会返回token,不应该出现这种情况,除非渗透测试
        }

        List<Ordering> orderings = orderingService.client_getOrderById(user.getUser_openid(),page);
        String msg = orderings == null ? "" : JSON.toJSONString(orderings);
        return msg;
    }
}
