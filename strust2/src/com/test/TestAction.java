package com.test;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private String userId;
	private String userName;
	private String message;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	
	
	public String execute() throws Exception {
		
		message = userName + "님 방가방가...";
		
		return SUCCESS; // 이 변수(SUCCESS)안에 success가 들어가있음
		
	}
	
}
