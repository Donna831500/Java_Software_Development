package Test;

import main.Order_GUI;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Order_GUITest {

    @Test
    void is_valid_input_test1() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String result_str = order_gui.is_valid_input("kajsdfh", "hjfga", "asdf", "qwiefh");
        assertEquals("Please enter valid date", result_str);
    }

    @Test
    void is_valid_input_test2() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String result_str = order_gui.is_valid_input("02/14/1988", "76", "3", "3");
        assertEquals("Please enter valid date", result_str);
    }

    @Test
    void is_valid_input_test3() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String result_str = order_gui.is_valid_input("1988/05/02", "21", "17", "3");
        assertEquals("Please enter a time in the future", result_str);
    }

    @Test
    void is_valid_input_test4() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String result_str = order_gui.is_valid_input("2022/05/02", "21", "17", "3");

        String[] parts = "2022/05/02".split("/");
        String time_str = parts[0]+"-"+parts[1]+"-"+parts[2]+" "+"21"+":"+"17"+":00";
        assertEquals("valid,"+time_str, result_str);
    }


    @Test
    void get_increament_str_test1() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String[] result_array = order_gui.get_increament_str(3);
        String[] expected_array = {"00","01","02"};
        assertTrue(Arrays.equals(expected_array, result_array));
    }


    @Test
    void get_increament_str_test2() throws Exception{
        Connection conn = getConnection();
        Order_GUI order_gui = new Order_GUI(conn, "002");
        String[] result_array = order_gui.get_increament_str(10);
        String[] expected_array = {"00","01","02","03","04","05","06","07","08","09"};
        assertTrue(Arrays.equals(expected_array, result_array));
    }


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