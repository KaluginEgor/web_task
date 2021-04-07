<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/15/2021
  Time: 9:00 PM
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
    <title>Edit media person</title>
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

    <form class="edit-form" action="controller" method="POST">
        <c:choose>
            <c:when test="${empty requestScope.mediaPerson}">
                <input type="hidden" name="command" value="create_media_person"/>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="mediaPersonId" value="${requestScope.mediaPerson.id}"/>
                <input type="hidden" name="command" value="update_media_person"/>
            </c:otherwise>
        </c:choose>
        <div class="block">
            <label for="firstName">First name</label><br/>
            <input type="text" required id="firstName" class="first-name" name="firstName" pattern="[A-Za-zА-Яа-яЁё]{1,20}"
                   value="${requestScope.mediaPerson.firstName}">
        </div>

        <div class="block">
            <label for="secondName">Second name</label><br/>
            <input type="text" required id="secondName" class="last-name" name="secondName" pattern="[A-Za-zА-Яа-яЁё]{1,20}"
                   value="${requestScope.mediaPerson.secondName}"/>
        </div>

        <div class="block">
            <label for="bio">Biography</label><br/>
            <textarea id="bio" name="bio" cols="40" rows="5">${requestScope.mediaPerson.bio}</textarea>
        </div>

        <div class="block occupation">
            <label for="occupation-div">Occupation</label>
            <div id="occupation-div" class="occupation-div">
                <c:forEach var="occupation" items="${sessionScope.occupationTypes}">
                    <div class="block-div">
                        <c:choose>
                            <c:when test="${requestScope.mediaPerson.occupationType == occupation}">
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
            <label for="birthday">Birthday</label><br/>
            <input type="date" id="birthday" name="birthday" value="${requestScope.mediaPerson.birthday}"/>
        </div>

        <div class="block">
            <label for="picture">Picture</label><br/>
            <input type="text" id="picture" name="picture" value="${requestScope.mediaPerson.picture}"/>
        </div>

        <div class="block occupation">
            <label for="movies-div">Movies</label>
            <div id="movies-div" class="occupation-div">
                <c:forEach var="title" items="${sessionScope.movieTitles}">
                    <div class="block-div">

                                <input type="checkbox" id="${title}" name="movieTitle"
                                       value="${title.getKey()}"/>

                        <label for="${title}">${title.getValue()}</label>
                    </div>
                </c:forEach>
            </div>
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
