package com.db.exception;

public class ApiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -305703364268131405L;
	private int code = 0;
	private Object data = null;

	public ApiException(int code, String msg, Object data) {
		super(msg);
		this.code = code;
		this.data = data;
	}

	public ApiException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}
}