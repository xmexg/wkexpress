# 校园快递帮微信小程序后端  


# 开始使用  

## 数据库配置  
 + 自行创建数据库wkexpress  
 + 查看数据库  
```
show databases;
```
 + 使用wkexpress数据库  
```
use wkexpress;
```
 + 创建configdata数据表,用于存放一些基本的配置信息  
```
create table if not exists configdata(`name` varchar(50), `value` varchar(50))engine=InnoDB default charset=utf8;
```
 + 创建expresspoint数据表,用于存放快递点  
```
create table if not exists expresspoint(`point` varchar(50))engine=InnoDB default charset=utf8;
```
 + 创建userlist数据表,用于存放用户列表,user_type:1配送员,2用户,9管理员  
```
create table userlist (`user_id` int unsigned auto_increment, `user_type` tinyint, `user_session_key` varcher(50), `user_openid` varchar(50), `user_cookie` varchar(100), `user_creattime` datetime, primary key (`user_id`))engine=InnoDB default charset=utf8;
```
 + 创建booking数据表,用于存放当前正在进行的订单,包含已支付的和未支付的  
 openid:支付者的微信唯一标识,orderid:服务器生成的订单号,ordertime:用户下单时间,ordertype:下单类型(0:用户确认价格后取货,1:直接取货),orderpricecon:用户是否已确认订单价格(0:未确认,1:已确认或不需确认),orderamount:订单金额,pickup:取货地点,pickdown:送达地点,pickcode:取货码,pickname:包裹名,name:姓名,phone:手机号,note:备注,shipper送货员的openid,payid:微信支付订单号,paytime:用户付款的时间
```
create table if not exists booking(openid varchar(50), orderid varchar(50), ordertime datetime, ordertype tinyint, orderpricecon tinyint,orderamount float, pickup varchar(50), pickdown varchar(50), pickcode varchar(20), pickname varchar(20), name varchar(10), phone varchar(20), note varchar(100), shipper varchar(50), payid varchar(50), paytime datetime) engine=InnoDB default charset=utf8;
```

 + 创建schoolnet数据表，用于存放办理校园网的信息，其中id:主键,name:学生姓名,phone:学生手机号,stuid:学号,idcard:身份证号,department:院系,duration,办理时长,orderdate:订单创建时间,state:办理状态(暂时无用,0:未付款,1:已付款,2:已取消),money:办理金额

```
create table if not exists `schoolnet`(`id` int unsigned auto_increment, openid varchar(50) not null, name varchar(10) not null, phone varchar(20) not null, stuid varchar(20) not null, idcard varchar(20), department varchar(50), duration varchar(10) not null, orderdate timestamp, state tinyint, money double, primary key (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

