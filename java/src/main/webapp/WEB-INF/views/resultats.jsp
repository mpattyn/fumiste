<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Page de ${user_name}</title>

</head>
<body>

	<h1>Jeux possédés</h1>
	<c:forEach items="${jeux }" var="jeu">
		${jeu} <br />
	</c:forEach>

	<h1>Jeux suggérés</h1>

	<c:forEach items="${recommandations}" var="jeu">
		   ${jeu} <br />
	</c:forEach>
</body>
</html>