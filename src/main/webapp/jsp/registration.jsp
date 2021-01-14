<%--
  Created by IntelliJ IDEA.
  User: egork
  Date: 12/30/2020
  Time: 8:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head><title>Login</title></head>
<body>
<form name="loginForm" method="POST" action="controller">
    <input type="hidden" name="command" value="register"/>
    Login:<br/>
    <input type="text" name="login" placeholder="user123" pattern="[A-Za-zА-Яа-яЁё0-9]{4,}" value=""/>
    <br/>Email:<br/>
    <input type="email" name="email" value="" placeholder="user123@gmail.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"/>
    <br/>Password:<br/>
    <input type="password" name="password" value="" pattern=".{8,}" title="Eight or more characters"/>
    <br/>
    ${errorRegisterMessage}
    <br/>
    ${errorUserRegistered}
    <br/>
    ${wrongAction}
    <br/>
    ${nullPage}
    <br/>
    <input type="submit" value="Register"/>
    <a href="controller?command=logout">Back to login page</a>
</form><hr/>
</body>
</html>
