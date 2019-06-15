package com.db.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

public class JavaWebTokenUtil {

    /**
     * 创建token
     * @param id TokenId
     * @param issuer 签发者
     * @param subject 面向的用户
     * @param ttlMillis 有效期
     * @return token
     */
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
        //id，issuer，subject，ttlMillis都是放在payload中的，可根据自己的需要修改
        //签名的算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //当前的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //签名算法的秘钥，解析token时的秘钥需要和此时的一样
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("TokenKey");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //构造
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        Logger.getLogger("---token生成---");
        //给token设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            Logger.getLogger("过期时间："+exp);
            builder.setExpiration(exp);
        }
        //创建token 并压缩为json
        return builder.compact();
    }


    /**
     * 解析并验证Token
     * @param jwt javaWebToken
     * @return string
     */
    public  static String parseJWT(String jwt) throws ExpiredJwtException
            ,SignatureException,MalformedJwtException{
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter
                .parseBase64Binary("TokenKey")).parseClaimsJws(jwt).getBody();
        Logger.getLogger("------解析token----");
        Logger.getLogger("ID: " + claims.getId());
        Logger.getLogger("Subject: " + claims.getSubject());
        Logger.getLogger("Issuer: " + claims.getIssuer());
        Logger.getLogger("IssuerAt:   " + claims.getIssuedAt());
        Logger.getLogger("Expiration: " + claims.getExpiration());
        /**
         *检验token是或否即将过期，如快要过期，就提前更新token。
         *如果已经过期的，会抛出ExpiredJwtException 的异常
         */
        Long exp=claims.getExpiration().getTime(); //过期的时间
        long nowMillis = System.currentTimeMillis();//现在的时间
        Date now=new Date(nowMillis);
        Logger.getLogger("currentTime:"+now);
        long seconds=exp-nowMillis;//剩余的时间 ，若剩余的时间小与48小时，就返回一个新的token给APP
        if (seconds<0){
            //token过期
            return "fail";
        }
        else if (seconds<=1000*60*60*48){
            //token有效期低于48小时自动更新
            return  createJWT(claims.getId(),claims.getIssuer(),claims.getSubject(),1000*60*60*24*7);
        }
        return "success";
    }
}
