<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 4/26/2021
  Time: 6:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/500.css">
    <title>500</title>
</head>
<body>
<div class="wrapper">
    <div class="box">
        <h1>500</h1>
        <p>Sorry, it's me, not you.</p>
        <p>&#58;&#40;</p>
        <c:if test="${not empty sessionScope.errorMessage}">
            <p>${sessionScope.errorMessage}</p>
        </c:if>
        <p><a href="/">Let me try again!</a></p>
    </div>
</div>
</body>
</html>
