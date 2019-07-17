<%--
  Created by IntelliJ IDEA.
  User: couty
  Date: 04/10/2017
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>$Title$</title>
</head>
<body>
  <h1>P52 : faut pas mélanger les tolchon et les servlet !</h1>
  <p>Voici notre page d'accueil top et cool.</p>
  <h2>Sommaire :</h2>
  <ul>
    <li><a href="<c:url value="/?q=1"/>">Question 1</a></li>
    <li><a href="<c:url value="/?q=2"/>">Question 2</a></li>
    <li><a href="<c:url value="/?q=3.1"/>">Question 3.1</a></li>
    <li><a href="<c:url value="/?q=3.2.1"/>">Question 3.2</a></li>
    <li><a href="<c:url value="/?q=4"/>">Question 4</a></li>
    <li><a href="<c:url value="/?q=6"/>">Question 6</a></li>
  </ul>
</body>
</html>
