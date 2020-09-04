package blog.hyojin4588.pjt.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import blog.hyojin4588.pjt.Utils;
import blog.hyojin4588.pjt.db.BoardCmtDAO;
import blog.hyojin4588.pjt.vo.BoardCmtVO;

@WebServlet("/coment")
public class BoardComentSER extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i_cmt = Utils.getIntParameter(request, "i_cmt");
		String strI_board = request.getParameter("i_board");
		int i_board = Utils.parseStringToInt(strI_board);
		int i_user = Utils.userInfo(request).getI_user();
		
		int page = Utils.parseStringToInt(request.getParameter("page"));
		int record_cnt = Utils.parseStringToInt(request.getParameter("record_cnt"));
		String searchText = request.getParameter("searchText");
		String searchType = request.getParameter("searchType");
		boolean isPage = page == 0;
		boolean isSearchText = "".equals(searchText);
//		if(!isSearchText) {
//			searchText = URLEncoder.encode(searchText,"UTF-8");			
//		}
		boolean isSearchType = "".equals(searchType);
//		if(!isSearchType) {
//			searchType = URLEncoder.encode(searchType,"UTF-8");			
//		}
		boolean[] isArr = {isPage, isSearchText, isSearchType};
		String[] strArr = {"page=" + page, "searchText=" + searchText, "searchType=" + searchType};
		StringBuilder allQuery = new StringBuilder("");
		for(int i = 0; i < isArr.length; i++) {
			if(!isArr[i]) {
				allQuery.append("&");
				allQuery.append(strArr[i]);
			}
		}
		
		BoardCmtVO param = new BoardCmtVO();
		param.setI_board(i_board);
		param.setI_cmt(i_cmt);
		param.setI_user(i_user);
		
//		System.out.println(i_board+ "," + i_cmt);
		
		int result = 0;
		result = BoardCmtDAO.delCmt(param);
//		System.out.println(result);
		response.sendRedirect("detail?i_board=" + i_board + allQuery.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strI_cmt = request.getParameter("i_cmt");
		int i_cmt = Utils.parseStringToInt(strI_cmt);
		String cmt = request.getParameter("cmt");
		String strI_board = request.getParameter("i_board");
		int i_board = Utils.parseStringToInt(strI_board);
		int i_user = Utils.userInfo(request).getI_user();
		
		int result = 0;
		
		BoardCmtVO param = new BoardCmtVO();
		param.setI_cmt(i_cmt);
		param.setI_board(i_board);
		param.setCmt(cmt);
		param.setI_user(i_user);
		
		switch(strI_cmt) {
			case "0":
				result = BoardCmtDAO.insCmt(param);
				break;
			default:
				result = BoardCmtDAO.updCmt(param);
				break;
		}
		
		int page = Utils.parseStringToInt(request.getParameter("page"));
		int record_cnt = Utils.parseStringToInt(request.getParameter("record_cnt"));
		String searchText = request.getParameter("searchText");
		String searchType = request.getParameter("searchType");
		boolean isPage = page == 0;
		boolean isSearchText = "".equals(searchText);
//		if(!isSearchText) {
//			searchText = URLEncoder.encode(searchText,"UTF-8");			
//		}
		boolean isSearchType = "".equals(searchType);
//		if(!isSearchType) {
//			searchType = URLEncoder.encode(searchType,"UTF-8");			
//		}
		boolean[] isArr = {isPage, isSearchText, isSearchType};
		String[] strArr = {"page=" + page, "searchText=" + searchText, "searchType=" + searchType};
		StringBuilder allQuery = new StringBuilder("");
		for(int i = 0; i < isArr.length; i++) {
			if(!isArr[i]) {
				allQuery.append("&");
				allQuery.append(strArr[i]);
			}
		}
		
		response.sendRedirect("detail?i_board=" + i_board + allQuery.toString());
	}
	
}
