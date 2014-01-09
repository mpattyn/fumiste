<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<a href = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=${key}&steamids=76561197960435530">Voir un profil !</a>
<a href="http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=${key}&steamid=76561197960434622&format=json">Voir les jeux</a>

	<form:form action='analyser.html'>
		<form:input path='id'/>
		<form:password path='pwd' />
		<input type="submit" />
	</form:form>
</body>
</html>
