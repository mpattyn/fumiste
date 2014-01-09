<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Les fumistes</title>
</head>
<body>
	<h1>Les fumistes.</h1>

	<form:form action='analyser.html'>
		<form:label path='id'>Entrez votre identifiant steam : </form:label>
		<form:input path='id' />
		<input type="submit" value="Analyser mon profil."/>
	</form:form>
</body>
</html>
