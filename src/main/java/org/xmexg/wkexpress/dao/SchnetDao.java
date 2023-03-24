package org.xmexg.wkexpress.dao;

import org.apache.ibatis.annotations.Select;

/**
 * schoolnet数据表的操作
 * 其中id:主键,openid:用户微信的唯一标识符,name:学生姓名,phone:学生手机号,stuid:学号,idcard:身份证号,department:院系,duration,办理时长,orderdate:订单创建时间,state:办理状态(暂时无用,0:未付款,1:已付款,2:已取消),money:办理金额
 */
public interface SchnetDao {

//    @Select("select * from schnet where openid=#{openid}")
}
