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
<c:set var="page" value="/pages/mediaPerson.jsp" scope="request"/>
<html>
<jsp:useBean id="mediaPerson" scope="session" class="com.example.demo_web.entity.MediaPerson"/>
<head>
    <title>${mediaPerson.firstName} ${mediaPerson.secondName}</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/moviePage.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">
<%--<jsp:useBean id="user" class="com.example.demo_web.entity.User" scope="session"/>--%>
<c:set var="user" value="${sessionScope.user}"/>
<section class="section main">

    <div class="section-title">
        <h2>${mediaPerson.firstName} ${mediaPerson.secondName}
            <c:set var="admin" value="ADMIN"/>
            <c:if test="${user.role == admin}">
                <button class="edit-by-admin-btn"><a href="controller?command=open_edit_media_person_page&mediaPersonId=${mediaPerson.id}"><i
                        class="fa fa-pencil-square-o"
                        aria-hidden="true"></i></a></button>
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

            <c:if test="${not empty mediaPersonPage.occupation}">
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
                            <a href="controller?command=show_movie_page&movieId=${movie.id}">
                                <c:out value="${movie.title}"/>
                            </a>
                            <br>
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
