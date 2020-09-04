package blog.hyojin4588.pjt.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import blog.hyojin4588.pjt.Const;
import blog.hyojin4588.pjt.Utils;
import blog.hyojin4588.pjt.ViewResolver;
import blog.hyojin4588.pjt.db.BoardDAO;
import blog.hyojin4588.pjt.vo.PageVO;

@WebServlet("/boardlist")
public class BoardListSER extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession hs = request.getSession(); // 세션을 가져온다
		if (null == hs.getAttribute(Const.LOGIN_USER)) {
			response.sendRedirect("login");
			return;
		}
		
		String searchText = request.getParameter("searchText");
		if(searchText == null) {
			searchText = "";
		}
		
//		System.out.println(searchText);
				
//		List<BoardVO> list = BoardDAO.selBoard();
//		request.setAttribute("data", list);
		
		int page = Utils.getIntParameter(request, "page");
		int recordCnt = Utils.getIntParameter(request, "record_cnt");
		page = page == 0 ? 1 : page;
		recordCnt = recordCnt == 0 ? 5 : recordCnt;
		
		Integer recentRecordCnt = (Integer)hs.getAttribute("recentRecordCnt");
		if(recentRecordCnt != null && recordCnt == recentRecordCnt) {
			recordCnt = recentRecordCnt;
		}
		
		String searchType = request.getParameter("searchType");
		searchType = (searchType == null) ? "a" : searchType;
		
		PageVO param2 = new PageVO();
		param2.setI_user(Utils.userInfo(request).getI_user());
		param2.setPage(page);
		param2.setRecordCnt(recordCnt);
		param2.setSearchText("%" + searchText + "%");
		param2.setSearchType(searchType);
//		System.out.println(page);
//		System.out.println(recordCnt);
		
//		System.out.println(param2.getPage());
//		System.out.println(param2.getRecordCnt());
		List<PageVO> list2 = BoardDAO.selPaging(param2);
//		System.out.println(list2.size());
		for(PageVO item : list2) {
			if(!"".equals(searchText) && ("a".equals(searchType) || "c".equals(searchType))) {
				String title = item.getTitle();
				title = title.replace(searchText, "<span class=\"highlight\">" + searchText + "</span>");
				item.setTitle(title);
			}
		}

		request.setAttribute("page", BoardDAO.selpagingCnt(param2));
		request.setAttribute("showPage", list2);
		request.setAttribute("currentPage", page);
		
		hs.setAttribute("recentRecordCnt", recordCnt);
		hs.setAttribute("recentPage", page);
//		System.out.println("세션 저장 직전의 레코드:" + recordCnt);
//		System.out.println("세션 저장 직전의 페이지:" + page);
//		System.out.println("세션 저장 직전의 페이지 타입:" + param2.getSearchType());
//		System.out.println("세션 저장 직전의 검색 텍스트:" + param2.getSearchText());
//		System.out.println("세션 저장 직전의 총 페이지:" + BoardDAO.selpagingCnt(param2));
		
		ViewResolver.forward("board/BoardList", request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
	}

}
