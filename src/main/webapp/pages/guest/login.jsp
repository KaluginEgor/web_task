<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 3:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<c:set var="page" value="/pages/guest/login.jsp" scope="session"/>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/core.css">
    <link href='http://fonts.googleapis.com/css?family=Cookie' rel='stylesheet' type='text/css'>
    <title><fmt:message key="label.login" /></title>
</head>
<body>
<c:import url="/pages/module/header.jsp"/>
<c:import url="/pages/module/messages.jsp"/>
<div class="user">
    <form class="form" name="loginForm" method="POST" action="<c:url value="/controller"/>">
        <input type="hidden" name="command" value="login" />
        <div class="form__group">
            <input type="text" class="form__input" name="login" value="${sessionScope.login}" placeholder=<fmt:message key="label.login"/> pattern="[\wА-Яа-яЁё0-9_.]{1,40}" value=""/>
        </div>
        <div class="form__group">
            <input type="password" class="form__input" name="password" placeholder=<fmt:message key="user.password"/> pattern="\w{8,100}" title="Eight or more characters"/>
        </div>
        <input type="submit" class="btn" value=<fmt:message key="label.login"/>>
    </form>
    <br/>
</div>
</body>
</html>
<c:remove var="login"/>
<c:remove var="validationErrors"/>
<c:remove var="errorMessage"/>
<c:remove var="confirmMessage"/>
