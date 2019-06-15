package com.db.service.i;

import com.db.bean.UserBean;
import com.db.exception.ApiException;

public interface AccountServiceInterface {
    UserBean searchUserByUserName(String username);
    UserBean searchUserById(int id);
    void deleteUserByUserName(String username);
    void deleteUserById(int id);
    void updateUser(UserBean userBean) throws ApiException;
    void addUser(UserBean userBean) throws ApiException;
}
