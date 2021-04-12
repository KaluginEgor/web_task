<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/demo.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
    <link href='http://fonts.googleapis.com/css?family=Cookie' rel='stylesheet' type='text/css'>
</head>
<body>
<header class="header-login-signup">
    <div class="header-limiter">
        <h1><a href="#">Movie<span>Rating</span></a></h1>
        <nav>
            <a href="controller?command=open_all_movies_page">Movies</a>
            <a href="controller?command=open_all_media_persons_page">Actors</a>
            <form name="loginForm" method="POST" action="controller">
                <input type="hidden" name="command" value="change_language">
                <input type="hidden" name="page" value="${ requestScope.page }">
                <select name="lang" onchange="submit()">
                    <option value="en_US" <c:if test="${lang eq 'en_US'}">selected</c:if>>English</option>
                    <option value="ru_RU" <c:if test="${lang eq 'ru_RU'}">selected</c:if>>Русский</option>
                </select>
            </form>
        </nav>
        <c:set var="user" value="${sessionScope.user}"/>
        <c:choose>
            <c:when test="${not empty user}">
                <div class="header-user-menu">
                    <img src="${pageContext.request.contextPath}/pictures/dude.jpg" alt="User Image"/>
                    <ul>
                        <li><a href="#" class="highlight">Edit profile</a></li>
                        <li><a href="controller?command=open_edit_media_person_page" class="highlight">Create media person</a></li>
                        <li><a href="controller?command=open_edit_movie_page" class="highlight">Create movie</a></li>
                        <li><a href="controller?command=logout" class="highlight">Logout</a></li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="controller?command=open_login_page"><fmt:message key="label.login" bundle="${ rb }" /></a></li>
                    <li><a href="controller?command=open_registration_page"><fmt:message key="label.register" bundle="${ rb }" /></a></li>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</header>
</body>
</html>
