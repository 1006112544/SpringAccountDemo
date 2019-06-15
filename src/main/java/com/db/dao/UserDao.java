package com.db.dao;

import com.db.bean.UserBean;
import com.db.common.annoation.MyBatisDao;

@MyBatisDao
public interface UserDao {
    UserBean searchUserByUserName(String username);
    UserBean searchUserById(int id);
    UserBean searchUserByToken(String jwt);
    void addUser(UserBean userBean);
    void updateUserById(UserBean userBean);
    void updateUserByName(UserBean userBean);
    void deleteUserById(int id);
    void deleteUserByUserName(String username);
}
