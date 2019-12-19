package com.weisen.www.code.yjf.login.service.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("serial")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Result implements Serializable {

	private int code;

	private String message;

	private Integer totalElements;

	private Object data;

	public static final int SUCCESS = 1;

	public static final int FAILURE = 0;

	public static Result suc(String message, Object data, Integer totalElements) {
		return new Result(SUCCESS, message, totalElements, data);
	}

	public static Result suc(String message, Object data) {
		return new Result(SUCCESS, message, 1, data);
	}

	public static Result suc(String message) {
		return suc(message, null, null);
	}

	public static Result suc() {
		return suc("操作成功");
	}

	public static Result fail(String message, Object data) {
		return new Result(FAILURE, message, 0, data);
	}

	public static Result fail(String message) {
		return new Result(FAILURE, message, null, null);
	}

	public static Result fail() {
		return fail("操作失败");
	}

	private Result(int code, String message, Integer totalElements, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.totalElements = totalElements;
		if (data != null && !(data instanceof List<?>)) {
			this.data = Arrays.asList(data);
		} else if (data == null) {
			this.totalElements = 0;
			this.data = Arrays.asList();
		} else {
			if (!((List<?>) data).isEmpty()) {
				this.data = data;
			} else {
				this.data = Arrays.asList();
				this.totalElements = 0;
			}
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static void main(String[] args) {
		String s = "Gasd asda";
		String str = s.toUpperCase();
		System.out.println(str);
	}
}
