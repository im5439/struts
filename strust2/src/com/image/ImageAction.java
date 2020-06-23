package com.image;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.board.BoardDTO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class ImageAction extends ActionSupport implements Preparable, ModelDriven<ImageDTO>{

	private static final long serialVersionUID = 1L;
	
	private ImageDTO dto;
	
	public ImageDTO getDto() {
		return dto;
	}

	@Override
	public ImageDTO getModel() {
		return dto;
	}

	@Override
	public void prepare() throws Exception {
		dto = new ImageDTO();
	}

	public String list() throws Exception {
		
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		HttpServletRequest req = ServletActionContext.getRequest();

		String cp = req.getContextPath();

		int numPerPage = 9;
		int totalPage = 0;
		int totalDataCount = 0;

		int currentPage = 1;

		if (dto.getPageNum() != null && !dto.getPageNum().equals("")) {
			currentPage = Integer.parseInt(dto.getPageNum());
		}

		Map<String, Object> hMap = new HashMap<String, Object>();

		totalDataCount = dao.getIntValue("board.dataCount", hMap);

		if (totalDataCount != 0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}

		// 데이터 삭제로 페이지 수 줄었을 때
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		int start = (currentPage - 1) * numPerPage + 1;
		int end = currentPage * numPerPage;

		hMap.put("start", start);
		hMap.put("end", end);

		List<Object> lists = (List<Object>) dao.getListData("board.listData", hMap);

		// 데이터를 일련번호로 정렬하기 위해 listNum 사용.
		int listNum, n = 0;
		ListIterator<Object> it = lists.listIterator();
		while (it.hasNext()) {
			BoardDTO dto = (BoardDTO) it.next();
			listNum = totalDataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
		}

		String urlList = "";
		String urlArticle = "";

		urlList = cp + "/imageTest/list.action";
		urlArticle = cp + "/imageTest/write.action?pageNum=" + currentPage;

		req.setAttribute("lists", lists);
		req.setAttribute("totalDataCount", totalDataCount);
		req.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		req.setAttribute("urlArticle", urlArticle);

		return SUCCESS;
		
	}
	
	public String upload() throws Exception {
		
		return SUCCESS;
		
	}
	
	
	

}
