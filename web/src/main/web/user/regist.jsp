<%--
  Created by IntelliJ IDEA.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Regist</title>
    <script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
    </script>
    <script type="text/javascript">
        function _change() {
            var ele=document.getElementById("vCode");
            ele.src="<c:url value='/VerifyCedeServlet'/>?xxx="+new Date().getTime();
        }
    </script>
    <style type="text/css">
        .father{
            width:100%;
            height:100%;
            position: absolute;
            background: #53e3a6;
        }
        .son {
            width: 400px;
            height: 400px;
            position: relative;
            background-color: white;
            top: 50%;
            left: 50%;
            margin-top: -200px;
            margin-left: -200px;
            border-radius: 10px;
        }
        .input{
            position: relative;
            margin-left:50px;
            width: 250px;
            height: 40px;
            border-radius: 4px;
            font-size: 20px;
            font-weight: 300;
        }
        .vericode{
            position: relative;
            margin-left: 50px;
            width: 150px;
            height: 40px;
            border-radius: 4px;
            line-height: 30px;
            font-size: 20px;
            font-weight: 300;
        }
        .button{
            position: relative;
            margin-left: 50px;
            width: 250px;
            height: 40px;
            border-radius: 4px;
            background-color: blue;
        }
        #username_pic
        {
            position: absolute;
            margin-top: 4px;
            margin-left: 3px;
        }
        #password_pic
        {
            position: absolute;
            margin-top: 4px;
            margin-left: 3px;
        }
        #code_info
        {
            position: absolute;
            margin-left:50px;
        }
    </style>
</head>

<body>

<div class="father" >
    <div class="son">
        <h1 style="text-align: center">注册</h1>
        <p style="color:red;font-weight: 900" >${msg}</p>
        <form action="<c:url value='/RegistServlet'/> " method="post" class="form" id="form">

            <input type="text" onchange="username_veri()" id="username" name="username" class="input" value="${user.username}" placeholder="用户名"/><img width="30px" height="30px" id="username_pic" src="/BS/resource/pic/white.png">
            <p></p><br/>
            <input type="password" onchange="password_veri()" id="password" name="password" class="input" value="${user.password}" placeholder="密码"/><img width="30px" height="30px" id="password_pic" src="/BS/resource/pic/white.png">
            <p></p><br/>
            <input type="text" name="verifyCode" class="vericode" value="${user.verifyCode}" size="10"/>
            <img id="vCode" src="<c:url value='/VerifyCedeServlet'/> ">
            <a href="javascript:_change()"> 点击更换 </a><br/>
            <p id="code_info">${errors.verifyCode}</p><br/><br/>
            <input type="button" class="button" onclick="verifi()" value="立即注册"><br/>
        </form>
    </div>
</div>
<script  type="text/javascript">

    var pic1 = document.getElementById("username_pic");
    var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
    function username_veri() {
        var username = $("#username").val();
        if(uPattern.test(username)){
            pic1.src = "/BS/resource/pic/true.png";
        }
        else {
            pic1.src = "/BS/resource/pic/false.png";
        }
    }

    var pic2 = document.getElementById("password_pic");
    var pPattern = /^.*(?=.{6,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*?. ]).*$/;
    function password_veri() {
        var password = $("#password").val();
        //密码强度正则，最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
        if(pPattern.test(password)){
            pic2.src = "/BS/resource/pic/true.png";
        }
        else {
            pic2.src = "/BS/resource/pic/false.png";
        }
    }
    function verifi() {
        var password = $("#password").val();
        var username = $("#username").val();
        var form = document.getElementById("form");
        if(pPattern.test(password)&&uPattern.test(username))
        {
            var form = document.getElementById("form");
            form.submit();
        }
        else
        {
            alert('用户名或密码不符合');
        }
    }
</script>
</body>
</html>
