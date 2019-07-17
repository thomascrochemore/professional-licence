<%--
  Created by IntelliJ IDEA.
  User: couty
  Date: 13/10/2017
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <p>Bonjour <c:out value="${username}" default="invité"/> !</p>
    <form method="post" action="<c:url value="/?q=6"/>">
        <input type="hidden" name="action" value="add"/>
        <input type="submit" value="Ajouter un cookie"/>
    </form>
    <form method="post" action="<c:url value="/?q=6"/>">
        <input type="hidden" name="action" value="delete"/>
        <input type="submit" value="Détruire le cookie"/>
    </form>
</body>
</html>
