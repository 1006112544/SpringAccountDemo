package com.db;

import com.db.dao.UserDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class MTest {
    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml","spring-mvc.xml");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        String sql = "UPDATE user SET username = ? WHERE id = ?";//问号表示需要传入的参数
        jdbcTemplate.update(sql,"name2",1);
    }
}
