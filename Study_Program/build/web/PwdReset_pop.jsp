<%-- 
    Document   : PwdReset_pop
    Created on : 2023. 11. 20., 오후 2:01:50
    Author     : PARK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>비밀번호 초기화</title>
    </head>
    <body>
        <h1>비밀번호 초기화</h1>
        <form action="" method="post" >
            <input type="text" placeholder="새로운 비밀번호를 입력해주세요"><br>
            <input type="text" placeholder="한번더 입력해주세요"><br>
            <input type="button" value="확인" onclick="location.href='Login_Page.jsp'">
        </form>
    </body>
</html>
