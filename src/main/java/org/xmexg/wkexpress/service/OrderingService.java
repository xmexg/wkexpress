package org.xmexg.wkexpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmexg.wkexpress.WkexpressApplication;
import org.xmexg.wkexpress.dao.OrderingDao;
import org.xmexg.wkexpress.entity.Ordering;

import java.sql.Date;
import java.util.List;

@Service
public class OrderingService {

    @Autowired
    private OrderingDao orderingDao;

    public boolean updateOrder(Ordering order) {
        int update = orderingDao.update(order);
        return update > 0;
    }

    public List<Ordering> getAll(){
        return orderingDao.getAll();
    }

    public boolean addOrder(Ordering ordering){
        int insert = orderingDao.insert(ordering);
        return insert > 0;
    }

    public int unpaidOrderNum(String user_openid) {
        //List通过size()方法可以获取到List的长度
        return orderingDao.getUnpaidOrder(user_openid).size();
    }

    public List<Ordering> getUnpaidOrder(String user_openid) {
        return orderingDao.getUnpaidOrder(user_openid);
    }

    public Ordering getOrderByOrderid(String orderid) {
        return orderingDao.getOrderByOrderid(orderid);
    }

    //前端用户获取自己的订单,不会包含用户的openid和快递员信息
    public List<Ordering> client_getOrderById(String user_openid, Integer page) {
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_getOrderById(user_openid, (page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }

    //前端订单页面获取简要的订单信息,包含orderid,ordertime,ordertype,pickup,pickdown,分别对应订单号,下单时间,下单类型,取货地点,送达地点
    public List<Ordering> client_orderPage_getOrder(Integer page) {//普通用户使用
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_orderPage_getOrder((page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }
    public List<Ordering> client_orderPage_getOrder_manager(Integer page) {//管理员和派送员使用
        if(page == null || page < 1){
            page = 1;
        }
        return orderingDao.client_orderPage_getOrder_manager((page-1)*WkexpressApplication.tempData.PageSize, WkexpressApplication.tempData.PageSize);
    }
}
