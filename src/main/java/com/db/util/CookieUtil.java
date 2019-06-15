package com.db.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {
    /**
     * 添加cookie
     * @param response HttpServletResponse
     * @param cookieName cookie名字
     * @param value cookie的值
     * @param maxAge cookie有效期
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String value, int maxAge){
        Cookie cookie = new Cookie(cookieName,value);
        cookie.setPath("/");
        if(maxAge>0){
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 根据name获取cookie
     * @param request HttpServletRequest
     * @param cookieName cookie的名字
     * @return cookie
     */
    public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(cookieName)){
            Cookie cookie = cookieMap.get(cookieName);
            return cookie;
        }
        return null;
    }

    /**
     * 读取HttpServletRequest中的cookie
     * @param request HttpServletRequest
     * @return cookie
     */
    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String,Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
