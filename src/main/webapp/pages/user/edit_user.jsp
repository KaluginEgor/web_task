<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 4/15/2021
  Time: 7:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<c:set var="page" value="/pages/user/edit_user.jsp" scope="session"/>
<jsp:useBean id="someUser" class="com.epam.project.model.entity.User" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title><fmt:message key="label.edit.user"/></title>
</head>
<body class="home">
<c:import url="/pages/module/header.jsp"/>
<div class="edit-page">

    <div class="block">
        <form action="<c:url value="/controller"/>" enctype="multipart/form-data" method="POST">
            <input type="hidden" name="command" value="upload_picture">
            <input type="file" accept="image/*" name="content" height="130">
            <input type="submit" value="<fmt:message key="label.file.upload"/>">
        </form>
    </div>

    <form class="edit-form" action="<c:url value="/controller"/>" method="POST">
        <input type="hidden" name="command" value="update_user"/>
        <input type="hidden" name="userId" value="${someUser.id}">

        <c:choose>
            <c:when test="${empty sessionScope.newPicture}">
                <div class="poster">
                    <img src="${pageContext.request.contextPath}/picture?currentPicture=${someUser.picture}">
                    <input type="hidden" name="picture" value="${someUser.picture}">
                </div>
            </c:when>
            <c:otherwise>
                <div class="poster">
                    <img src="${pageContext.request.contextPath}/picture?currentPicture=${sessionScope.newPicture}">
                    <input type="hidden" name="picture" value="${sessionScope.newPicture}">
                </div>
            </c:otherwise>
        </c:choose>

        <div class="block">
            <label for="first-name"><fmt:message key="user.name.first"/></label><br/>
            <input type="text" id="first-name" class="first-name" name="firstName" pattern="[A-Za-zА-Яа-яЁё]{1,40}" placeholder="<fmt:message key="user.name.first"/>"
                    <c:choose>
                        <c:when test="${not empty sessionScope.firstName}">
                            value="${sessionScope.firstName}"
                        </c:when>
                        <c:when test="${not empty someUser.firstName}">
                            value="${someUser.firstName}"
                        </c:when>
                    </c:choose>
            />
        </div>

        <div class="block">
            <label for="last-name"><fmt:message key="user.name.second"/></label><br/>
            <input type="text" id="last-name" class="last-name" name="secondName" pattern="[A-Za-zА-Яа-яЁё]{1,40}" placeholder="<fmt:message key="user.name.second"/>"
                    <c:choose>
                        <c:when test="${not empty sessionScope.secondName}">
                            value="${sessionScope.secondName}"
                        </c:when>
                        <c:when test="${not empty someUser.secondName}">
                            value="${someUser.secondName}"
                        </c:when>
                    </c:choose>
            />
        </div>

        <div class="block">
            <label for="email"><fmt:message key="user.email"/></label><br/>
            <input type="email" id="email" class="email" name="email"
                   pattern="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"
                    <c:choose>
                        <c:when test="${not empty sessionScope.email}">
                            value="${sessionScope.email}"
                        </c:when>
                        <c:when test="${not empty someUser.email}">
                            value="${someUser.email}"
                        </c:when>
                    </c:choose>
            />
        </div>

        <div class="block">
            <input type="submit" class="edit-btn" value="<fmt:message key="label.submit"/>">
        </div>
    </form>
    <c:import url="/pages/module/messages.jsp"/>
    <div class="block">
        <form action="<c:url value="/controller"/>" method="POST">
            <input type="hidden" name="command" value="open_user_page"/>
            <input type="hidden" name="userId" value="${someUser.id}"/>
            <input type="submit" value="back"/>
            <br>
        </form>
    </div>
</div>
</body>
</html>
<c:remove var="firstName"/>
<c:remove var="secondName"/>
<c:remove var="email"/>
<c:remove var="newPicture"/>
<c:remove var="errorMessage"/>
<c:remove var="validationErrors"/>
<c:remove var="confirmMessage"/>
