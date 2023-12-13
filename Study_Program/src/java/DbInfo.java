/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PARK
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbInfo {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/?user=root";
        String username = "root";
        String pwd = "1234";
        return DriverManager.getConnection(url, username, pwd);
    }
}
