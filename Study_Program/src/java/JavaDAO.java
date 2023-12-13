/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PARK
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JavaDAO {
    private Connection connection;
    
    public JavaDAO(Connection connection) {
        this.connection = connection;
    }
    
    public boolean checkUser (String id, String pwd) {
        String query = "select user_id, user_name from user_info where email=? and pwd=?";
        try (PreparedStatement psmt = connection.prepareStatement(query)) {
            psmt.setString(1, id);
            psmt.setString(2, pwd);
            ResultSet rs = psmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
