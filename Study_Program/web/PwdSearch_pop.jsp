<%-- 
    Document   : PwdSearch_pop
    Created on : 2023. 11. 20., 오후 2:01:30
    Author     : PARK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>비밀번호 찾기</title>
    </head>
    <body>
        <h1>비밀번호 찾기</h1>
        <form action="" method="post" >
            <input type="text" placeholder="아이디를 입력해주세요">
            <input type="button" value="인증코드 전송" onclick="location.href='인증코드 전송 서블릿'"><br>
            <input type="text" placeholder="인증코드를 입력해주세요"><br>
            <input type="button" value="확인" onclick="location.href='Login_Page.jsp'">
        </form>
    </body>
</html>
