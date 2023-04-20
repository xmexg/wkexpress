package org.xmexg.wkexpress.entity;

import java.sql.Date;

public class User {

    //user_id是主键,自增
    private int user_id; //用户id
    private int user_type; //用户类型,0:管理员,1:配送员,2:用户,3:黑名单
    private String user_session_key; //微信session_key
    private String user_openid; //微信openid
    private String user_token; //用户的token
    private Date user_creattime; //用户注册时间

    public int getUser_id() {
        return user_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_session_key() {
        return user_session_key;
    }

    public void setUser_session_key(String user_session_key) {
        this.user_session_key = user_session_key;
    }

    public String getUser_openid() {
        return user_openid;
    }

    public void setUser_openid(String user_openid) {
        this.user_openid = user_openid;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public Date getUser_creattime() {
        return user_creattime;
    }

    public void setUser_creattime(Date user_creattime) {
        this.user_creattime = user_creattime;
    }

}
