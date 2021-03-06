<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*,lee.*"%>
<%@ page import="java.util.*,content.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <link href="${pageContext.request.contextPath}/css/BoardList.css" rel="stylesheet">

	 <!-- HEADER -->
	 <%@include file ="/View/Common/header.jsp" %>


<title>자유게시판</title>


<body>

      <br><br><br><br>
         <center>
         <p class="jb-xx-large">게시판</p>
         </center>

		<!-- 게시판 리스트  -->
		<table class="table table-bordered table-hover">
			<colgroup>
				<col width="200px" height="100px">
			</colgroup>
			<thead>
				<tr>
					<td class="align-middle  text-align:center">번호</td>
					<td>글제목</td>
					<td>작성자</td>
					<td>게시글날짜</td>
					<td>조회수</td>
				</tr>
			</thead>
			<tbody>
				<!-- 추가  -->
				<%
				ArrayList list = (ArrayList) request.getAttribute("list");//${list}
				if (list != null) {//데이터가 존재한다면
					Iterator iter = list.iterator();
					while (iter.hasNext()) {//꺼낼 데이터가 존재한다면
						//Board data=(Board)iter.next();//Object ->(Board)형변환
						BoardCommand data = (BoardCommand) iter.next();
						//-------------------------------------------------------
						int num = data.getNum();
						String title = data.getTitle();
						String author = data.getAuthor();
						//String writeday=data.getDate();//날짜출력 ->10글자뿐만 출력하라
						String writeday = data.getWriteday();
						//------------------------------------------------
						int readcnt = data.getReadcnt();//조회수
				%>

				<tr>
					<td align="center"><%=num%></td>
					<td><a href="retrieve.do?num=<%=num%>"><%=title%></a></td>
					<td><%=author%></td>
					<td><%=writeday.substring(0, 10)%></td>
					<td><%=readcnt%></td>
				</tr>

				<%
				} //end while
				} //end if
				%>
			</tbody>
		</table>

		<!-- 게시판리스트 끝  -->

		<!-- 버튼 -->
		<div class="text-center">
			<a href="View/Board/write.do" class="btn btn-primary" role="button">글작성</a>
			<!--  <a href="update.jsp" class="btn btn-primary" role="button">글수정</a> -->
			<a href="Delete.html" class="btn btn-primary" role="button"
				onclick="alert('삭제페이지로 이동합니다')">글삭제</a>
		</div>



		<!-- 페이징 (이전,다음 게시물 이동) -->
		<!--paginate -->
<div class="paginate">
        <div class="paging">
            <a class="direction prev" href="javascript:void(0);"
                onclick="movePage(1,${pagination.cntPerPage},${pagination.pageSize});">
                &lt;&lt; </a> <a class="direction prev" href="javascript:void(0);"
                onclick="movePage(${pagination.currentPage}<c:if test="${pagination.hasPreviousPage == true}">-1</c:if>,${pagination.cntPerPage},${pagination.pageSize});">
                &lt; </a>

            <c:forEach begin="${pagination.firstPage}"
                end="${pagination.lastPage}" var="idx">
                <a
                    style="color:<c:out value="${pagination.currentPage == idx ? '#cc0000; font-weight:700; margin-bottom: 2px;' : ''}"/> "
                    href="javascript:void(0);"
                    onclick="movePage(${idx},${pagination.cntPerPage},${pagination.pageSize});"><c:out
                        value="${idx}" /></a>
            </c:forEach>
            <a class="direction next" href="javascript:void(0);"
                onclick="movePage(${pagination.currentPage}<c:if test="${pagination.hasNextPage == true}">+1</c:if>,${pagination.cntPerPage},${pagination.pageSize});">
                &gt; </a> <a class="direction next" href="javascript:void(0);"
                onclick="movePage(${pagination.totalRecordCount},${pagination.cntPerPage},${pagination.pageSize});">
                &gt;&gt; </a>
        </div>
    </div>
<br>
<div class="bottom">
        <div class="bottom-left">
            <select id="cntSelectBox" name="cntSelectBox"
                onchange="changeSelectBox(${pagination.currentPage},${pagination.cntPerPage},${pagination.pageSize});"
                class="form-control" style="width: 100px;">
                <option value="10"
                    <c:if test="${pagination.cntPerPage == '10'}">selected</c:if>>10개씩</option>
                <option value="20"
                    <c:if test="${pagination.cntPerPage == '20'}">selected</c:if>>20개씩</option>
                <option value="30"
                    <c:if test="${pagination.cntPerPage == '30'}">selected</c:if>>30개씩</option>
            </select>
        </div>
    </div>
    <!-- /paginate -->

		<!-- 검색 기능 -->
		<center>
     <tr><td colspan="5" align="center">
		<form action="search.do">
		   <select name="searchName" size="1">
	          <option value="author">작성자</option>
    	          <option value="title">제목</option>
                 </select>
		   <input type="text" name="searchValue"><input type="submit" value="검색">
		</form>
	</td></tr>
	</center>




	<script>
//10,20,30개씩 selectBox 클릭 이벤트
function changeSelectBox(currentPage, cntPerPage, pageSize){
    var selectValue = $("#cntSelectBox").children("option:selected").val();
    movePage(currentPage, selectValue, pageSize);

}

//페이지 이동
function movePage(currentPage, cntPerPage, pageSize){

    var url = "${pageContext.request.contextPath}/list.do";
    url = url + "?currentPage="+currentPage;
    url = url + "&cntPerPage="+cntPerPage;
    url = url + "&pageSize="+pageSize;

    location.href=url;
}

</script>
	<!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
	<script src="../js/bootstrap.min.js"></script>
</body>


	<!-- FOOTER -->
	<%@include file ="/View/Common/footer.jsp" %>

