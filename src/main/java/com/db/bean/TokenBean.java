package com.db.bean;

public class TokenBean {
    private Integer id;
    private String jwt;

    public TokenBean(){

    }

    public TokenBean(Integer id,String jwt){
        this.id = id;
        this.jwt = jwt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
