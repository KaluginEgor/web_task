<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/27/2021
  Time: 9:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<c:set var="page" value="/pages/edit_movie.jsp" scope="session"/>
<jsp:useBean id="movie" class="com.example.demo_web.model.entity.Movie" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title><fmt:message key="label.edit.movie"/></title>
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">
<div class="edit-page">
    <div class="greeting">
        <h2>Edit movie</h2>
    </div>

    <c:forEach var="validationException" items="${requestScope.validationExceptions}">
        <h4>${validationException}</h4>
    </c:forEach>

    <form action="<c:url value="/controller"/>" enctype="multipart/form-data" method="POST">
        <input type="hidden" name="command" value="upload_picture">
        <input type="hidden" name="movieId" value="${movie.id}">
        <input type="file" accept="image/*" name="content" height="130">
        <input type="submit" value="Upload File">
    </form>

    <form class="edit-form" action="<c:url value="/controller"/>" method="POST">
        <c:choose>
            <c:when test="${movie.id == 0}">
                <input type="hidden" name="command" value="create_movie"/>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="command" value="update_movie"/>
                <input type="hidden" name="movieId" value="${movie.id}"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${empty sessionScope.newPicture}">
                <div class="poster">
                    <img src="${pageContext.request.contextPath}/picture?currentPicture=${movie.picture}">
                    <input type="hidden" name="picture" value="${movie.picture}">
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
            <label for="title"><fmt:message key="movie.title"/></label><br>
            <input type="text" required id="title" class="title" name="movieTitle" value="${movie.title}"/>
        </div>

        <div class="block">
            <label for="release-date"><fmt:message key="movie.release.date"/></label><br>
            <input type="date" id="release-date" name="movieReleaseDate" value="${movie.releaseDate}"/>
        </div>

        <div class="block">
            <label for="description"><fmt:message key="movie.description"/></label><br/>
            <textarea id="description" name="movieDescription" cols="40"
                      rows="5">${movie.description}</textarea>
        </div>

        <div class="block genre">
            <label for="genre-div"><fmt:message key="movie.genres"/></label>
            <div id="genre-div" class="genre-div">
                <c:forEach var="genre" items="${sessionScope.genreTypes}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${not empty movie.genres and movie.genres.contains(genre)}">
                                <input type="checkbox" id="${genre}" name="movieGenre" checked="checked"
                                       value="${genre}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" id="${genre}" name="movieGenre"
                                       value="${genre}"/>
                            </c:otherwise>
                        </c:choose>
                        <label for="${genre}">${genre}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="block occupation">
            <label for="movies-div"><fmt:message key="movie.crew"/></label>
            <div id="movies-div" class="occupation-div">
                <c:forEach var="mediaPerson" items="${sessionScope.mediaPeople}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${not empty movie.crew and movie.crew.contains(mediaPerson)}">
                                <input type="checkbox" id="${mediaPerson.id}" name="movieCrew" checked="checked"
                                       value="${mediaPerson.id}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" id="${mediaPerson.id}" name="movieCrew"
                                       value="${mediaPerson.id}"/>
                            </c:otherwise>
                        </c:choose>
                        <label for="${mediaPerson.id}">${mediaPerson.firstName} ${mediaPerson.secondName}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="block">
            <input type="submit" class="edit-btn" value="Edit">
        </div>
    </form>
    <c:if test="${movie.id != 0}">
        <div class="block">
            <form action="<c:url value="/controller"/>" method="POST">
                <input type="hidden" name="command" value="open_movie_page"/>
                <input type="hidden" name="movieId" value="${movie.id}"/>
                <input type="submit" value="<fmt:message key="label.back"/>"/>
                <br>
            </form>
        </div>
    </c:if>
</div>
</body>
</html>
<c:remove var="newPicture"/>
