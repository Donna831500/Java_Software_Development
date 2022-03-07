package Test;

import main.Operation;
import main.Reserve;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;


class OperationTest {
    @Test // This test will success only once, because the add_reservation function make change in the database.
    void add_reservation_car_available() throws Exception{
        Connection conn = getConnection();
        Operation operation = new Operation(conn);

        String car_type = "sedan";
        Timestamp timestamp = java.sql.Timestamp.valueOf("2022-06-20 10:10:10.0");
        int wanted_duration = 2;
        String user_id = "006";

        // Expected ID
        Statement statement3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql_query3 = "SELECT * FROM car_reserve_system.reserve";
        ResultSet reserve_set = statement3.executeQuery(sql_query3);
        reserve_set.last();
        String new_order_ID = Integer.toString(reserve_set.getInt("order_id")+1);

        assertEquals("Reserve Success!\nYour order ID is "+new_order_ID,operation.add_reservation(car_type, timestamp, wanted_duration, user_id));
    }

    @Test
    void add_reservation_car_unavailable() throws Exception{
        Connection conn = getConnection();
        Operation operation = new Operation(conn);

        String car_type = "sedan";
        Timestamp timestamp = java.sql.Timestamp.valueOf("2022-04-12 10:10:10.0");
        int wanted_duration = 4;
        String user_id = "002";

        assertEquals("Sorry! No available car!",operation.add_reservation(car_type, timestamp, wanted_duration, user_id));
    }


    @Test
    void no_overlap_test1()throws Exception{ //no_overlap(Timestamp reserved_start_time, int reserved_num_of_day, Timestamp wanted_start_time, int wanted_num_of_day)
        Timestamp t1 = java.sql.Timestamp.valueOf("2022-06-20 10:10:10.0");
        int num_of_day_1 = 5;
        Timestamp t2 = java.sql.Timestamp.valueOf("2022-06-21 19:06:00.0");
        int num_of_day_2 = 3;
        Connection conn = getConnection();
        Operation operation = new Operation(conn);
        assertFalse(operation.no_overlap(t1, num_of_day_1, t2, num_of_day_2));
    }

    @Test
    void no_overlap_test2()throws Exception{ //no_overlap(Timestamp reserved_start_time, int reserved_num_of_day, Timestamp wanted_start_time, int wanted_num_of_day)
        Timestamp t1 = java.sql.Timestamp.valueOf("2022-05-20 10:10:10.0");
        int num_of_day_1 = 2;
        Timestamp t2 = java.sql.Timestamp.valueOf("2022-05-28 19:06:00.0");
        int num_of_day_2 = 10;
        Connection conn = getConnection();
        Operation operation = new Operation(conn);
        assertTrue(operation.no_overlap(t1, num_of_day_1, t2, num_of_day_2));
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