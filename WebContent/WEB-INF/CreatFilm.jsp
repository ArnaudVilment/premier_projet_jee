<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
        <meta charset="utf-8" />
        <title>Tests JDBC</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/style.css"/>" />
    </head>
    <body>
        
        <FORM METHOD="POST" ACTION="creatFilm">
        
        	<table border="1" cellpadding="4" cellspacing="0" width="30%" align=center >
				<tr bgcolor="#A6A5C2">
					<td><h1>Création d'un film</h1></td><td></td>
				<tr>
					<td align="center">Numéro de film :</Td><td><INPUT TYPE=TEXT NAME="numFilm"></td>
				</tr>
				<tr>
					<td align="center">Titre :</td><td><INPUT TYPE=TEXT NAME="titre"></td>
				</Tr>
				<tr>
					<td align="center">Durée :</td><td><INPUT TYPE=TEXT NAME="duree"></td>
				</Tr>
				<tr>
					<td align="center">Sortie :</td><td><INPUT TYPE=TEXT NAME="sortie"></td>
				</Tr>
				<tr>
					<td align="center">Genre :</td><td><INPUT TYPE=TEXT NAME="idGenre"></td>
				</Tr>
				<tr>
					<td align="center"><INPUT TYPE=SUBMIT VALUE="Créer"></td>
			</table>
			
			<%-- Affichage de la chaîne "message" transmise par la servlet --%>
        	<center><p class="info">${ message }  ${ film.titre }</p></center>
			
		</FORM>
    </body>
</html>