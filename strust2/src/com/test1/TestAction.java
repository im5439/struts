package com.test1;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	//Domain Object 방식
	private UserDTO dto;
	private String message;

	public UserDTO getDto() {
		return dto;
	}
	public void setDto(UserDTO dto) {
		this.dto = dto;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String execute() throws Exception {
		
		message = dto.getUserName() + "님 방가방가...";
		
		return "ok";
		
	}
	
}
