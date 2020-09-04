<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필</title>
</head>
<body>
	<h1>프로필</h1>
	<div>${data.user_id}</div>
	<div>${data.user_nm}</div>
	<div>
		<c:choose>
			<c:when test="${data.profile_img != null}">
				<img width="100px" height="100px" src="/img/user/${login_user.i_user}/${data.profile_img}"> <!-- 세션과 서블릿에 설정한 attribute 사용 -->
			</c:when>
			<c:otherwise>
				<img width="100px" height="100px" src="${'/img/default_profile.jpg'}"> <!-- web-inf 폴더 안에 있는거 사용 -->
			</c:otherwise>
		</c:choose>
	</div>
	<div>${data.email}</div>
	
	<div>
		<form action="/profile" method="post" enctype="multipart/form-data">
			<div>
				<label>프로필 이미지 : <input type="file" name="profile_img" accept="image/*"></label>
				<input type="submit" value="업로드">
			</div>
		</form>
	</div>
	<div>
	<span onclick="location.href='change'">회원정보 변경</span>
	<span onclick="location.href='boardlist'">리스트로</span>
	</div>
	<script type="text/javascript">
		function init() {
			let proc = ${param.proc};
			if (proc == 1) {
				alert('회원 정보가 수정되었습니다.');
			}
		}
		
		init();
	</script>
</body>
</html>