package com.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpUtil {
	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	public static String getAPIKey(HttpServletRequest request) {
		String requestAuthorization = request.getHeader("Authorization");
		if (requestAuthorization == null || "".equals(requestAuthorization)) {
			return "";
		}

		String[] requestAuth = requestAuthorization.split(":");
		if (requestAuth != null && requestAuth.length > 1) {
			return requestAuth[0];
		}
		return "";
	}

	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		return basePath;
	}

	/**
	 * 获取请求Body
	 *
	 * @param request
	 * @return
	 */
	public static String getBodyString(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		String character = request.getCharacterEncoding();
		if (character == null) {
			character = "utf-8";
		}
		try {
			inputStream = request.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, character));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 强制刷新客户端html
	 * 
	 * @param response
	 */
	public static void forceRefresh(HttpServletResponse response) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Buffer", "True");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Expires", "0");
		response.setHeader("ETag", String.valueOf(System.currentTimeMillis()));
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Date", String.valueOf(new Date()));
		response.setHeader("Last-Modified", String.valueOf(new Date()));
	}

	/**
	 * 获取当前请求HttpServletRequest
	 * 
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	/**
	 * 获取当前请求session
	 * 
	 * @return
	 */
	public static HttpSession getHttpSession() {
		return getHttpServletRequest().getSession();
	}
}