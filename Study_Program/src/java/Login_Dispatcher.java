/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PARK
 */
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/Login_Dispatcher")
public class Login_Dispatcher extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if("login".equals(action)) {
            login(request, response);
        } else if ("signup".equals(action)) {
            signUp(request, response);
        } else if ("pwdSearch".equals(action)) {
            pwdSearch(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
        
    }
    
    private void login (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = request.getParameter("name");
    String password = request.getParameter("pwd");

    try (Connection con = DbInfo.getConnection()) {
        JavaDAO jdao = new JavaDAO(con);

        // Check if the provided username and password are valid
        if (jdao.checkUser(username, password)) {
            
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Redirect to the login success page
            response.sendRedirect("login_success.jsp");
        } else {
            // Failed login logic
            // Redirect to the login page with an error message
            response.sendRedirect("login.jsp?error=1");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle database-related exception
        response.sendRedirect("error.jsp");
    }
}

    
    private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        response.sendRedirect("Signup_Page.jsp");
    }
    
    private void pwdSearch (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        response.sendRedirect("PwdSearch.jsp");
    }
}
