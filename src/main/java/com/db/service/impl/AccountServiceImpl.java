package com.db.service.impl;

import com.db.bean.TokenBean;
import com.db.bean.UserBean;
import com.db.constant.CodeUtils;
import com.db.constant.ConstantUtil;
import com.db.dao.TokenDao;
import com.db.dao.UserDao;
import com.db.exception.ApiException;
import com.db.service.i.AccountServiceInterface;
import com.db.util.JavaWebTokenUtil;
import com.db.util.Md5Util;
import com.mysql.cj.jdbc.jmx.LoadBalanceConnectionGroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccountServiceImpl implements AccountServiceInterface {

    @Autowired
    private UserDao mUserDao;
    @Autowired
    private TokenDao mTokenDao;

    public UserBean searchUserByUserName(String username) {
        try{
            return mUserDao.searchUserByUserName(username);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public UserBean searchUserById(int id) {
        try{
            return mUserDao.searchUserById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void deleteUserByUserName(String username) {
        mUserDao.deleteUserByUserName(username);
    }

    public void deleteUserById(int id) {
        mUserDao.deleteUserById(id);
    }

    public void updateUser(UserBean userBean) throws ApiException {
        //判断用户是否存在
        if(mUserDao.searchUserByUserName(userBean.getUsername())==null){
            throw new ApiException(CodeUtils.USER_NO_EXIST,"该用户不存在,请先注册");
        }
        //确认验证码是否准确
        mUserDao.updateUserByName(userBean);
    }

    public void addUser(UserBean userBean) throws ApiException {
        String username = userBean.getUsername();
        String password = userBean.getPassword();
        //判断用户是否存在
        System.out.println(mUserDao.searchUserByUserName(username));
        if(mUserDao.searchUserByUserName(username)!=null){
            throw new ApiException(CodeUtils.USER_EXIST,"该用户已经存在,请直接登录");
        }
        //设置加密盐值
        userBean.setSalt(password.substring(password.length()-3, password.length()));
        //进行密码加密
        String md5Password = Md5Util.createMD5(password,userBean.getSalt());
        userBean.setPassword(md5Password);
        //添加用户
        mUserDao.addUser(userBean);
    }

    public String userLogin(String username,String password,HttpServletRequest request) throws ApiException {
        UserBean userBean;
        //判断用户是否存在
        if((userBean = mUserDao.searchUserByUserName(username))==null){
            throw new ApiException(CodeUtils.USER_NO_EXIST,"该用户不存在,请先注册");
        }
        if(password.equals(userBean.getPassword())) {
            //登陆成功
            //将用户登陆信息保存到session中
            request.getSession().setAttribute(ConstantUtil.USER,userBean);
            //创建Token 7天有效
            String jwt  = JavaWebTokenUtil.createJWT(String.valueOf(userBean.getId()),
                    "DB", username, 1000*60*60*24*7);
            if(userBean.getTokenId()==null){
                //如果user的TokenId为null在数据库中创建一个新的token并关联到user
                TokenBean tokenBean = new TokenBean(null,jwt );
                mTokenDao.addToken(tokenBean);
                if(tokenBean.getId()!=null){
                    userBean.setTokenId(tokenBean.getId());
                    mUserDao.updateUserByName(userBean);
                }
            }else {
                //如果user的TokenId不为null则更新Token
                TokenBean tokenBean = new TokenBean(userBean.getTokenId(),jwt);
                mTokenDao.updateToken(tokenBean);
            }
            return jwt;
        }else {
            //登陆失败
            throw new ApiException(CodeUtils.USER_PASSWORD_ERROR,"密码错误,请检查后重试");
        }
    }

    public String userLoginByToken(String jwt,HttpServletRequest request) throws ApiException{
        UserBean userBean;
        if((userBean=mUserDao.searchUserByToken(jwt))!=null){
            //登陆成功
            String response = JavaWebTokenUtil.parseJWT(jwt);
            if(response.equals("success")){
                //将用户登陆信息保存到session中
                request.getSession().setAttribute(ConstantUtil.USER,userBean);
                return jwt;
            }else if(response.equals("fail")){
                //登陆失败
                throw new ApiException(CodeUtils.INVALID_TOKEN,"无效的Token");
            }else {
                //将用户登陆信息保存到session中
                request.getSession().setAttribute(ConstantUtil.USER,userBean);
                //token有效期低于了48小时 自动更新了Token
                return response;
            }
        }else {
            //登陆失败
            throw new ApiException(CodeUtils.INVALID_TOKEN,"无效的Token");
        }
    }
}
