<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div style="color: red;">${msg}</div>
	<c:if test="${isAuth == false || isAuth == null}"> <!-- 현재 비밀번호 확인 -->
		<div>
			<form action="/change" method="post">
				<input type="hidden" name="type" value="1">
				<label><input type="password" name="pw" placeholder="현재 비밀번호"></label>
				<div>
				<input type="submit" value="입력">
				<button onclick="goToProfile()">프로필로</button>
				</div>
			</form>
		</div>
	</c:if>
	<c:if test="${isAuth == true}"> <!-- 이전 비밀번호 변경 -->
		<form id="changePwFrm" action="/change" method="post" onsubmit="return chkChangePw()">
			<input type="hidden" name="type" value="2">
			<div>
				<label><input type="password" name="pw" placeholder="변경할 비밀번호"></label>
			</div>
			<div>
				<label><input type="password" name="repw" placeholder="변경할 비밀번호 확인"></label>
			</div>
			<div>
				<input type="submit" value="확인"><button onclick="goToProfile()">프로필로</button>
			</div>
		</form>
	</c:if>
	<script type="text/javascript">
		function chkChangePw() {
			// console.log(changePwFrm.pw.value);
			// console.log(changePwFrm.repw.value);
			let pw = changePwFrm.pw.value;
			let repw = changePwFrm.repw.value;
			
			if (pw.length == 0 || repw.length == 0) {
				alert('비밀번호를 입력해주세요.');
				changePwFrm.pw.value == '' ? changePwFrm.pw.focus() : changePwFrm.repw.focus();
			} else if(pw == repw) {
				return true;
			} else {
				alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
				changePwFrm.pw.value = '';
				changePwFrm.repw.value = '';
			}
			return false;
		}
		
		function goToProfile() {
			event.preventDefault();
			location.href='profile';
		}
	</script>
</body>
</html>