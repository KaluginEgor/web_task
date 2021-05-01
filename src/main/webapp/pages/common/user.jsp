<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 4/15/2021
  Time: 4:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:useBean id="user" class="com.epam.project.model.entity.User" scope="session"/>
<jsp:useBean id="someUser" scope="session" class="com.epam.project.model.entity.User"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="property/pagecontent"/>
<c:set var="page" value="/pages/common/user.jsp" scope="session"/>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/moviePage.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>${user.login}</title>
</head>
<body class="home">
<jsp:include page="/pages/module/header.jsp"/>
<jsp:useBean id="reviewToUpdate" class="com.epam.project.model.entity.MovieReview" scope="session"/>
<section class="section main">
    <div class="section-title">
        <h2>${someUser.login}</h2>
        <c:set var="admin" value="ADMIN"/>
        <c:if test="${user.id == someUser.id or (user.role == admin and someUser.role != admin)}">
            <form action="<c:url value="/controller"/>" method="POST" >
                <input type="hidden" name="command" value="open_edit_user_page"/>
                <input type="hidden" name="userId" value="${someUser.id}">
                <div class="btn">
                    <button class="edit-btn" id="${someUser.id}">
                        <i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                </div>
            </form>
        </c:if>
        <c:if test="${(user.id == someUser.id and user.role != admin) or (user.role == admin and someUser.role != admin)}">
            <form action="<c:url value="/controller"/>" method="POST" >
                <input type="hidden" name="command" value="delete_user">
                <input type="hidden" name="userId" value="${someUser.id}">
                <input type="hidden" name="userRole" value="${user.role}">
                <div class="btn">
                    <button class="delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></a>
                    </button>
                </div>
            </form>
        </c:if>
    </div>
    <section class="section-movies">
        <div class="movie">

            <div class="poster">
                <a>
                    <img src="${pageContext.request.contextPath}/picture?currentPicture=${someUser.picture}"
                         alt="${someUser.login}"/>
                </a>
            </div>

            <c:if test="${not empty someUser.rating}">
                <p><strong><fmt:message key="user.rating"/>: </strong>${someUser.rating}</p>
            </c:if>

            <c:if test="${not empty someUser.firstName}">
                <p><strong><fmt:message key="user.name.first"/>: </strong>${someUser.firstName}</p>
            </c:if>

            <c:if test="${not empty someUser.secondName}">
                <p><strong><fmt:message key="user.name.second"/>: </strong>${someUser.secondName}</p>
            </c:if>

            <c:if test="${not empty someUser.email}">
                <p><strong><fmt:message key="user.email"/>: </strong>${someUser.email}</p>
            </c:if>

            <c:if test="${not empty someUser.role}">
                <p><strong><fmt:message key="user.role"/>: </strong>${someUser.role}</p>
            </c:if>

            <c:if test="${not empty someUser.state}">
                <p><strong><fmt:message key="user.state"/>: </strong>${someUser.state}</p>
            </c:if>

            <c:forEach var="validationException" items="${requestScope.validationExceptions}">
                <h4>${validationException}</h4>
            </c:forEach>

            <c:if test="${not empty someUser.movieReviews}">
                <p><strong><fmt:message key="label.reviews"/> : </strong></p>
                <c:forEach var="review" items="${someUser.movieReviews}">
                    <c:if test="${reviewToUpdate.id != review.id and sessionScope.movieReviewId != review.id}">
                        <div class="review">
                            <form action="<c:url value="/controller"/>" method="POST" >
                                <input type="hidden" name="command" value="open_user_page"/>
                                <input type="hidden" name="userId" value="${review.userId}">
                                <h4><button class="link"><c:out
                                        value="${review.userLogin}"/></button></h4>
                            </form>

                            <div class="btn-row">
                                <c:if test="${(user.id == review.userId or user.role == admin)}">
                                    <form action="<c:url value="/controller"/>" method="POST" >
                                        <input type="hidden" name="command" value="prepare_movie_review_update"/>
                                        <input type="hidden" name="movieReviewId" value="${review.id}"/>
                                        <input type="hidden" name="movieId" value="${movie.id}"/>
                                        <input type="hidden" name="userId" value="${user.id}"/>
                                        <div class="btn">
                                            <button class="edit-btn" id="${review.id}">
                                                <i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                                        </div>
                                    </form>

                                    <form action="<c:url value="/controller"/>" method="POST" class="delete-review-form">
                                        <input type="hidden" name="command" value="delete_movie_review"/>
                                        <input type="hidden" name="movieReviewId" value="${review.id}"/>
                                        <input type="hidden" name="movieId" value="${movie.id}"/>
                                        <input type="hidden" name="userId" value="${user.id}"/>
                                        <div class="btn">
                                            <button class="delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i>
                                            </button>
                                        </div>
                                    </form>

                                    <c:set var="addedReview" value="true"/>
                                </c:if>
                            </div>


                            <h3 id="title-${review.id}" class="review-title"><c:out value="${review.title}"/></h3>
                            <p id="body-${review.id}" class="review-body"><c:out value="${review.body}"/></p>
                            <p>
                                <c:out value="${review.creationDate}"/>
                                <br>
                                <br>
                            </p>

                        </div>
                    </c:if>
                </c:forEach>
            </c:if>

            <c:if test="${not empty someUser.movieRatings}">
            <p><strong><fmt:message key="label.ratings"/>: </strong></p>
            <c:forEach var="rating" items="${someUser.movieRatings}">
                <c:if test="${sessionScope.user.id == someUser.id}">
                    <form action="<c:url value="/controller"/>" method="POST" class="delete-rating-form">
                        <input type="hidden" name="command" value="delete_movie_rating"/>
                        <input type="hidden" name="movieRatingId" value="${rating.id}"/>
                        <input type="hidden" name="movieId" value="${rating.movieId}"/>
                        <input type="hidden" name="userId" value="${someUser.id}">
                        <div class="btn">
                            <button class="delete-rating-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button>
                        </div>
                    </form>
                </c:if>
                <p>
                <form action="<c:url value="/controller"/>" method="POST">
                    <input type="hidden" name="command" value="open_movie_page"/>
                    <input type="hidden" name="movieId" value="${rating.movieId}"/>
                    <button class="link">${rating.movieTitle}</button>
                    : ${rating.value}
                    <br>
                </form>
                </p>

            </c:forEach>
        </div>
        </c:if>

        <div class="block">
            <br/>
            <c:forEach var="validationException" items="${sessionScope.validationErrors}">
                <h4>${validationException}</h4>
            </c:forEach>

            <c:if test="${not empty sessionScope.errorMessage}">
                <h4>${sessionScope.errorMessage}</h4>
            </c:if>
        </div>

        <c:set var="active" value="ACTIVE"/>
        <c:if test="${user.state == active and reviewToUpdate.id != 0 or not empty sessionScope.movieReviewTitle or not empty sessionScope.movieReviewBody}">
            <form action="<c:url value="/controller"/>" method="POST">
                <c:choose>
                    <c:when test="${reviewToUpdate.id == 0 and empty sessionScope.movieReviewId}">
                        <input type="hidden" name="command" value="create_movie_review"/>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="movieReviewId"
                                <c:choose>
                                    <c:when test="${not empty sessionScope.movieReviewId}">
                                        value="${sessionScope.movieReviewId}"
                                    </c:when>
                                    <c:when test="${not empty reviewToUpdate.id}">
                                        value="${reviewToUpdate.id}"
                                    </c:when>
                                </c:choose>
                        />
                        <input type="hidden" name="command" value="update_movie_review"/>
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="userId" value="${user.id}"/>
                <input type="hidden" name="movieId" value="${movie.id}"/>
                <input type="text" required name="movieReviewTitle" class="review-title-input" placeholder="<fmt:message key="review.title"/>"
                        <c:choose>
                            <c:when test="${not empty sessionScope.movieReviewTitle}">
                                value="${sessionScope.movieReviewTitle}"
                            </c:when>
                            <c:when test="${not empty reviewToUpdate.title}">
                                value="${reviewToUpdate.title}"
                            </c:when>
                        </c:choose>
                />
                <textarea required cols="60" rows="5" name="movieReviewBody" class="review-body-input"
                          placeholder="<fmt:message key="review.body"/> "><c:choose><c:when test="${not empty sessionScope.movieReviewBody}">${sessionScope.movieReviewBody}</c:when><c:when test="${not empty reviewToUpdate.body}">${reviewToUpdate.body}</c:when></c:choose></textarea>
                <input type="submit" class="leave-review-btn" value="<fmt:message key="label.leave.review"/> ">
            </form>
        </c:if>
        <c:remove var="reviewToUpdate"/>
    </section>
</section>
</body>
</html>
<c:remove var="validationErrors"/>
<c:remove var="errorMessage"/>
<c:remove var="movieReviewTitle"/>
<c:remove var="movieReviewBody"/>
<c:remove var="movieReviewId"/>
