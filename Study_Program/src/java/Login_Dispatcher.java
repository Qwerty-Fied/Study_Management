/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PARK
 */
import com.ithows.ResultMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@WebServlet("/Login_Dispatcher")
public class Login_Dispatcher extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String email = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        ArrayList<ResultMap> rs = null;
        try {
            rs = (ArrayList<ResultMap>) Study_DAO.logincheck(email, pwd); //id_rs 들어감
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int user_id = 0;
        String user_name = null;
        String success = null;
        for (ResultMap check : rs) {
            user_id = check.getInt("user_id");
            user_name = check.getString("user_name");
        }
        if (user_id != 0 && user_name != null) {
            success = user_name + "님 환영합니다. 아이디 = " + user_id;
            response.getWriter().write(success);
        } else {
            success = "로그인 실패, 아이디 / 비밀번호를 찾을 수 없습니다.";
            response.getWriter().write(success);
        }
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        
    }
    
    private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.sendRedirect("Signup_Page.jsp");
    }
    
    private void pwdSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.sendRedirect("PwdSearch.jsp");
    }
}
