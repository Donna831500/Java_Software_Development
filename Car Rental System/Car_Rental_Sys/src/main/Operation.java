package main;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The Operation class make the computation for rental system
 */
public class Operation {

    private Connection connection;
    public Operation(Connection conn){
        this.connection = conn;
    }

    /**
     * Check whether two period of time have overlap, helped check whether car is available
     * @param reserved_start_time   start time of previous reservation
     * @param reserved_num_of_day   number of days of previous reservation
     * @param wanted_start_time     start time of current wanted reservation
     * @param wanted_num_of_day     number of days of current wanted reservation
     * @return  whether two period of time have overlap
     */
    public Boolean no_overlap(Timestamp reserved_start_time, int reserved_num_of_day, Timestamp wanted_start_time, int wanted_num_of_day){
        // get end time of previous reservation
        Calendar cal = Calendar.getInstance();
        cal.setTime(reserved_start_time);
        cal.add(Calendar.DATE, reserved_num_of_day);
        Timestamp reserved_end_time = new Timestamp(cal.getTime().getTime());
        // get end time of current wanted reservation
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(wanted_start_time);
        cal1.add(Calendar.DATE, wanted_num_of_day);
        Timestamp wanted_end_time = new Timestamp(cal1.getTime().getTime());

        if(wanted_start_time.after(reserved_end_time) || wanted_end_time.before(reserved_start_time)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Add reservation to database and show success message
     * @param car_type          user selected car type
     * @param wanted_timestamp  start time of current wanted reservation
     * @param wanted_duration   number of days of current wanted reservation
     * @param user_id           user id of the user who make the reservation
     * @return                  if there are available car for rent, return success message with order ID
     *                          if there are available car for rent, return no available car message
     */
    public String add_reservation(String car_type, Timestamp wanted_timestamp, int wanted_duration, String user_id){

        try {
            // get all car id with given car type
            Statement statement = this.connection.createStatement();
            String sql_query = "SELECT car_id FROM car_storage where car_type = \"" + car_type + "\" order by car_id;";
            ResultSet car_id_set = statement.executeQuery(sql_query);
            ArrayList<Integer> car_id_list = new ArrayList<Integer>();
            while (car_id_set.next()) {
                car_id_list.add(car_id_set.getInt("car_id"));
            }

            int rent_car_ID = -1;
            int i=0;
            // if not find available car yet and not finish check all the car, keep going.
            while (rent_car_ID == -1 && i<car_id_list.size()){
                int current_car_id = car_id_list.get(i);

                Statement statement2 = this.connection.createStatement();
                String sql_query2 = "SELECT * FROM reserve WHERE car_id = "+String.valueOf(current_car_id);
                ResultSet single_car_set = statement2.executeQuery(sql_query2);

                Boolean is_available = true;
                while(single_car_set.next() && is_available){
                    Timestamp current_start_timestamp = single_car_set.getTimestamp("start_date_time");
                    int current_duration = single_car_set.getInt("num_of_days");
                    if (!no_overlap(current_start_timestamp, current_duration, wanted_timestamp, wanted_duration)){
                        is_available=false;
                    }
                }
                // If find available car, insert data into database and return success message with order ID
                if (is_available){
                    rent_car_ID = current_car_id;
                    // get new order ID
                    Statement statement3 = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    String sql_query3 = "SELECT * FROM car_reserve_system.reserve";
                    ResultSet reserve_set = statement3.executeQuery(sql_query3);
                    reserve_set.last();
                    int new_order_ID = reserve_set.getInt("order_id")+1;

                    String[] parts = wanted_timestamp.toString().split(" ");
                    String wanted_time_str = parts[0];
                    String year_str = wanted_time_str.substring(0,4);
                    wanted_time_str = wanted_time_str.substring(5);
                    wanted_time_str = wanted_time_str+"-"+year_str+" "+parts[1];
                    wanted_time_str = wanted_time_str.substring(0,wanted_time_str.length()-2);
                    //inser into database
                    // INSERT INTO `reserve` VALUES (123,'001', 1,'sedan',STR_TO_DATE("04-17-2022 15:40:00", "%m-%d-%Y %H:%i:%s"),'2');
                    String sql_query4 = "INSERT INTO `reserve` VALUES (";
                    sql_query4 = sql_query4+String.valueOf(new_order_ID)+",";
                    sql_query4 = sql_query4+"'"+user_id+"',";
                    sql_query4 = sql_query4+current_car_id+",";
                    sql_query4 = sql_query4+"'"+car_type+"',";
                    sql_query4 = sql_query4+"STR_TO_DATE(\""+wanted_time_str+"\", \"%m-%d-%Y %H:%i:%s\"),";
                    sql_query4 = sql_query4+"'"+ Integer.toString(wanted_duration)+"')";


                    System.out.println(sql_query4);
                    Statement statement4 = this.connection.prepareStatement(sql_query4);
                    statement4.executeUpdate(sql_query4);
                    return "Reserve Success!\nYour order ID is "+String.valueOf(new_order_ID);
                }
                i++;
            }

            // if not find available car, return No available car message.
            return "Sorry! No available car!";

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Connection Problem";
        }

    }
}
