<%-- 
    Document   : Login_Page
    Created on : 2023. 11. 20., 오후 12:46:54
    Author     : PARK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인</title>
    </head>
    <body>
        <h1>스터디 관리 프로그램</h1>
        <form action="Login_Dispatcher" method="post" class="log_frame">
            아이디 : <input type="text" id="name" placeholder="아이디를 입력해주세요">
            비밀번호 : <input type="password" id="pwd" placeholder="비밀번호를 입력해주세요">
            <input type="submit" value="로그인">
            <input type="button" onclick="location.href='Signup_Page.jsp'" value="회원가입">
        </form>
    </body>
</html>
