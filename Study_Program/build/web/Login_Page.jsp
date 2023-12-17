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
            아이디 : <input type="text" name="name" placeholder="아이디를 입력해주세요">
            비밀번호 : <input type="password" name="pwd" placeholder="비밀번호를 입력해주세요">
            <input type="submit" value="로그인">
            <input type="button" onclick="location.href = 'Signup_Page.jsp'" value="회원가입">
        </form>
        <script>
            $(document).ready(function () {
                function showMessagePopup(success) {
                    alert(success);
                }

                // AJAX request to the servlet
                $.ajax({
                    type: "POST",
                    url: "Login_Dispatcher",
                    dataType: "text",
                    success: function (data) {
                        // Display the received message in a popup
                        showMessagePopup(data);
                    },
                    error: function () {
                        // Handle error if the request fails
                        alert("Error in AJAX request");
                    }
                });
            });
        </script>

        <h1>회원가입 Ajax테스트</h1>
        <button onclick="requestAjax();">버튼을 클릭하세요</button>
        <div id="result" style="margin-top: 5px"></div>
        <script>
            function requestAjax() {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    var result = document.getElementById("result");
                    if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE)
                        result.innerHTML = xhr.responseText;
                };
                xhr.open("GET", "error.jsp", true);
                xhr.send();
            }
        </script>
    <h>로그인 판정문</h>
    <script>
        $(document).ready(function () {
            $('#loginForm').submit(function event() {
                event.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "Login_Dispatcher",
                    data: $("#loginForm").serialize(),
                    success: function (response) {
                        if (response.trim === "success") {
                            alter("로그인 성공!")
                        } else {
                            alter("로그인 실패!")
                        }
                    }
                })
            })
        })

    </script>
</body>
</html>
