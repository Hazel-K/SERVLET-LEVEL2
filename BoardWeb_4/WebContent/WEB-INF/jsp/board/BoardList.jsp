<%@page import="com.sun.org.apache.xpath.internal.operations.Div"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="blog.hyojin4588.pjt.vo.PageVO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<style>
span {
	cursor: pointer;
	margin-right: 10px;
}

.highlight {
	color: red;
	margin: 0;
}

#likeListContainer {
	border: 1px solid darkgray;
	position: absolute;
	width: auto;
	height: auto;
	overflow-y: auto;
	background: white;
	opacity: 0;
}
.pImg {
	width: 30px;
	height: 30px;
	border-radius: 50%;
}
</style>
</head>
<body>
	<div>${login_user.getUser_nm()}님환영합니다.</div>
	<span onclick="location.href='profile'">프로필</span>
	<div>
		<span onclick="location.href='regmod'">글쓰기</span> <span
			onclick="location.href='logout'">로그아웃</span>
	</div>
	<h1>게시판</h1>
	<table>
		<tr>
			<th>글번호</th>
			<th>작성자</th>
			<th>제목</th>
			<th>조회수</th>
			<th>좋아요</th>
			<th>게시일</th>
		</tr>
		<c:if test="${empty showPage}">
			<tr>
				<td colspan=5 style="text-align: center;">작성된 게시글이 없습니다.</td>
			</tr>
		</c:if>
		<c:forEach items="${showPage}" var="item">
			<tr>
				<td
					onclick="location.href='detail?i_board=${item.i_board}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">${item.i_board}</td>
				<td
					onclick="location.href='detail?i_board=${item.i_board}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">
					<c:choose>
						<c:when test="${item.profile_img != null}">
							<img width="30px" height="30px"
								src="/img/user/${item.i_user}/${item.profile_img}">
							<!-- 세션과 서블릿에 설정한 attribute 사용 -->
						</c:when>
						<c:otherwise>
							<img width="30px" height="30px"
								src="${'/img/default_profile.jpg'}">
							<!-- web-inf 폴더 안에 있는거 사용 -->
						</c:otherwise>
					</c:choose> ${item.u_nm}
				</td>
				<td
					onclick="location.href='detail?i_board=${item.i_board}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">${item.title}
					<c:if test="${item.cmt_cnt != 0}">&#40;${item.cmt_cnt}&#41;</c:if>
				</td>
				<td
					onclick="location.href='detail?i_board=${item.i_board}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">${item.hits}</td>
				<td><span
					onclick="getLikeList(${item.i_board}, ${item.like_cnt}, this)">${item.like_cnt}</span></td>
				<td
					onclick="location.href='detail?i_board=${item.i_board}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">${item.r_dt}</td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<c:forEach var="cnt" begin="1" end="${page}" step="1">
			<c:if test="${cnt == currentPage}">
				<span style="color: red;">${cnt}</span>
			</c:if>
			<c:if test="${cnt != currentPage}">
				<span
					onclick="location.href='boardlist?page=${cnt == null ? 1 : cnt}&record_cnt=${param.record_cnt == null ? 5 : param.record_cnt}&searchText=${param.searchText}&searchType=${param.searchType == null ? 'a' : param.searchType}'">${cnt}</span>
			</c:if>
		</c:forEach>
	</div>
	<div>
		<form action="/boardlist" method="get" id="selFrm">
			<input type="hidden" name="page" value="1"> <input
				type="hidden" name="searchText" value="${param.searchText}">
			<input type="hidden" name="searchType"
				value="${param.searchType == null ? 'a' : param.searchType}">
			레코드 수 <select name="record_cnt" onchange="selFrm.submit()">
				<c:forEach begin="5" end="15" step="5" var="item">
					<c:choose>
						<c:when test="${param.record_cnt == item}">
							<option value="${item}" selected>${item}개</option>
						</c:when>
						<c:otherwise>
							<option value="${item}">${item}개</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</form>
	</div>
	<div>
		<form action="/boardlist" id="searchFrm" method="get">
			<select name="searchType">
				<option value="a" ${param.searchType == 'a' ? 'selected' : ''}>제목</option>
				<option value="b" ${param.searchType == 'b' ? 'selected' : ''}>내용</option>
				<option value="c" ${param.searchType == 'c' ? 'selected' : ''}>제목+내용</option>
			</select> <input type="search" name="searchText" value="${param.searchText}">
			<span onclick="searchFrm.submit()">검색</span>
		</form>
	</div>
	<div id="likeListContainer"></div>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js">
		// 스트림 라이브러리
	</script>
	<script type="text/javascript">
	
	let beforeI_board = 0; 
		
	function getLikeList(i_board, cnt, span) {
		if(cnt == 0) { return };

		if(beforeI_board == i_board) {
			likeListContainer.style.display = 'none';
			return;
		} else {
			beforeI_board = i_board;
			likeListContainer.style.display = 'unset';
		}
		const locationX = window.scrollX + span.getBoundingClientRect().left
		const locationY = window.scrollY + span.getBoundingClientRect().top + 30
//		console.log(locationX + ":" + locationY)

		const target = event.target;
      
      	//likeListContainer.innerHTML = "";
         
      	//likeListContainer.style.left = target.getBoundingClientRect().right + window.pageXOffset;
      	//likeListContainer.style.top = target.getBoundingClientRect().bottom + window.pageYOffset;
		
		likeListContainer.style.left = `\${locationX}px`;
		likeListContainer.style.top = `\${locationY}px`;
		
		likeListContainer.style.opacity = 1;
		likeListContainer.innerHTML = "";
		
		axios.get('/like', {
			params: {
				i_board : i_board // key와 변수명이 같을 때 i_board만 적어도 value가 나옴. 원형은 i_board: i_board
			}
		}).then(function(res) {
			// console.log(res); // '/like' 경로에 해당하는 SERVLET의 데이터를 콘솔에 출력
			if(res.data.length > 0) {
				for(let i = 0; i < res.data.length; i++) {
					const result = makeLikeUser(res.data[i]);
					likeListContainer.innerHTML += result;
				}
			}
		})
	}
	
	function makeLikeUser(one) {
		const img = one.profile_img == null ? 
				'<image class="pImg" src="/img/default_profile.jpg">'
				: 
				`<img class ="pImg" src="/img/user/\${one.i_user}/\${one.profile_img}">`
		
		const ele = `<div class="likeItemContainer">
			<div class="profileContainer">
			<div class="profile">
					\${img}
				</div>
				<div class="nm">\${one.u_nm}</div>
			</div>
		</div>`
		
		return ele
	}
	</script>
</body>
</html>