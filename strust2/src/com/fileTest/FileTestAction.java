package com.fileTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.FileManager;

public class FileTestAction extends ActionSupport implements Preparable, ModelDriven<FileTestDTO> {

	private static final long serialVersionUID = 1L;

	private FileTestDTO dto;

	public FileTestDTO getDto() {
		return dto;
	}

	@Override
	public FileTestDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		dto = new FileTestDTO();
	}

	public String created() throws Exception {

		if (dto == null || dto.getMode() == null || dto.getMode().equals("")) {
			return INPUT;
		}

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();

		String root = session.getServletContext().getRealPath("/");
		String savePath = root + File.separator + "pds" + File.separator + "saveData";

		saveFileName = FileManager.doFileUpload(dto.getUpload(), dto.getUploadFileName(), savePath);

		originalFileName = dto.getUploadFileName();

		return SUCCESS;

	}

	public String download() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		String savePath = root + File.separator + "pds" + File.separator + "saveData";
		
		FileManager.doFileDownload(saveFileName, originalFileName, savePath, response);
		
		return null;
		
	}
	
	public String down() throws Exception { // 파일 보기
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		String root = session.getServletContext().getRealPath("/");
		String savePath = root + File.separator + "pds" + File.separator + "saveData";
		
		originalFileName = new String(originalFileName.getBytes("euc-kr"), "8859_1");
		
		String fullFileName = savePath + File.separator + saveFileName;
		
		inputStream = new FileInputStream(fullFileName);
		
		return SUCCESS;
		
	}
	

	private InputStream inputStream; // 파일 보기

	// DB사용시
	private String saveFileName;
	private String originalFileName;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

}
