<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'ru_RU'}" scope="session" />
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core.css">
</head>
<body>
<div class="user__header">
    <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3219/logo.svg" alt="" />
    <form name="loginForm" method="POST" action="controller">
        <input type="hidden" name="command" value="change_language">
        <input type="hidden" name="page" value="${ requestScope.page }">
        <select name="lang" onchange="submit()">
            <option value="en_US" <c:if test="${lang eq 'en_US'}">selected</c:if>>English</option>
            <option value="ru_RU" <c:if test="${lang eq 'ru_RU'}">selected</c:if>>Русский</option>
        </select>
    </form>
</div>
</body>
</html>
