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
<fmt:setBundle basename="pagecontent" var="rb" />
<c:set var="page" value="/pages/edit_user.jsp" scope="session"/>
<jsp:useBean id="someUser" class="com.example.demo_web.model.entity.User" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Title</title>
</head>
<body class="home">
<jsp:include page="/pages/module/header.jsp"/>
<div class="edit-page">
    <div class="greeting">
        <h2>Edit user</h2>
    </div>

    <c:forEach var="validationException" items="${requestScope.validationExceptions}">
        <h4>${validationException}</h4>
    </c:forEach>

    <form action="<c:url value="/controller"/>" enctype="multipart/form-data" method="POST">
        <input type="hidden" name="command" value="upload_picture">
        <input type="hidden" name="userId" value="${someUser.id}">
        <input type="file" accept="image/*" name="content" height="130">
        <input type="submit" value="Upload File">
    </form>

    <form class="edit-form" action="<c:url value="/controller"/>" method="POST">
        <input type="hidden" name="command" value="update_user"/>
        <input type="hidden" name="userId" value="${someUser.id}">
        <input type="hidden" name="login" value="${someUser.login}">
        <input type="hidden" name="userRole" value="${someUser.role}">
        <input type="hidden" name="userState" value="${someUser.state}">
        <input type="hidden" name="userRating" value="${someUser.rating}">

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
            <label for="first-name">First name</label><br/>
            <input type="text" id="first-name" class="first-name" name="firstName" value="${someUser.firstName}"/>
        </div>

        <div class="block">
            <label for="last-name">Last name</label><br/>
            <input type="text" id="last-name" class="last-name" name="secondName" value="${someUser.secondName}"/>
        </div>

        <div class="block">
            <label for="email">Email</label><br/>
            <input type="email" id="email" class="email" name="email" value="${someUser.email}"/>
        </div>

        <div class="block">
            <input type="submit" class="edit-btn" value="Edit">
        </div>
    </form>
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

<c:remove var="someUser"/>
<c:remove var="newPicture"/>
