<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 2/28/2021
  Time: 10:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="page" value="/pages/all_media_persons.jsp" scope="session"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css" />
    <title>Title</title>
</head>
<jsp:include page="/pages/module/header.jsp"/>
<body class="home">
<section class="section main">
    <section class="section-movies">
        <ctg:all_actors/>
    </section>
</section>
</body>
</html>
