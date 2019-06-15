package com.db.constant;

public class CodeUtils {
    public final static int SUCCESSFUL = 200;            //请求成功
    public final static int FAIL = 500;                  //请求失败
    public final static int USER_EXIST = 9001;           //用户已经存在
    public final static int USER_NO_EXIST = 9002;        //用户不存在
    public final static int USER_PASSWORD_ERROR = 9003;  //用户密码错误
    public final static int PARMAMETER_ERROR = 9004;     //请求参数错误
    public final static int USER_UNLOGIN = 9005;         //用户未登录
    public final static int UNKONW_ERROR = 9009;         //服务器未知错误
    public final static int INVALID_TOKEN = 9010;        //无效的Token
}
