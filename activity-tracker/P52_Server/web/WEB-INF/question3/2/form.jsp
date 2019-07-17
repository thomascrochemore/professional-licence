<%--
  Created by IntelliJ IDEA.
  User: couty
  Date: 08/10/2017
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${!empty error}">
    <p><c:out value="${error}"/></p>
</c:if>
<form action="<c:url value="/?q=3.2.1"/>" method="post">
    <p>
        <label for="username">Nom d'utilisateur : </label>
        <input id="username" name="username" type="text">
    </p>
    <p>
        <label for="password">Mot de passe : </label>
        <input id="password" name="password" type="password">
    </p>
    <p><input type="submit" value="Envoyer"/></p>
</form>
</body>
</html>

