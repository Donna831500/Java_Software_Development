package main;

import java.sql.*;

public class Reserve {
    /**
     * This is the main function of this project,
     * First connection to local database, then show the login GUI.
     * @param agrs Arguments
     * @throws Exception
     */
    public static void main(String[] agrs) throws Exception {
        Connection connection = getConnection();
        new Login_GUI(connection);
    }


    /**
     * This function establish a local connection between MySQL database and this project.
     * Please change the port number and databasename in url if necessary
     * Please change the username and password if necessary
     * @return java.sql.Connection object
     * @throws Exception sql exception will be throw if connection fail and error will be print out.
     */
    public static Connection getConnection() throws Exception{

        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/car_reserve_system";
            String username = "root";   // change user name if necessary
            String password = "";   // Put your password here
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connected to Database!");
            return conn;
        }catch(Exception e){System.out.println(e);}

        return null;

    }

}
