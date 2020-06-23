package com.test2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class TestAction extends ActionSupport implements Preparable,ModelDriven<UserDTO>{

	private static final long serialVersionUID = 1L;

	private UserDTO dto;

	public UserDTO getDto() {
		return dto;
	}
	
	@Override
	public void prepare() throws Exception {
		
		dto = new UserDTO();
		
	}

	@Override
	public UserDTO getModel() {
		
		return dto;
	}
	
	public String created() throws Exception {
		
		if (dto == null || dto.getMode() == null || dto.getMode().equals("")) {
			
			return INPUT;
			
		}
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		request.setAttribute("message", "½ºÆ®·µÃ÷2...");
		//request.setAttribute("dto", dto);
		
		return SUCCESS;
		
	}
	
	
	

}
