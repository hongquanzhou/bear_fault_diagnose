<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>列车轴箱轴承数据管理系统</title>
    <link rel="stylesheet" type="text/css" href="/BS/resource/css/styles_login.css">
</head>
<body>
<div class="htmleaf-container">
    <div class="wrapper">
        <div class="container">
            <h1>Welcome</h1>
            <p style="color:red;font-weight: 900">${msg}</p>
            <form action="<c:url value='/LoginServlet'/> " method="post" class="form">
                <input type="text" name="username" value="${user.username}"/>${errors.username}
                <input type="password" name="password" value="${user.password}"/>${errors.password}
                <input type="submit" value="登录">

            </form>
            <form action="<c:url value='/user/regist.jsp'/> " method="post" class="regist">
                <input type="submit" value="注册">
            </form>
        </div>

        <ul class="bg-bubbles">
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>
    </div>
</div>

<script src='/BS/resource/js/jquery-2.1.1.min.js' type="text/javascript"></script>
<script>

$('#login-button').click(function (event) {
event.preventDefault();
$('form').fadeOut(50);
$('.wrapper').addClass('form-success');
});

</script>

<div >
    <h1 style="text-align:center;margin:50px 0; font:normal 24px 'MicroSoft YaHei';color:#000000">列车轴箱轴承数据管理系统</h1>

</div>
</body>
</html>