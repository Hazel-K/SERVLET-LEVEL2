package blog.hyojin4588.pjt.user;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import blog.hyojin4588.pjt.Utils;
import blog.hyojin4588.pjt.ViewResolver;
import blog.hyojin4588.pjt.db.UserDAO;
import blog.hyojin4588.pjt.vo.UserVO;

@WebServlet("/profile")
public class UserSER extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 프로필 화면 (나의 프로필 이미지, 이미지 변경 가능한 화면), 업로드는 무조건 post로 동작
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int i_user = Utils.userInfo(request).getI_user();
		UserVO param = UserDAO.selUser(i_user);
		request.setAttribute("data", param);
		ViewResolver.forward("user/Profile", request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserVO loginUser = Utils.userInfo(request);
		String savePath = getServletContext().getRealPath("img") + "/user/" + loginUser.getI_user();
//		System.out.println("savePath : " + savePath);
		int maxFileSize = 10485760; // 1024 * 1024 * 10 (10mb), maximum file size
		String fileNm = "";
		String saveFileNm = null;
//		String originFileNm = "";

		File directory = new File(savePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		try {
			MultipartRequest mr = new MultipartRequest(request, savePath, maxFileSize, "UTF-8",
					new DefaultFileRenamePolicy());
			Enumeration files = mr.getFileNames();

			if (files.hasMoreElements()) {
				String key = (String) files.nextElement();
				fileNm = mr.getFilesystemName(key);
//				originFileNm = mr.getOriginalFileName(key); // 원본 파일 이름 가져오기
//				System.out.println("key : " + key);
//				System.out.println("fileNm : " + fileNm);
//				System.out.println("originFileNm : " + originFileNm);
				File oldFile = new File(savePath + "/" + fileNm);
				int exeIndex = fileNm.lastIndexOf(".");
				String ext = fileNm.substring(exeIndex);
				saveFileNm = UUID.randomUUID() + ext;
				File newFile = new File(savePath + "/" + saveFileNm);
//				System.out.println(newFile);
				oldFile.renameTo(newFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (saveFileNm != null) {
			UserVO param = new UserVO();
			param.setProfile_img(saveFileNm);
			param.setI_user(loginUser.getI_user()); // id를 쓰지 않는 이유는 속도가 정수가 더 빨라서

			int result = UserDAO.updUser(param);
//			System.out.println(result + "개 유저 업데이트 완료");
		}

		response.sendRedirect("profile");
	}

}
