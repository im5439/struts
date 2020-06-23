package com.board;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.util.MyUtil;
import com.util.dao.CommonDAO;
import com.util.dao.CommonDAOImpl;

public class BoardAction extends ActionSupport implements Preparable, ModelDriven<BoardDTO> {
	private static final long serialVersionUID = 1L;

	private BoardDTO dto; // ������ ������Ʈ

	public BoardDTO getDto() {
		return dto;
	}

	@Override
	public void prepare() throws Exception { // ��ü ����

		dto = new BoardDTO();
	}

	@Override
	public BoardDTO getModel() {
		return dto;
	}

	// �Խù� �ۼ�
	public String created() throws Exception {
		HttpServletRequest req = ServletActionContext.getRequest(); // �ʿ��ϸ� ����.

		// �ܼ��� â�� ���.
		if (dto == null || dto.getMode() == null || dto.getMode().equals("")) {

			req.setAttribute("mode", "created");
			return INPUT;
		}

		// �Խù� �����ϴ� ����.
		CommonDAO dao = CommonDAOImpl.getInstance();
		int maxBoardNum = dao.getIntValue("board.maxBoardNum");
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(req.getRemoteAddr());
		dto.setGroupNum(dto.getBoardNum());
		dto.setDepth(0);
		dto.setOrderNo(0);
		dto.setParent(0);

		dao.insertData("board.insertData", dto);

		return SUCCESS;

	}

	// ����Ʈ ����
	public String list() throws Exception {
		CommonDAO dao = CommonDAOImpl.getInstance();
		MyUtil myUtil = new MyUtil();
		HttpServletRequest request = ServletActionContext.getRequest();

		String cp = request.getContextPath();

		int numPerPage = 7;
		int totalPage = 0;
		int totalDataCount = 0;

		int currentPage = 1;

		if (dto.getPageNum() != null && !dto.getPageNum().equals("")) {
			currentPage = Integer.parseInt(dto.getPageNum());
		}

		if (dto.getSearchKey() == null || dto.getSearchKey().equals("")) { // �˻����� �ʾ��� ��
			dto.setSearchKey("subject");
			dto.setSearchValue("");
		}

		if (request.getMethod().equalsIgnoreCase("GET")) {
			dto.setSearchValue(URLDecoder.decode(dto.getSearchValue(), "UTF-8"));
		}

		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("searchKey", dto.getSearchKey());
		hMap.put("searchValue", dto.getSearchValue());

		totalDataCount = dao.getIntValue("board.dataCount", hMap);

		if (totalDataCount != 0) {
			totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		}

		// ������ ������ ������ �� �پ��� ��
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		int start = (currentPage - 1) * numPerPage + 1;
		int end = currentPage * numPerPage;

		hMap.put("start", start);
		hMap.put("end", end);

		List<Object> lists = (List<Object>) dao.getListData("board.listData", hMap);

		// �����͸� �Ϸù�ȣ�� �����ϱ� ���� listNum ���.
		int listNum, n = 0;
		ListIterator<Object> it = lists.listIterator();
		while (it.hasNext()) {
			BoardDTO dto = (BoardDTO) it.next();
			listNum = totalDataCount - (start + n - 1);
			dto.setListNum(listNum);
			n++;
		}

		String param = "";
		String urlList = "";
		String urlArticle = "";

		if (!dto.getSearchValue().equals("")) {
			param = "searchKey=" + dto.getSearchKey() + "&searchValue="
					+ URLEncoder.encode(dto.getSearchValue(), "UTF-8");
		}
		urlList = cp + "/bbs/list.action";
		urlArticle = cp + "/bbs/article.action?pageNum=" + currentPage;

		if (!param.equals("")) {
			urlList += "?" + param;
			urlArticle += "&" + param;
		}

		request.setAttribute("lists", lists);
		request.setAttribute("totalDataCount", totalDataCount);
		request.setAttribute("pageIndexList", myUtil.pageIndexList(currentPage, totalPage, urlList));
		request.setAttribute("urlArticle", urlArticle);

		return SUCCESS;
	}

	// �Խù� ����
	public String article() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();

		// article�� �Ѿ���� ���� �ִٸ� ���� ������.
		// �޾Ƴ��� ������ 174��(dto = (BoardDTO)dao.getReadData("board.readData", boardNum);)����
		// dto ���� �ٲ�� ��.
		String searchKey = dto.getSearchKey();
		String searchValue = dto.getSearchValue();
		String pageNum = dto.getPageNum();
		int boardNum = dto.getBoardNum();

