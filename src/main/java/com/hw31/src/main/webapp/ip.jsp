<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>IP</title>
</head>
<body>
<table border="1" bgcolor="#7fffd4">
    <thead>
    <tr>
        <th>IP</th>
        <th>User-Agent</th>
        <th>Time</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visitors}" var="visitor">
        <tr>
            <td><strong>${visitor.ip}</strong></td>
            <td><strong>${visitor.userAgent}</strong></td>
            <td>${visitor.created}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>