<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 5/1/2021
  Time: 4:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/messages.css" />
</head>
<body>
<div class="block">
    <br/>
    <c:forEach var="validationException" items="${sessionScope.validationErrors}">
        <h4>${validationException}</h4>
    </c:forEach>
    <br/>
    <c:if test="${not empty sessionScope.errorMessage}">
        <h4>${sessionScope.errorMessage}</h4>
    </c:if>
    <br/>
    <c:if test="${not empty sessionScope.confirmMessage}">
        <h4>${sessionScope.confirmMessage}</h4>
    </c:if>
</div>
</body>
</html>
