<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/27/2021
  Time: 9:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Edit movie</title>
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

    <form class="edit-form" action="controller" method="POST">
        <c:choose>
            <c:when test="${empty sessionScope.movie}">
                <input type="hidden" name="command" value="create_movie"/>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="command" value="update_movie"/>
                <input type="hidden" name="movieId" value="${sessionScope.movie.id}"/>
            </c:otherwise>
        </c:choose>
        <div class="block">
            <label for="title">Title</label><br>
            <input type="text" required id="title" class="title" name="movieTitle" value="${sessionScope.movie.title}"/>
        </div>

        <div class="block">
            <label for="release-date">Release date</label><br>
            <input type="date" id="release-date" name="movieReleaseDate" value="${sessionScope.movie.releaseDate}"/>
        </div>

        <div class="block">
            <label for="description">Description</label><br/>
            <textarea id="description" name="movieDescription" cols="40"
                      rows="5">${sessionScope.movie.description}</textarea>
        </div>

        <div class="block genre">
            <label for="genre-div">Genre</label>
            <div id="genre-div" class="genre-div">
                <c:forEach var="genre" items="${sessionScope.genreTypes}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${not empty sessionScope.movie.genres and sessionScope.movie.genres.contains(genre)}">
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
            <label for="movies-div">Movies</label>
            <div id="movies-div" class="occupation-div">
                <c:forEach var="mediaPerson" items="${sessionScope.mediaPeople}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${not empty sessionScope.movie.crew and sessionScope.movie.crew.contains(mediaPerson)}">
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
            <label for="poster">Poster</label><br/>
            <input type="text" id="poster" name="picture" value="${sessionScope.movie.picture}"/>
        </div>

        <div class="block">
            <input type="submit" class="edit-btn" value="Edit">
        </div>
    </form>
    <div class="block">
        <a href="${requestScope.previous_page}"><fmt:message key="back"/></a>
    </div>
</div>
</body>
</html>
