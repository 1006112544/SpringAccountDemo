package com.db.controller;

import com.db.bean.LoginBean;
import com.db.bean.TokenBean;
import com.db.bean.UserBean;
import com.db.constant.ConstantUtil;
import com.db.exception.ApiException;
import com.db.model.ResponseModel;
import com.db.service.impl.AccountServiceImpl;
import com.db.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.db.constant.CodeUtils.*;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;
    @RequestMapping(value = "/hello",method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> Hello(HttpServletRequest request){
        UserBean userBean = (UserBean) request.getSession().getAttribute(ConstantUtil.USER);
        if(userBean==null){
            return ResponseEntityUtil.createResponse(USER_UNLOGIN, "您还没有登录");
        }else {
            return ResponseEntityUtil.createResponse(SUCCESSFUL, userBean.toString());
        }
    }

    @RequestMapping(value="/user_login",method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> UserLogin(@RequestBody LoginBean bean, HttpServletRequest request, HttpServletResponse response){
        String username = bean.getUsername();
        String password = bean.getPassword();
        if(TextUtil.isEmpty(username)||TextUtil.isEmpty(password)){
            return ResponseEntityUtil.createResponse(PARMAMETER_ERROR, "请求参数不符合API要求");
        }
        //验证登陆
        try {
            String jwt = accountService.userLogin(username,password,request);
            return ResponseEntityUtil.createResponse(SUCCESSFUL,"登陆成功",jwt);
        } catch (ApiException e) {
            Logger.getLogger(getClass()).error(e.getMessage());
            Logger.getLogger(getClass()).error("客户端请求地址：" + request.getRequestURL().toString());
            Logger.getLogger(getClass()).error("登录账户：" + username);
            Logger.getLogger(getClass()).error("登录密码：" + password);
            return ResponseEntityUtil.createResponse(e.getCode(), e.getMessage());
        }
    }

    /**
     * token登陆
     */
    @RequestMapping(value="/user_login_token",method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> UserLoginByToken(@RequestBody TokenBean tokenBean, HttpServletRequest request){
        String jwt = tokenBean.getJwt();
        if(TextUtil.isEmpty(jwt)){
            return ResponseEntityUtil.createResponse(PARMAMETER_ERROR, "请求参数不符合API要求");
        }
        //验证登陆
        try {
            String responseJwt = accountService.userLoginByToken(jwt,request);
            return ResponseEntityUtil.createResponse(SUCCESSFUL,"登陆成功",responseJwt);
        } catch (ApiException e) {
            return ResponseEntityUtil.createResponse(e.getCode(), e.getMessage());
        }
    }
    /**
     * 用户注册接口
     */
    @RequestMapping(value="/user_register",method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> registerUser(@RequestBody UserBean bean,HttpServletRequest request){
        String username = bean.getUsername();
        String password = bean.getPassword();
        String email = bean.getEmail();
        if(TextUtil.isEmpty(username)||TextUtil.isEmpty(password)||TextUtil.isEmpty(email)){
            return ResponseEntityUtil.createResponse(PARMAMETER_ERROR, "请求参数不符合API要求");
        }
        try {
            accountService.addUser(bean);
        } catch (ApiException e) {
            if(e.getCode()==USER_EXIST){
                return ResponseEntityUtil.createResponse(USER_EXIST, e.getMessage());
            }
        }
        Logger.getLogger(getClass()).error("用户注册");
        Logger.getLogger(getClass()).error("客户端请求地址：" + request.getRequestURL().toString());
        Logger.getLogger(getClass()).error("注册账户：" + username);
        return ResponseEntityUtil.createResponse(SUCCESSFUL, "注册成功");
    }

}
