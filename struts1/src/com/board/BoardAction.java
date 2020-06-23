package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.util.DBCPConn;
import com.util.MyUtil;

public class BoardAction extends DispatchAction {

	public ActionForward write(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		return mapping.findForward("created");
	}
	
	public ActionForward write_ok(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		Connection conn = DBCPConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		BoardForm f = (BoardForm) form;
		
		f.setNum(dao.getMaxNum() + 1);
		f.setIpAddr(request.getRemoteAddr());
		
		dao.insertData(f);
		
		return mapping.findForward("save");
		
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		String cp = request.getContextPath();
		
		MyUtil myUtil = new MyUtil();
		
		int numPerPage = 10;
		int totalPage = 0;
		int totalDataCount = 0;
		
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		
		if(pageNum != null) {
			currentPage = Integer.parseInt(pageNum);
		}
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue == null) {
			searchKey = "subject";
			searchValue = "";
		}
		
		if(request.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}

		totalDataCount = dao.getDataCount(searchKey, searchValue);
		
		if(totalDataCount != 0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}
		
		if(currentPage > totalPage)
			currentPage = totalPage;
		
		int start = (currentPage - 1) * numPerPage + 1;
		int end = currentPage * numPerPage;
		
		List<BoardForm> lists = dao.getLists(start, end, searchKey, searchValue);
		
		String param = "";
		String urlArticle = "";
		String urlList = "";
		
		if(!searchValue.equals("")) {
			
			searchValue = URLEncoder.encode(searchValue, "UTF-8");
			
			param = "&searchKey=" + searchKey;
			param += "&searchValue=" + searchValue;
			
		}
		
		urlList = cp + "/board.do?method=list" + param;
		urlArticle = cp + "/board.do?method=article&pageNum=" + currentPage;
		urlArticle += param;
		
		request.setAttribute("lists", lists);
		request.setAttribute("urlArticle", urlArticle);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalDataCount", totalDataCount);
		
		return mapping.findForward("list");
		
	}

	public ActionForward article(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		BoardForm dto = dao.getReadData(num);
		
		dao.updateHitCount(num);
		
		int lineSu = dto.getContent().split("\n").length;
		
		request.setAttribute("dto", dto);
		request.setAttribute("lineSu", lineSu);
		
		
		return mapping.findForward("article");
		
	}
	
	public ActionForward updated(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		BoardForm dto = dao.getReadData(num);
		
		request.setAttribute("dto", dto);
		
		return mapping.findForward("updated");
		
	}
	
	public ActionForward updated_ok(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		BoardForm f = (BoardForm) form;
		
		dao.updateData(f);
		
		return mapping.findForward("updated_ok");
		
	}
	
	public ActionForward deleted_ok(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		BoardDAO dao = new BoardDAO(DBCPConn.getConnection());
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		dao.deleteData(num);
		
		return mapping.findForward("deleted_ok");
		
	}
	
	
}
