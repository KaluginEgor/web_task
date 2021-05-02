
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 3:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<html>
<head>
    <title>404</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/404.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@600;900&display=swap">
    <script src="https://kit.fontawesome.com/4b9ba14b0f.js" crossorigin="anonymous"></script>
</head>
</head>
<body>
<div class="mainbox">
    <div class="err">4</div>
    <i class="far fa-question-circle fa-spin"></i>
    <div class="err2">4</div>
    <div class="msg">
        <br/>
        <c:if test="${not empty sessionScope.errorMessage}">
            <h4>${sessionScope.errorMessage}</h4>
        </c:if>
        <br/>
        <a href="${pageContext.request.contextPath}/pages/common/main.jsp"><fmt:message key="label.home"/></a>
    </div>
</div>
</body>
</html>
<c:remove var="validationErrors"/>
<c:remove var="confirmMessage"/>
<c:remove var="errorMessage"/>
