package com.ithows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class JdbcDao {

    static {
        try {
            JdbcDao.loadProperties();
        } catch (Exception ex) {
            System.out.println( "드라이브 로딩 에러");
        }
    }


    private static Connection  conn = null;

    public static Properties loadProperties() { //드라이버 로딩
        Properties p = new Properties();
        try {
            String fileName;
            String OS = System.getProperty("os.name").toLowerCase();

            if(OS.indexOf("win") >= 0){ //win 있으면
                fileName = JdbcDao.class.getResource("/com/ithows/").toString().replaceAll("file:/", "") + "connectinfo.xml";  // -- @@ 윈도우즈 적용
            }else{
                fileName = "/" +  JdbcDao.class.getResource("/com/ithows/").toString().replaceAll("file:/", "") + "connectinfo.xml" ; //  @@ 리눅스 용
            }

            File f = new File(fileName);
            if (f.exists()) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));  //XML경로로 파일스트림을 연다
                p.loadFromXML(bis);  //XML파일의 프로퍼티를 로드함
                bis.close();            //inputstream 닫음
            } else {
                System.out.println("if else 로딩 실패" + f.toString());
            }
        } catch (IOException e) {
            System.out.println( "로딩 실패");
        }
        return p;
    }


    public static Connection getConnection() {


        Properties config = JdbcDao.loadProperties();

        String className = "";
        String jdbcURL = "";
        String username = "";
        String password = "";



        jdbcURL = config.getProperty("url");
        username = config.getProperty("username");
        password = config.getProperty("password");

        try {
            if(conn == null){

                if(config.getProperty("database").equals("mysql")) {
                    className = "com.mysql.jdbc.Driver"; // -- @@ mysql
                }else if(config.getProperty("database").equals("mariadb")){
                    className = "org.mariadb.jdbc.Driver"; // -- @@ mariaDB
                }else{
                    className = "org.sqlite.JDBC"; // -- @@ sqlite
                    

                }
                Class.forName(className);

                // 데이터베이스 연결
                if(config.getProperty("database").equals("mysql") || config.getProperty("database").equals("mariadb")){
                    conn = DriverManager.getConnection(jdbcURL, username, password);
                    System.out.println("Connect - Mysql ");
                }else{
                    jdbcURL = "jdbc:sqlite:" + JdbcDao.class.getResource("/jdbcdaotest/").toString().replaceAll("file:/", "") + jdbcURL;
                    conn = DriverManager.getConnection(jdbcURL);
                    System.out.println("Connect - SQLite ");
                }

                if (conn != null) {
                    System.out.println("데이터베이스에 성공적으로 연결되었습니다.");
                    conn.setAutoCommit(false);
                } else {
                    System.out.println("데이터베이스에 연결 실패");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(  "Connection 얻어내기 에러");
        }
        return conn;
    }


    public static void endConnection() {
        try {
            conn.commit();
        } catch (SQLException e) {
            try {
                System.out.println( "endConnection 에러 - commit - error");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println( "endConnection 에러 - rollback - error");
            }
        } finally {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e3) {
                System.out.println(  "endConnection 에러 - close - error");
            }
        }
    }


    /**
     * alter table DML 쿼리를 실행한다
     * @param sp_query 요청된 query
     * @return result 쿼리 성공 여부
     * @throws SQLException
     */
    public static boolean execute(String sp_query) throws SQLException {
        boolean result = false;
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        /* --------2011.9.5 transaction 처리를 위해서 ------------------- */
        boolean isInnerConnection = false;//일단은 외부 Connection 이용
        if(conn==null){ //없으면 내부 Connection 이용
            isInnerConnection = true;
            conn = JdbcDao.getConnection();
        }
        /* -------------------------------------------------------------- */

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sp_query);
            result = pstmt.execute();
        } catch (SQLException ex) {
            System.out.println("Alter Query ");
            throw ex;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getLocalizedMessage());
                throw ex;
            } finally {
                if (conn != null) {
                    /* -------------------------------------------------------------- */
                    if(isInnerConnection==true){//내부에서 만든 Connection만 제거한다.
                        JdbcDao.endConnection();
                    }/* -------------------------------------------------------------- */
                }
            }
        }
        return result;
    }


    public static int update(String sp_query) throws SQLException {
        return JdbcDao.update(sp_query, null);
    }

    /**
     * insert, update, delete 쿼리를 실행하고 변경된 레코드의 개수를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @return result 변경된 레코드 개수
     * @throws SQLException
     */
    public static int update(String sp_query, Object[] sp_Input) throws SQLException {
        int result = -1;
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        /* --------2011.9.5 transaction 처리를 위해서 ------------------- */
        boolean isInnerConnection = false;//일단은 외부 Connection 이용
        if(conn==null){ //없으면 내부 Connection 이용
            isInnerConnection = true;
            conn = JdbcDao.getConnection();
        }
        /* -------------------------------------------------------------- */

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    pstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            result = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(  "SQL Query Update");
            throw ex;
        } finally {
            try {
                if (pstmt != null) {
                    //conn.commit();
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw ex;
            } finally {
                if (conn != null) {
                    /* -------------------------------------------------------------- */
                    if(isInnerConnection==true){//내부에서 만든 Connection만 제거한다.
                        JdbcDao.endConnection();
                    }/* -------------------------------------------------------------- */
                }
            }
        }
        return result;
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 PreparedStatement 객체를 생성한 후 List 타입으로 결과값을 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @param mapper 사용자가 구현한 RowMapper
     * @return list list 객체
     * @throws SQLException
     */
    public static List queryForList(String sp_query, Object[] sp_Input, RowMapper mapper) throws SQLException {
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        List list = new ArrayList();
        Connection conn = JdbcDao.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    pstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            boolean hasResults = pstmt.execute();
            rs = pstmt.getResultSet();
            while (rs.next()) {
                if (mapper != null) {
                    list.add(mapper.mapRow(rs));
                } else {
                    list.add(rs.getObject(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(  "catch:" + sp_query);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    //conn.commit();
                    pstmt.close();
                }
            } catch (SQLException ex) {
                System.out.println( "catch :" + sp_query);
                throw ex;
            } finally {
                if (conn != null) {
                    JdbcDao.endConnection();
                }
            }
        }
        return list;
    }

    public static ResultMap queryForMapObject(String sp_query, Object[] sp_Input) throws SQLException {
        List list = queryForMapList(sp_query, sp_Input);
        if (list.size() > 0) {
            return (ResultMap) list.get(0);
        } else {
            return null;
        }
    }

    public static List queryForMapList(String sp_query, Object[] sp_Input) throws SQLException {
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        List list = new ArrayList();

        Connection conn = JdbcDao.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    pstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            boolean hasResults = pstmt.execute();
            rs = pstmt.getResultSet();
            /*----------------------------------------------------------------*/
            ResultSetMetaData rsMetaData = rs.getMetaData();
            while (rs.next()) {
                ResultMap map = new ResultMap();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    map.put(rsMetaData.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
            /*----------------------------------------------------------------*/
        } catch (SQLException e) {
            System.out.println( "catch:" + sp_query);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    //conn.commit();
                    pstmt.close();
                }
            } catch (SQLException ex) {
                System.out.println( "catch :" + sp_query);
                throw ex;
            } finally {
                if (conn != null) {
                    JdbcDao.endConnection();
                }
            }
        }
        return list;
    }

    /**
     * 주어진 쿼리를 이용하여 PreparedStatement 객체를 생성한 후 하나의 Object 타입 객체를 리턴하는 함수
     * @param sp_query 요청된 query
     * @return obj Object 객체
     * @throws SQLException
     */
    public static Object queryForObject(String sp_query, Object[] sp_Input, RowMapper mapper) throws SQLException {
        List list = queryForList(sp_query, sp_Input, mapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 PreparedStatement 객체를 생성한 후 하나의 Object 타입 객체를 리턴하는 함수
     * @param sp_query 요청된 query
     * @return obj Object 객체
     * @throws SQLException
     */
    public static Object queryForObject(String sp_query, RowMapper mapper) throws SQLException {
        return queryForObject(sp_query, null, mapper);
    }

    /**
     * 주어진 쿼리를 이용하여 PreparedStatement 객체를 생성한 후 하나의 int형 결과값을 리턴하는 함수
     * @param sp_query - 요청된 query
     * @return resultInt - int형 결과값
     * @throws SQLException
     * @throws NumberFormatException
     */
    public static int queryForInt(String sp_query) throws SQLException, NumberFormatException {
        return queryForInt(sp_query, null);
    }

    /**
     * 주어진 쿼리와 파라미터값을 이용하여 PreparedStatement 객체를 생성한 후 하나의 int형 결과값을 리턴하는 함수
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @return resultInt int형 결과값
     * @throws SQLException
     * @throws NumberFormatException
     */
    public static int queryForInt(String sp_query, Object[] sp_Input) throws SQLException, NumberFormatException {
        int resultInt = -1;
        Object obj = queryForObject(sp_query, sp_Input, null);
        if(obj!=null){
            try {
                resultInt = Integer.parseInt(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return resultInt;
    }

    /**
     * 주어진 쿼리와 파라미터값을 이용하여 PreparedStatement 객체를 생성한 후 하나의 String형 결과값을 리턴하는 함수
     * @param sp_query 요청된 query
     * @return obj.toString() String 객체
     * @throws SQLException
     */
    public static String queryForString(String sp_query) throws SQLException {
        return queryForString(sp_query, null);
    }

    /**
     * 주어진 쿼리와 파라미터값을 이용하여 PreparedStatement 객체를 생성한 후 하나의 String형 결과값을 리턴하는 함수
     * @param sp_query
     * @param sp_Input
     * @return obj.toString() String 객체
     * @throws SQLException
     */
    public static String queryForString(String sp_query, Object[] sp_Input) throws SQLException {
        Object obj = queryForObject(sp_query, sp_Input, null);
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 CallableStatement 객체를 생성한 후 OUT  파라미터와 변경된 레코드 갯수를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @param sp_Out OUT 파라미터
     * @return reuslt 변경된 레코드 갯수
     * @throws SQLException
     */
    public static int updateStored(String sp_query, Object[] sp_Input, Object[] sp_Out) throws SQLException {
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        int result = -1;
        /* --------2011.9.5 transaction 처리를 위해서 ------------------- */
        boolean isInnerConnection = false;//일단은 외부 Connection 이용
        if(conn==null){ //없으면 내부 Connection 이용
            isInnerConnection = true;
            conn = JdbcDao.getConnection();
        }
        /* -------------------------------------------------------------- */
        CallableStatement cstmt = null;
        try {
            cstmt = conn.prepareCall(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    cstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            System.out.println(sp_query);
//            if (sp_Out != null) {
//                int pos = sp_Input.length; // IN 파라미더 이후 OUT 파라미터를 받기 위해 pos 이동
//                for (int i = 0; i < sp_Out.length; i++) {
//                    if (sp_Out[i] instanceof String) {
//                        System.out.println("String Procedue");
//                        cstmt.registerOutParameter(pos + i + 1, Types.VARCHAR);
//                    } else if (sp_Out[i] instanceof Integer) {
//                        System.out.println("Integer Procedue");
//                        cstmt.registerOutParameter(pos + i + 1, Types.INTEGER);
//                    } else if (sp_Out[i] instanceof Date) {
//                        System.out.println("Date Procedue");
//                        cstmt.registerOutParameter(pos + i + 1, Types.DATE);
//                    } else{
//                        System.out.println("Nothing Procedue");
//                    }
//                }
//            }
            result = cstmt.executeUpdate();

            if (sp_Out != null) {
                int pos = sp_Input.length; // IN 파라미터 이후 OUT 파라미터를 받기 위해 pos 이동
                for (int i = 0; i < sp_Out.length; i++) {
                    sp_Out[i] = cstmt.getObject(pos + i + 1);
                }
            }

        } catch (SQLException ex) {
             ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (cstmt != null) {
                    //conn.commit();
                    cstmt.close();
                }
            } catch (SQLException ex) {
                 ex.printStackTrace();
                throw ex;
            } finally {
                if (conn != null) {
                    /* -------------------------------------------------------------- */
                    if(isInnerConnection==true)//내부에서 만든 Connection만 제거한다.
                        JdbcDao.endConnection();
                    /* -------------------------------------------------------------- */
                }
            }
        }
        return result;
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 CallableStatement 객체를 생성한 후 변경된 레코드 갯수를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @return result 변경된 레코드 갯수
     * @throws SQLException
     */
    public static int updateStored(String sp_query, Object[] sp_Input) throws SQLException {
        return updateStored(sp_query, sp_Input, null);
    }


    /**
     * 주어진 쿼리와 파라미터를 이용하여 CallableStatement 객체를 생성한 후 List 타입 객체를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @param mapper 사용자가 구현한 RowMapper
     * @return list List 객체
     * @throws SQLException
     */
    public static List queryForListStored(String sp_query, Object[] sp_Input, RowMapper mapper) throws SQLException {
        return JdbcDao.queryForListStoredOutParam(sp_query, sp_Input, null, mapper);
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 CallableStatement 객체를 생성한 후 List 타입 객체를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @param mapper 사용자가 구현한 RowMapper
     * @return list List 객체
     * @throws SQLException
     */
    public static List queryForListStoredOutParam(String sp_query, Object[] sp_Input, Object[] sp_Out, RowMapper mapper) throws SQLException {
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        List list = new ArrayList();
        Connection conn = JdbcDao.getConnection();
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = conn.prepareCall(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    cstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            if (sp_Out != null) {
                int pos = sp_Input.length; // IN 파라미더 이후 OUT 파라미터를 받기 위해 pos 이동
                for (int i = 0; i < sp_Out.length; i++) {
                    if (sp_Out[i] instanceof String) {
                        cstmt.registerOutParameter(pos + i + 1, Types.VARCHAR);
                    } else if (sp_Out[i] instanceof Integer) {
                        cstmt.registerOutParameter(pos + i + 1, Types.INTEGER);
                    } else if (sp_Out[i] instanceof Date) {
                        cstmt.registerOutParameter(pos + i + 1, Types.DATE);
                    }
                }
            }

            boolean hasResults = cstmt.execute();
            rs = cstmt.getResultSet();
            while (rs.next()) {
                list.add(mapper.mapRow(rs));
            }
            if (sp_Out != null) {
                int pos = sp_Input.length; // IN 파라미터 이후 OUT 파라미터를 받기 위해 pos 이동
                for (int i = 0; i < sp_Out.length; i++) {
                    sp_Out[i] = cstmt.getObject(pos + i + 1);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cstmt != null) {
                    //conn.commit();
                    cstmt.close();
                }
            } catch (SQLException ex) {
                throw ex;
            } finally {
                if (conn != null) {
                    JdbcDao.endConnection();
                }
            }
        }
        return list;
    }

    /**
     * 주어진 쿼리와 파라미터를 이용하여 CallableStatement 객체를 생성한 후 Object 타입 객체를 리턴한다.
     * @param sp_query 요청된 query
     * @param sp_Input IN 파리미터
     * @param mapper 사용자가 구현한 RowMapper
     * @return list List 객체
     * @throws SQLException
     */
    public static Object queryForObjectStored(String sp_query, Object[] sp_Input, RowMapper mapper) throws SQLException {
        List list = JdbcDao.queryForListStored(sp_query, sp_Input, mapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 주어진 쿼리를 CallableStatement 객체를 생성한 후 Object 타입 객체를 리턴한다.
     * @param sp_query 요청된 query
     * @param mapper 사용자가 구현한 RowMapper
     * @return list List 객체
     * @throws SQLException
     */
    public static Object queryForObjectStored(String sp_query, RowMapper mapper) throws SQLException {
        return queryForObjectStored(sp_query, null, mapper);
    }

    public static ResultMap queryForMapObjectStored(String sp_query, Object[] sp_Input, Object[] sp_Out) throws SQLException{
        List list = queryForMapListStored(sp_query, sp_Input, sp_Out);
        if(list.size()>0){
            return (ResultMap)list.get(0);
        }else{
            return null;
        }
    }
    public static List queryForMapListStored(String sp_query, Object[] sp_Input, Object[] sp_Out) throws SQLException{
        if (sp_query == null) {
            throw new SQLException("SQL Query가 NULL입니다.");
        }
        List list = new ArrayList();
        Connection conn = JdbcDao.getConnection();
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            cstmt = conn.prepareCall(sp_query);
            if (sp_Input != null) {
                for (int i = 0; i < sp_Input.length; i++) {
                    cstmt.setObject(i + 1, sp_Input[i]);
                }
            }
            if (sp_Out != null) {
                int pos = sp_Input.length; // IN 파라미더 이후 OUT 파라미터를 받기 위해 pos 이동
                for (int i = 0; i < sp_Out.length; i++) {
                    if (sp_Out[i] instanceof String) {
                        cstmt.registerOutParameter(pos + i + 1, Types.VARCHAR);
                    } else if (sp_Out[i] instanceof Integer) {
                        cstmt.registerOutParameter(pos + i + 1, Types.INTEGER);
                    } else if (sp_Out[i] instanceof Date) {
                        cstmt.registerOutParameter(pos + i + 1, Types.DATE);
                    }
                }
            }

            boolean hasResults = cstmt.execute();
            rs = cstmt.getResultSet();
            /*----------------------------------------------------------------*/
            ResultSetMetaData rsMetaData = rs.getMetaData();
            while (rs.next()) {
                ResultMap map = new ResultMap();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    map.put(rsMetaData.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
            /*----------------------------------------------------------------*/
            if (sp_Out != null) {
                int pos = sp_Input.length; // IN 파라미터 이후 OUT 파라미터를 받기 위해 pos 이동
                for (int i = 0; i < sp_Out.length; i++) {
                    sp_Out[i] = cstmt.getObject(pos + i + 1);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cstmt != null) {
                    //conn.commit();
                    cstmt.close();
                }
            } catch (SQLException ex) {
                throw ex;
            } finally {
                if (conn != null) {
                    JdbcDao.endConnection();
                }
            }
        }
        return list;
    }
}
