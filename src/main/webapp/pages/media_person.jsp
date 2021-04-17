<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/9/2021
  Time: 11:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />
<c:set var="page" value="/pages/mediaPerson.jsp" scope="session"/>
<html>
<jsp:useBean id="mediaPerson" scope="session" class="com.example.demo_web.model.entity.MediaPerson"/>
<head>
    <title>${mediaPerson.firstName} ${mediaPerson.secondName}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/moviePage.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">
<%--<jsp:useBean id="user" class="com.example.demo_web.entity.User" scope="session"/>--%>
<jsp:useBean id="user" class="com.example.demo_web.model.entity.User" scope="session"/>
<section class="section main">

    <div class="section-title">
        <h2>${mediaPerson.firstName} ${mediaPerson.secondName}
            <c:set var="admin" value="ADMIN"/>
            <c:if test="${user.role == admin}">
                <form action="<c:url value="/controller"/>" method="POST" >
                    <input type="hidden" name="command" value="open_edit_media_person_page">
                    <input type="hidden" name="mediaPersonId" value="${mediaPerson.id}">
                    <div class="btn">
                        <button class="edit-by-admin-btn"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                    </div>
                </form>
                <form action="<c:url value="/controller"/>" method="POST" >
                    <input type="hidden" name="command" value="delete_media_person">
                    <input type="hidden" name="mediaPersonId" value="${mediaPerson.id}">
                    <div class="btn">
                        <button class="delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                        </button>
                    </div>
                </form>
            </c:if>
        </h2>
    </div>

    <section class="section-movies">
        <div class="movie">

            <div class="poster">
                <a href="#">
                    <img src="${pageContext}${mediaPerson.picture}"
                         alt="${mediaPerson.firstName} ${mediaPerson.secondName}"/>
                </a>
            </div>

            <c:if test="${not empty mediaPerson.bio}">
                <p class="description">
                        ${mediaPerson.bio}
                </p>
            </c:if>

            <c:if test="${not empty mediaPerson.occupationType}">
                <p><strong><fmt:message key="occupation"/>: </strong>
                    ${mediaPerson.occupationType}
                </p>
            </c:if>

            <c:if test="${not empty mediaPerson.birthday}">
                <p><strong><fmt:message key="birthday"/>: </strong>${mediaPerson.birthday}</p>
            </c:if>

            <c:if test="${not empty mediaPerson.movies}">
                <p><strong><fmt:message key="movies"/>: </strong></p>
                <c:forEach var="movie" items="${mediaPerson.movies}">
                    <div class="movie">
                        <p>
                        <form action="<c:url value="/controller"/>" method="POST">
                            <input type="hidden" name="command" value="open_movie_page"/>
                            <input type="hidden" name="movieId" value="${movie.id}"/>
                            <button class="link"><c:out value="${movie.title}"/></button>
                            <br>
                        </form>
                        </p>
                    </div>
                </c:forEach>
            </c:if>

        </div>
        <a href="${requestScope.previous_page}"><fmt:message key="back"/></a>
    </section>
</section>
</body>
</html>