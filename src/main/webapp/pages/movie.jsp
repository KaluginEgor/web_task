<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 3/9/2021
  Time: 11:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="pagecontent" var="rb" />
<html>
<jsp:useBean id="movie" scope="session" class="com.example.demo_web.model.entity.Movie"/>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/moviePage.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>${movie.title}</title>
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">

<jsp:useBean id="user" class="com.example.demo_web.model.entity.User" scope="session"/>
<jsp:useBean id="reviewToUpdate" class="com.example.demo_web.model.entity.MovieReview" scope="session"/>
<section class="section main">

    <div class="section-title">
        <h2>${movie.title}
            <c:set var="admin" value="ADMIN"/>
            <c:if test="${user.role == admin}">
                <div class="btn">
                    <button class="edit-by-admin-btn"><a
                            href="controller?command=open_edit_movie_page&movieId=${movie.id}"><i
                            class="fa fa-pencil-square-o"
                            aria-hidden="true"></i></a></button>
                </div>
                <div class="btn">
                    <button class="delete-btn"><a href="controller?command=delete_movie&movieId=${movie.id}">
                        <i class="fa fa-trash-o" aria-hidden="true"></i></a>
                    </button>
                </div>
            </c:if>
        </h2>
    </div>


    <section class="section-movies">
        <div class="movie">
            <div class="poster">
                <a href="#">
                    <img src="${pageContext.request.contextPath}/${movie.picture}"
                         alt="${movie.title}"/>
                </a>
            </div>

            <c:if test="${not empty movie.description}">
                <p class="description">
                        ${movie.description}
                </p>
            </c:if>

            <c:if test="${not empty movie.releaseDate}">
                <p><strong><fmt:message key="release.date"/>: </strong>${movie.releaseDate}</p>
            </c:if>

            <c:if test="${not empty movie.genres}">
                <p><strong><fmt:message key="genre"/>: </strong>
                    <c:forEach var="genre" items="${movie.genres}">
                        ${genre},
                    </c:forEach>
                </p>
            </c:if>

            <c:if test="${not empty movie.averageRating}">
                <p><strong><fmt:message key="rating"/>: </strong>
                <p id="movieRate">${movie.averageRating}</p>
                </p>
            </c:if>

            <c:if test="${not empty movie.crew}">
                <p><strong><fmt:message key="crew"/>: </strong></p>
                <c:forEach var="mediaPerson" items="${movie.crew}">
                    <div class="crew">
                        <p>
                            <a href="controller?command=open_media_person_page&mediaPersonId=${mediaPerson.id}">
                                <c:out value="${mediaPerson.firstName}"/>
                                <c:out value="${mediaPerson.secondName}"/>
                            </a>
                            <br>
                        </p>
                    </div>
                </c:forEach>
            </c:if>

            <c:set var="active" value="ACTIVE"/>
            <c:set var="userRate" value="${ctg:getUserRate(movie.ratingList, user.id)}"/>
            <c:if test="${not empty userRate}">
                <p>Your rate: ${userRate.value}</p>
                <form action="controller" method="POST" class="delete-review-form">
                    <input type="hidden" name="command" value="delete_movie_rating"/>
                    <input type="hidden" name="movieRatingId" value="${userRate.id}"/>
                    <input type="hidden" name="movieId" value="${movie.id}"/>
                    <div class="btn">
                        <button class="delete-btn"><i class="fa fa-trash-o" aria-hidden="true"></i>
                        </button>
                    </div>
                </form>
            </c:if>
            <c:if test="${user.id != 0 and user.state == active}">
                <form action="controller" method="POST" class="movieRatingForm">
                    <fieldset class="rating">
                        <input type="radio" id="star10" name="movieRatingValue" value="10" onclick="submit()" <c:if test="${userRate.value == 10}">checked="checked"</c:if>/>
                        <label class="full" for="star10"
                               title="Awesome - 10 stars">10</label>

                        <input type="radio" id="star9" name="movieRatingValue" value="9" onclick="submit()" <c:if test="${userRate.value == 9}">checked="checked"</c:if>/>
                        <label class="full" for="star9"
                               title="Really good - 9 stars">9</label>

                        <input type="radio" id="star8" name="movieRatingValue" value="8" onclick="submit()" <c:if test="${userRate.value == 8}">checked="checked"</c:if>/>
                        <label class="full" for="star8"
                               title="Pretty good - 8 stars">8</label>

                        <input type="radio" id="star7" name="movieRatingValue" value="7" onclick="submit()" <c:if test="${userRate.value == 7}">checked="checked"</c:if>/>
                        <label class="full" for="star7"
                               title="I'd watch it again with a beer - 7 stars">7</label>

                        <input type="radio" id="star6" name="movieRatingValue" value="6" onclick="submit()" <c:if test="${userRate.value == 6}">checked="checked"</c:if>/>
                        <label class="full" for="star6"
                               title="Not so bad - 6 stars">6</label>

                        <input type="radio" id="star5" name="movieRatingValue" value="5" onclick="submit()" <c:if test="${userRate.value == 5}">checked="checked"</c:if>/>
                        <label class="full" for="star5"
                               title="Kinda bad - 5 stars">5</label>

                        <input type="radio" id="star4" name="movieRatingValue" value="4" onclick="submit()" <c:if test="${userRate.value == 4}">checked="checked"</c:if>/>
                        <label class="full" for="star4"
                               title="Bad - 4 stars">4</label>

                        <input type="radio" id="star3" name="movieRatingValue" value="3" onclick="submit()" <c:if test="${userRate.value == 3}">checked="checked"</c:if>/>
                        <label class="full" for="star3"
                               title="Really bad - 3 stars">3</label>

                        <input type="radio" id="star2" name="movieRatingValue" value="2" onclick="submit()" <c:if test="${userRate.value == 2}">checked="checked"</c:if>/>
                        <label class="full" for="star2"
                               title="Lame - 2 stars">2</label>

                        <input type="radio" id="star1" name="movieRatingValue" value="1" onclick="submit()" <c:if test="${userRate.value == 1}">checked="checked"</c:if>/>
                        <label class="full" for="star1"
                               title="Awful - 1 star">1</label>
                    </fieldset>

                    <c:choose>
                        <c:when test="${empty userRate}">
                            <input type="hidden" name="command" value="create_movie_rating"/>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="command" value="update_movie_rating"/>
                        </c:otherwise>
                    </c:choose>

                    <input type="hidden" name="movieRatingId" value="${userRate.id}"/>
                    <input type="hidden" name="movieId" value="${movie.id}"/>
                    <input type="hidden" name="userId" id="userId" value="${user.id}"/>
                </form>
            </c:if>
            <br/>
            <br/>

            <c:set var="addedReview" value="false"/>


            <c:if test="${user.id != 0 and user.state == active and not empty movie.reviews}">
                <p><strong><fmt:message key="reviews"/> : </strong></p>
                <c:forEach var="review" items="${movie.reviews}">
                    <c:if test="${reviewToUpdate.id != review.id}">
                        <div class="review">
                            <h4><a href="controller?command=show_user_page&userId=${review.userId}"><c:out
                                    value="${review.userLogin}"/></a></h4>

                            <div class="btn-row">
                                <c:if test="${(user.id == review.userId or user.role == admin)}">
                                    <form action="controller" method="POST" >
                                        <input type="hidden" name="command" value="prepare_movie_review_update"/>
                                        <input type="hidden" name="movieReviewId" value="${review.id}"/>
                                        <input type="hidden" name="movieId" value="${movie.id}"/>
                                        <div class="btn">
                                            <button class="edit-btn" id="${review.id}">
                                                <i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                                        </div>
                                    </form>

                                    <form action="controller" method="POST" class="delete-review-form">
                                        <input type="hidden" name="command" value="delete_movie_review"/>
                                        <input type="hidden" name="movieReviewId" value="${review.id}"/>
                                        <input type="hidden" name="movieId" value="${movie.id}"/>
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

            <c:forEach var="validationException" items="${requestScope.validationExceptions}">
                <h4>${validationException}</h4>
            </c:forEach>


            <c:if test="${user.id != 0 and user.state == active and not addedReview and reviewToUpdate.id != 0}">
                <form action="controller" method="POST">
                    <c:choose>
                        <c:when test="${empty reviewToUpdate}">
                            <input type="hidden" name="command" value="create_movie_review"/>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="movieReviewId" value="${reviewToUpdate.id}"/>
                            <input type="hidden" name="command" value="update_movie_review"/>
                        </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="userId" value="${user.id}"/>
                    <input type="hidden" name="movieId" value="${movie.id}"/>
                    <input type="text" required name="movieReviewTitle" class="review-title-input" value="${reviewToUpdate.title}"
                           placeholder="<fmt:message key="review.title"/>"/>
                    <textarea required cols="60" rows="5" name="movieReviewBody" class="review-body-input"
                              placeholder="<fmt:message key="review.body"/> ">${reviewToUpdate.body}</textarea>
                    <input type="submit" class="leave-review-btn" value="<fmt:message key="leave.review"/> ">
                    <c:remove var="reviewToUpdate"/>
                </form>
            </c:if>

        </div>
    </section>
</section>
</body>
</html>
