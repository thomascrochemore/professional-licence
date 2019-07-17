<%--
  Created by IntelliJ IDEA.
  User: couty
  Date: 12/10/2017
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <select>
        <c:forEach items="${users}" var="user">
            <option value="${user.username}">
                <c:out value="${user.firstName} ${user.lastName}"/>
            </option>
        </c:forEach>
    </select>
</body>
</html>
