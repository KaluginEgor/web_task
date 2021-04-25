<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/15/2021
  Time: 9:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<c:set var="page" value="/pages/edit_media_person.jsp" scope="session"/>
<jsp:useBean id="mediaPerson" class="com.example.demo_web.model.entity.MediaPerson" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editPage.css" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title><fmt:message key="label.edit.media.person"/></title>
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">
<div class="edit-page">
    <div class="greeting">
        <h2>Edit media person</h2>
    </div>

    <c:forEach var="validationException" items="${requestScope.validationExceptions}">
        <h4>${validationException}</h4>
    </c:forEach>


    <form action="<c:url value="/controller"/>" enctype="multipart/form-data" method="POST">
        <input type="hidden" name="command" value="upload_picture">
        <input type="hidden" name="mediaPersonId" value="${mediaPerson.id}">
        <input type="file" accept="image/*" name="content" height="130">
        <input type="submit" value="Upload File">
    </form>

    <form class="edit-form" action="<c:url value="/controller"/>" method="POST">
        <c:choose>
            <c:when test="${mediaPerson.id == 0}">
                <input type="hidden" name="command" value="create_media_person"/>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="mediaPersonId" value="${mediaPerson.id}"/>
                <input type="hidden" name="command" value="update_media_person"/>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${empty sessionScope.newPicture}">
                <div class="poster">
                    <img src="${pageContext.request.contextPath}/picture?currentPicture=${mediaPerson.picture}">
                    <input type="hidden" name="picture" value="${mediaPerson.picture}">
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
            <label for="firstName">First name</label><br/>
            <input type="text" required id="firstName" class="first-name" name="firstName" pattern="[A-Za-zА-Яа-яЁё]{1,20}"
                   placeholder="<fmt:message key="user.name.first"/>" value="${mediaPerson.firstName}">
        </div>

        <div class="block">
            <label for="secondName">Second name</label><br/>
            <input type="text" required id="secondName" class="last-name" name="secondName" pattern="[A-Za-zА-Яа-яЁё]{1,20}"
                   placeholder="<fmt:message key="user.name.second"/>" value="${mediaPerson.secondName}"/>
        </div>

        <div class="block">
            <label for="bio"><fmt:message key="media.person.bio"/></label><br/>
            <textarea id="bio" name="bio" cols="40" rows="5">${mediaPerson.bio}</textarea>
        </div>

        <div class="block occupation">
            <label for="occupation-div"><fmt:message key="media.person.occupation"/></label>
            <div id="occupation-div" class="occupation-div">
                <c:forEach var="occupation" items="${sessionScope.occupationTypes}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${mediaPerson.occupationType == occupation}">
                                <input type="radio" id="${occupation}" name="occupationType" checked="checked"
                                       value="${occupation}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" id="${occupation}" name="occupationType"
                                       value="${occupation}"/>
                            </c:otherwise>
                        </c:choose>
                        <label for="${occupation}">${occupation}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="block">
            <label for="birthday"><fmt:message key="media.person.birthday"/></label><br/>
            <input type="date" id="birthday" name="birthday" value="${mediaPerson.birthday}"/>
        </div>

        <div class="block occupation">
            <label for="movies-div"><fmt:message key="media.person.movies"/></label>
            <div id="movies-div" class="occupation-div">
                <c:forEach var="movie" items="${sessionScope.movies}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${mediaPerson.movies.contains(movie)}">
                                <input type="checkbox" id="${movie.id}" name="mediaPersonMovies" checked="checked"
                                       value="${movie.id}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" id="${movie.id}" name="mediaPersonMovies"
                                       value="${movie.id}"/>
                            </c:otherwise>
                        </c:choose>
                        <label for="${movie}">${movie.title}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="block">
            <input type="submit" class="edit-btn" value="<fmt:message key="label.submit"/>">
        </div>
    </form>
    <c:if test="${mediaPerson.id != 0}">
        <div class="block">
            <form action="<c:url value="/controller"/>" method="POST">
                <input type="hidden" name="command" value="open_media_person_page"/>
                <input type="hidden" name="mediaPersonId" value="${mediaPerson.id}"/>
                <input type="submit" value="<fmt:message key="label.back"/>"/>
                <br>
            </form>
        </div>
    </c:if>
</div>
</body>
</html>
<c:remove var="newPicture"/>


