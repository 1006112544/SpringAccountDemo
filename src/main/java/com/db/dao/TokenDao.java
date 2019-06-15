package com.db.dao;

import com.db.bean.TokenBean;
import com.db.bean.UserBean;
import com.db.common.annoation.MyBatisDao;

@MyBatisDao
public interface TokenDao {
    TokenBean searchToken(UserBean userBean);
    void updateToken(TokenBean tokenBean);
    void addToken(TokenBean tokenBean);
}
