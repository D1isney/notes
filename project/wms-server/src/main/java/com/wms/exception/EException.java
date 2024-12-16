package com.wms.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * @author koch
 *
 */
@Setter
@Getter
public class EException extends RuntimeException{
	private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;
	private String errField;


    public EException(String msg) {
		super(msg);
		this.msg = msg;
	}


	public EException(String msg, String errField) {
		super(msg);
		this.msg = msg;
		this.errField = errField;
	}
	
	public EException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public EException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public EException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

}