		// �˻� �� ������
		if (searchValue == null || searchValue.equals("")) {
			searchKey = "subject";
			searchValue = "";
		}

		if (request.getMethod().equalsIgnoreCase("GET")) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}

		// ��ȸ�� ����
		dao.updateData("board.hitCountUpdate", boardNum);

		dto = (BoardDTO) dao.getReadData("board.readData", boardNum);

		if (dto == null) {
			return "read-Error";
		}

		int lineNum = dto.getContent().split("\r\n").length;
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));

		Map<String, Object> hMap = new HashMap<>();
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo", dto.getOrderNo());

		BoardDTO preDTO = (BoardDTO) dao.getReadData("board.preReadData", hMap);
		int preBoardNum = 0;
		String preSubject = "";
		if (preDTO != null) {
			preBoardNum = preDTO.getBoardNum();
			preSubject = preDTO.getSubject();
		}

		BoardDTO nextDTO = (BoardDTO) dao.getReadData("board.nextReadData", hMap);
		int nextBoardNum = 0;
		String nextSubject = "";
		if (nextDTO != null) {
			nextBoardNum = nextDTO.getBoardNum();
			nextSubject = nextDTO.getSubject();
		}
		String params = "pageNum=" + pageNum;
		if (!searchValue.equals("")) {
			params += "&searchKey=" + searchKey + "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}

		request.setAttribute("preBoardNum", preBoardNum);
		request.setAttribute("preSubject", preSubject);
		request.setAttribute("nextBoardNum", nextBoardNum);
		request.setAttribute("nextSubject", nextSubject);
		request.setAttribute("params", params);
		request.setAttribute("lineNum", lineNum);
		request.setAttribute("pageNum", pageNum);

		return SUCCESS;
	}

	// �Խù� ����
	public String updated() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();

		if (dto.getMode() == null || dto.getMode().equals("")) {
			dto = (BoardDTO) dao.getReadData("board.readData", dto.getBoardNum());

			if (dto == null)
				return "read-error";
			request.setAttribute("mode", "updated");
			request.setAttribute("pageNum", dto.getPageNum());

			return INPUT;
		}

		// ���� �Ȱ� ������Ʈ
		dao.updateData("board.updateData", dto);

		return SUCCESS;
	}

	// �亯
	public String reply() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		CommonDAO dao = CommonDAOImpl.getInstance();

		// �亯�Ϸ��� �θ� �����͸� �о��.
		if (dto == null || dto.getMode() == null || dto.getMode().equals("")) {
			dto = (BoardDTO) dao.getReadData("board.readData", dto.getBoardNum());

			if (dto == null)
				return "read-error";

			// ���� ���� ���� �� ������ �亯
			String temp = "\r\n\r\n--------------------------------------------------\r\n\r\n";
			temp += "[�亯]\r\n";
			dto.setSubject("[�亯]" + dto.getSubject());
			dto.setContent(dto.getContent() + temp);

			// �ڱ��ڽ��� ���� �Է��ؾ� �ϹǷ� �ʱ�ȭ
			dto.setName("");
			dto.setEmail("");
			dto.setPwd("");

			request.setAttribute("mode", "reply");
			request.setAttribute("pageNum", dto.getPageNum());

			return INPUT;
		}

		// �亯 ó��, OrderNo ����
		Map<String, Object> hMap = new HashMap<>();
		hMap.put("groupNum", dto.getGroupNum());
		hMap.put("orderNo", dto.getOrderNo());

		dao.updateData("board.orderNoUpdate", hMap); // groupNum�� ���� �ڱ��ڽź��� orderNo ū �Խù����� orderNo+1 ��.

		// �亯 �Է�
		int maxBoardNum = dao.getIntValue("board.maxBoardNum");
		dto.setBoardNum(maxBoardNum + 1);
		dto.setIpAddr(request.getRemoteAddr());
		dto.setDepth(dto.getDepth() + 1); // ���� �θ��� depth + 1
		dto.setOrderNo(dto.getOrderNo() + 1); // ���� �θ��� orderNo + 1

		dao.insertData("board.insertData", dto);

		return SUCCESS;
		
	}

	// �Խù� ����
	public String deleted() throws Exception {
		CommonDAO dao = CommonDAOImpl.getInstance();

		dao.deleteData("board.deleteData", dto.getBoardNum());

		return SUCCESS;
	}

}
