<%--
  Created by IntelliJ IDEA.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
    <style>
        *{margin:0;padding:0;}
        .top{
            background-image: image('/resource');
            height: 50px;
            width: 100%;
            float: left;
        }
        .top1{
            height: 50px;
            width: 90%;
            float: left;
        }
        .top2{
            height: 50px;
            width: 10%;
            float: left;
        }
        .center
        {
            height: 500px;
            width: 100%;
            margin-top: 20px;
            float: left;
        }
        .center1,.center2,.center3{
            height: 500px;
            width: 33%;
            float: left;
        }
        .button{
            background-color: aqua;
            height: 50px;
            float: left;
            width: 100%;
        }
    </style>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="top">
    <div id="nav">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="welcome.jsp">首页<span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item ">
                        <a class="nav-link " href="signal_analysis.html">数据分析</a>
                    </li>
                    <li class="nav-item ">
                        <a class="nav-link " href="model_construct_AlexNet.html">模型构建与下载</a>
                    </li>
                    <li class="nav-item ">
                        <a class="nav-link " href="statistic_information.html">统计信息</a>
                    </li>
                </ul>
                <h5 style="height: 10px;" align="right"><a id="username" class="user" onmouseenter="showUserMenu(event)"></a> &nbsp;</h5>
            </div>
        </nav>
    </div>
    <div id="userMenu" style="position: fixed;width: 90px;height:160px;display: none;"onmouseleave="disShowUserMenu(event)">
        <div style="margin: 50px 20px 10px 10px;border-radius: 10px;box-shadow: darkgrey 10px 10px 30px 5px;width: 90px;height:100px;position: relative;background-color: whitesmoke">
            <ul style="list-style-type: none;position: absolute;margin-top: 15px;">
                <li><a href="userHome.html" class="menuli">个人中心</a></li>
                <li><a href="userSetting.html" class="menuli">账号设置</a></li>
                <li><a href="login.jsp" class="menuli" >退出</a></li>
            </ul>
        </div>
        <style>
            .menuli{
                margin-left: 10px;
            }
        </style>
    </div>
    <script>
        function showUserMenu(e) {
            at = $("#username")[0].getBoundingClientRect();
            left = at.left;
            style = $("#userMenu")[0].style;
            style.display=""
            style.left = left
            style.top = 0;
        }
        function disShowUserMenu(e) {
            $("#userMenu")[0].style.display="none"
        }
    </script>
</div>
<div class="center">
    <div class="center1">
        <a href="signal_analysis.html"><img src="../resource/pic/signal_analysis.jpg" height=500px width=100% /></a>
    </div>
    <div class="center2">
        <a href="model_construct_FCNN.html"><img src="../resource/pic/modeltrain_download.jpg" height=500px width=100% /></a>
    </div>
    <div class="center3">
        <a href="statistic_information.html"><img src="../resource/pic/statistic_information.jpg" height=500px width=100% /></a>
    </div>
</div>
<div class="button">

</div>
<script type="text/javascript">
    window.onload = function (ev) {
        var user = document.getElementById("username");
        var xhr = new XMLHttpRequest();
        xhr.open("POST","../GetUserName",true);
        xhr.send();
        xhr.onreadystatechange = function (ev1) {
            if(xhr.readyState==4&&xhr.status==200)
            {
                var content = xhr.responseText;
                if(content=="no,relogin")
                {
                    if(!alert("用户信息失效，重新登录！"))
                    {
                        window.location.href="login.jsp";
                    }
                }
                else
                {
                    user.innerText = content;
                    user.href = "userSetting.html.html";
                }
            }

        }

    };
</script>
</body>
</html>
