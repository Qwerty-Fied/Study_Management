<%-- 
    Document   : Study_select
    Created on : 2023. 11. 20., 오후 3:12:46
    Author     : PARK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>스터디 선택</title>
    </head>
    <body>
        <h1>스터디 선택</h1>
    <from action="" method="post">
        <img src="./Img_src/HomeButton.jpg" onclick="location.href='Login_Page.jsp'"/>
        <img src="./Img_src/Logout_Button.jpg" onclick="location.href='Login_Page.jsp'"/><br>
        <div>
            <div onclick="location.href='Login_Page.jsp'" style="border:1px solid; padding:10px;">
                <a>너만보여 스터디</a>
                <a>직책 : 리더</a><br>
                <input type="button" value="시작" onclick="시작 서블릿">
                <a>마지막 접속 : 2023-11-23 (목) PM 2:30</a> 
            </div>
            <div onclick="location.href='Login_Page.jsp'" style="border:1px solid; padding:10px;">
                <a>삼육 의정 스터디</a>
                <a>종료</a><br>
                <input type="button" value="시작" onclic="시작 서블릿"> 
                <a></a>
            </div>
        </div>
    </from>
    </body>
</html>
