<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>user</title>
</head>
<body>
<script>
    alert("로그아웃 되었습니다.");
    location.href = "${pageContext.request.contextPath}/main.do";
</script>
</body>
</html>