/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PARK
 */
import com.ithows.JdbcDao;
import java.sql.SQLException;
import java.util.List;


public class Study_DAO {
    public static List logincheck(String email, String pwd) throws SQLException {
        String id_query = "select user_id, user_name from user_info where email=? and pwd=?";
        List id_rs = null;
        try {
            id_rs = JdbcDao.queryForMapList(id_query, new Object[]{email,pwd});
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return id_rs;  
    } 
    
    public static List selectUserList(){

        String query = "select user_id, user_name from user_info where email=? and pwd=?";

        List list = null;
        try {
            list = JdbcDao.queryForMapList(query, new Object[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
