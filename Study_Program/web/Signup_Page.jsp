<%-- 
    Document   : Signup_Page
    Created on : 2023. 11. 20., 오후 1:23:50
    Author     : PARK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원가입</title>
    </head>
    <body>
        <h1>회원가입 양식</h1>
        <form action="" method="post" class="signup_frame">
            아이디 : <input type="text" placeholder="아이디를 입력해주세요">
            <input type="button" value="중복 확인" onclick="location.href='중복확인'"><br> 
            비밀번호 : <input type="text" placeholder="비밀번호를 입력해주세요 (8자 이상)"><br>
            비밀번호 확인 : <input type="text" placeholder="비밀번호를 한번 더 입력해주세요"><br>
            이름 : <input type="text" placeholder="이름을 입력해주세요"><br>
            <input type="text" placeholder="이메일을 입력해주세요">
            <input type="button" value="인증코드 전송" onclick="location.href='인증코드 전송'"><br>
            <input type="button" value="확인" onclick="location.href='인증코드 확인'"><br>
            <input type="image" id="img">
            <a>shu_img.jpg</a><br> 
            <input type="button" value="업로드" onclick="location.href='사진 업로드'"><br>
            <input type="button" value="회원가입" onclick="location.href='Login_Page.jsp'">
            <input type="button" value="취소" onclick="location.href='Login_Page.jsp'">
                   
            
        </form>
    </body>
</html>
