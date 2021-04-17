<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setLocale value="${not empty sessionScope.lang ? sessionScope.lang : 'en_US'}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />
<jsp:useBean id="user" class="com.example.demo_web.model.entity.User" scope="session"/>
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
            <ul>
                <li>
                    <form action="<c:url value="/controller"/>" method="POST" >
                        <input type="hidden" name="command" value="open_all_movies_page">
                        <button class="link">Movies</button>
                    </form>
                </li>
                <li>
                    <form action="<c:url value="/controller"/>" method="POST" >
                        <input type="hidden" name="command" value="open_all_media_persons_page">
                        <button class="link">Actors</button>
                    </form>
                </li>
                <li>
                    <form name="loginForm" method="POST" action="<c:url value="/controller"/>">
                        <input type="hidden" name="command" value="change_language">
                        <input type="hidden" name="page" value="${ sessionScope.page }">
                        <select name="lang" onchange="submit()">
                            <option value="en_US" <c:if test="${lang eq 'en_US'}">selected</c:if>>English</option>
                            <option value="ru_RU" <c:if test="${lang eq 'ru_RU'}">selected</c:if>>Русский</option>
                        </select>
                    </form>
                </li>
            </ul>
        </nav>
        <c:choose>
            <c:when test="${user.id != 0}">
                <div class="header-user-menu">
                    <img src="${pageContext.request.contextPath}/pictures/dude.jpg" alt="User Image"/>
                    <ul>
                        <li>
                            <form action="<c:url value="/controller"/>" method="POST" >
                                <input type="hidden" name="command" value="open_edit_user_page">
                                <input type="hidden" name="userId" value="${user.id}">
                                <button class="link" class="highlight">Edit profile</button>
                            </form>
                        </li>
                        <c:if test="${user.role == 'ADMIN'}">
                            <li>
                                <form action="<c:url value="/controller"/>" method="POST" >
                                    <input type="hidden" name="command" value="open_edit_media_person_page">
                                    <button class="highlight">Create media person</button>
                                </form>
                            </li>
                            <li>
                                <form action="<c:url value="/controller"/>" method="POST" >
                                    <input type="hidden" name="command" value="open_edit_movie_page">
                                    <button class="highlight">Create movie</button>
                                </form>
                            </li>
                        </c:if>
                        <li>
                            <form action="<c:url value="/controller"/>" method="POST" >
                                <input type="hidden" name="command" value="logout">
                                <button class="highlight">Logout</button>
                            </form>
                        </li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <ul>
                    <li>
                        <form action="<c:url value="/controller"/>" method="POST" >
                            <input type="hidden" name="command" value="open_login_page">
                            <button class="link"><fmt:message key="label.login" bundle="${ rb }" /></button>
                        </form>
                    </li>
                    <li>
                        <form action="<c:url value="/controller"/>" method="POST" >
                            <input type="hidden" name="command" value="open_registration_page">
                            <button class="link"><fmt:message key="label.register" bundle="${ rb }" /></button>
                        </form>
                    </li>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>
</header>
</body>
</html>
