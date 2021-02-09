<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 8:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="page" value="/pages/registration.jsp" scope="request"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core.css">
    <title>Login</title>
</head>
<body>
    <div class="user">
        <jsp:include page="/pages/module/header.jsp"/>
        <form class="form" name="registerForm" method="POST" action="controller">
            <input type="hidden" name="command" value="register"/>
            <div class="form__group">
                <input type="text" class="form__input" name="login" placeholder=<fmt:message key="label.login" bundle="${ rb }" /> pattern="[A-Za-zА-Яа-яЁё0-9]{4,}" value=""/>
            </div>
            <div class="form__group">
                <input type="email" class="form__input" name="email" value="" placeholder=<fmt:message key="label.email" bundle="${ rb }" /> pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"/>
            </div>
            <div class="form__group">
                <input type="text" class="form__input" name="firstName" value="" placeholder=<fmt:message key="label.fname" bundle="${ rb }" /> pattern="[A-Za-zА-Яа-яЁё]{4,}"/>
            </div>
            <div class="form__group">
                <input type="text" class="form__input" name="secondName" value="" placeholder=<fmt:message key="label.sname" bundle="${ rb }" /> pattern="[A-Za-zА-Яа-яЁё]{4,}"/>
            </div>
            <div class="form__group">
                <input type="password" class="form__input" name="password" placeholder=<fmt:message key="label.password" bundle="${ rb }" /> value="" pattern=".{8,}" title="Eight or more characters"/>
            </div>
            <div class="form__group">
                <input type="password" class="form__input" name="passwordRepeat" placeholder=<fmt:message key="label.password" bundle="${ rb }" /> value="" pattern=".{8,}" title="Eight or more characters"/>
            </div>
            <input type="submit" class="btn" value=<fmt:message key="label.submit" bundle="${ rb }" />>
        </form>
        <a href="controller?command=logout" class="user__title"><fmt:message key="label.login" bundle="${ rb }" /></a>
        <br/>
        ${error}
    </div>
</body>
</html>
