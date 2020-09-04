package blog.hyojin4588.pjt.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import blog.hyojin4588.pjt.Const;
import blog.hyojin4588.pjt.Utils;
import blog.hyojin4588.pjt.ViewResolver;
import blog.hyojin4588.pjt.db.UserDAO;
import blog.hyojin4588.pjt.vo.UserVO;

@WebServlet("/change")
public class ChangeInfoSER extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ViewResolver.forwardLoginChk("user/ChangeInfo", request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String pw = request.getParameter("pw");
		String encrypt_pw = Utils.encryptString(pw);

		HttpSession hs = request.getSession();
		UserVO user = (UserVO)hs.getAttribute(Const.LOGIN_USER);
		user.setUser_pw(encrypt_pw);
		
		switch(type) {
		case "1": // 현재 비밀번호 확인
			int result = UserDAO.login(user);
//			System.out.println(result);
			if(result == 1) {
				request.setAttribute("isAuth", true);
			} else {
				request.setAttribute("msg", "비밀번호를 확인해주세요");
			}
			doGet(request, response);
			break;
		case "2": // 비밀번호 변경
			int result2 = UserDAO.updUser(user);
//			System.out.println(result2);
			response.sendRedirect("profile?proc=1");
			break;
		}
	}

}
