<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
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
<c:set var="page" value="/pages/admin/edit_movie.jsp" scope="session"/>
<jsp:useBean id="movie" class="com.epam.project.model.entity.Movie" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title><fmt:message key="label.edit.movie"/></title>
</head>
<c:import url="/pages/module/header.jsp"/>
<body class="home">
<div class="edit-page">


    <div class="block">
    <form action="<c:url value="/controller"/>" enctype="multipart/form-data" method="POST">
        <input type="hidden" name="command" value="upload_picture">
        <input type="file" accept="image/*" name="content" height="130">
        <input type="submit" value="<fmt:message key="label.file.upload"/>">
    </form>
    </div>

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
            <input type="text" required id="title" class="title" name="movieTitle" placeholder="<fmt:message key="movie.title"/>"
                    <c:choose>
                        <c:when test="${not empty sessionScope.movieTitle}">
                            value="${sessionScope.movieTitle}"
                        </c:when>
                        <c:when test="${not empty movie.title}">
                            value="${movie.title}"
                        </c:when>
                    </c:choose>
            />
        </div>

        <div class="block">
            <label for="release-date"><fmt:message key="movie.release.date"/></label><br>
            <input type="date" id="release-date" name="movieReleaseDate"
                    <c:choose>
                        <c:when test="${not empty sessionScope.movieReleaseDate}">
                            value="${sessionScope.movieReleaseDate}"
                        </c:when>
                        <c:when test="${not empty movie.releaseDate}">
                            value="${movie.releaseDate}"
                        </c:when>
                    </c:choose>
            />
        </div>

        <div class="block">
            <label for="description"><fmt:message key="movie.description"/></label><br/>
            <textarea id="description" name="movieDescription" cols="40"
                      rows="5"><c:choose><c:when test="${not empty sessionScope.movieDescription}">${sessionScope.movieDescription}</c:when><c:when test="${not empty movie.description}">${movie.description}</c:when></c:choose></textarea>
        </div>

        <div class="block genre">
            <label for="genre-div"><fmt:message key="movie.genres"/></label>
            <div id="genre-div" class="genre-div">
                <c:forEach var="genre" items="${sessionScope.genreTypes}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${not empty sessionScope.movieGenre and sessionScope.movieGenre.contains(genre.ordinal())}">
                                <input type="checkbox" id="${genre}" name="movieGenre" checked="checked"
                                       value="${genre.ordinal()}"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty movie.genres and movie.genres.contains(genre)}">
                                        <input type="checkbox" id="${genre}" name="movieGenre" checked="checked"
                                               value="${genre.ordinal()}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" id="${genre}" name="movieGenre"
                                               value="${genre.ordinal()}"/>
                                    </c:otherwise>
                                </c:choose>
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
                            <c:when test="${not empty sessionScope.movieCrew and sessionScope.movieCrew.contains(mediaPerson.key)}">
                                <input type="checkbox" id="${mediaPerson.key}" name="movieCrew" checked="checked"
                                       value="${mediaPerson.key}"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty movie.crew and ctg:containsMediaPersonId(movie.crew, mediaPerson.key)}">
                                        <input type="checkbox" id="${mediaPerson.key}" name="movieCrew" checked="checked"
                                               value="${mediaPerson.key}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" id="${mediaPerson.key}" name="movieCrew"
                                               value="${mediaPerson.key}"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <label for="${mediaPerson.key}">${mediaPerson.value}</label>
                    </div>
                </c:forEach>
            </div>
        </div>
        <c:import url="/pages/module/messages.jsp"/>

        <div class="block">
            <input type="submit" class="edit-btn" value="<fmt:message key="label.submit"/>">
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
<c:remove var="movieTitle"/>
<c:remove var="movieReleaseDate"/>
<c:remove var="movieDescription"/>
<c:remove var="movieCrew"/>
<c:remove var="movieGenre"/>
<c:remove var="validationErrors"/>
<c:remove var="errorMessage"/>
<c:remove var="confirmMessage"/>
