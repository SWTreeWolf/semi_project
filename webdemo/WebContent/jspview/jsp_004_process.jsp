<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js">
	
</script>
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");
		String fname = request.getParameter("fname");
	%>
	
	<p><%=fname%></p>
</body>
</html>