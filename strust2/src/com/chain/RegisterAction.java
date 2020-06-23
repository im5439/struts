package com.chain;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class RegisterAction extends ActionSupport implements Preparable, ModelDriven<UserDTO> {
	
	private static final long serialVersionUID = 1L;
	
	private UserDTO dto;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserDTO getDto() {
		return dto;
	}
	
	@Override
	public UserDTO getModel() {
		return dto;
	}
	
	@Override
	public void prepare() throws Exception {
		dto = new UserDTO();
	}
	
	public String created() throws Exception {
		if(dto == null || dto.getMode() == null || dto.getMode().equals(""))
			return INPUT;
		
		message = dto.getUserId() + "회원가입 성공!!";
		
		return SUCCESS;
		
	}

	
	
}
