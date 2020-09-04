package blog.hyojin4588.pjt.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import blog.hyojin4588.pjt.Utils;
import blog.hyojin4588.pjt.db.BoardDAO;
import blog.hyojin4588.pjt.vo.PageVO;

@WebServlet("/like")
public class BoardLikeSER extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// 리스트 가져오기
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i_board = Utils.getIntParameter(request, "i_board");
		System.out.println("i_board : " + i_board);
		
		PageVO param = new PageVO();
		param.setI_board(i_board);
		List<PageVO> likeList = BoardDAO.selBoardLikeList(param); // param을 이용하여 likeList 목록을 불러옴
		Gson gson = new Gson(); // gson 객체를 생성
		String json = gson.toJson(likeList); // 생성한 gson 객체를 통해 likeList를 Json형태로 parse
		System.out.println("json : " + json);
		
		response.setCharacterEncoding("UTF-8"); // 가져오는 문자열의 인코딩 값을 UTF-8로 설정
		response.setContentType("application/json"); // 가져오는 문장들의 타입을 JSON으로 설정
		// PrintWriter 출력스트림을 이용한 문자열 출력은 Browser에서 일반 문자가 아닌 HTML로 인식하므로 JSON으로 PARSE하는 것이 불가능해서 이러한 설정을 하는 것
		PrintWriter out = response.getWriter(); // response에 대응하는 출력 스트림을 설정
		out.print(json); // 출력 스트림 out을 이용하여 Object(name: 3)을 비동기 형식으로 출력
	}
	
	// 좋아요 처리
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
