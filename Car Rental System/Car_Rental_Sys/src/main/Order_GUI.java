package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.regex.*;

/**
 * This is the GUI to make reservation for user.
 * The function of each GUI component can be easily understand from the variable name.
 */
public class Order_GUI implements ActionListener{
    private Connection conn;
    private String uid;

    private static JLabel date_time_label;
    private static JComboBox<String> jComboBox; // Dropdown menu for user to select car type
    private static JLabel car_type_label;

    private static JLabel date_label;
    private static JTextField date_field;   // Field for user to enter rental start date

    private static JLabel hour_label;
    private static JComboBox<String> hour_jComboBox;    // Dropdown menu for user to select hour of rental start time

    private static JLabel min_label;
    private static JComboBox<String> min_jComboBox; // Dropdown menu for user to select minutes of rental start time

    private static JLabel duration_label;
    private static JTextField duration_field;   // Field for user to enter number of days of rental

    private static JButton reserve_button;  // Button user to make reservation
    private static JButton cancel_button;   // Button user to cancel previous order
    private static JButton logout_button;   // Button user to log out


    /**
     * Constructor of Order_GUI class, set the position and text of each component.
     * @param connection    a java.sql.Connection object to connect to local MySQL database
     * @param user_ID   the user ID get from login GUI
     */
    public Order_GUI(Connection connection, String user_ID){
        this.conn = connection;
        this.uid = user_ID;

        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500,350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);


        car_type_label = new JLabel("Please select the car type");
        car_type_label.setBounds(10,20,165,25);
        panel.add(car_type_label);


        String[] optionsToChoose = {"sedan", "SUV", "van"};
        jComboBox = new JComboBox<>(optionsToChoose);
        jComboBox.setBounds(300, 20, 80, 25);
        panel.add(jComboBox);


        date_time_label = new JLabel("Please select the start date with time and number of days of your reservation");
        date_time_label.setBounds(10,50,400,25);
        panel.add(date_time_label);


        date_label = new JLabel("Please enter the date in format YYYY/MM/DD");
        date_label.setBounds(10,80,250,25);
        panel.add(date_label);


        date_field = new JTextField(15);
        date_field.setBounds(300,80,120,25);
        panel.add(date_field);

        hour_label = new JLabel("Please select hour");
        hour_label.setBounds(10,110,200,25);
        panel.add(hour_label);

        String[] hourToChoose = get_increament_str(24);
        hour_jComboBox = new JComboBox<>(hourToChoose);
        hour_jComboBox.setBounds(300, 110, 80, 25);
        panel.add(hour_jComboBox);


        min_label = new JLabel("Please select minute");
        min_label.setBounds(10,150,200,25);
        panel.add(min_label);

        String[] minToChoose = get_increament_str(60);
        min_jComboBox = new JComboBox<>(minToChoose);
        min_jComboBox.setBounds(300, 150, 80, 25);
        panel.add(min_jComboBox);

        duration_label = new JLabel("Please enter the number of days");
        duration_label.setBounds(10,190,250,25);
        panel.add(duration_label);

        duration_field = new JTextField(15);
        duration_field.setBounds(300,190,120,25);
        panel.add(duration_field);

        reserve_button = new JButton("Reserve");
        reserve_button.setBounds(30, 230, 100, 25);
        reserve_button.addActionListener(this);
        panel.add(reserve_button);

        // If cancel button is clicked, current Order_GUI will be closed and
        // the GUI used to cancel previous order will pop up.
        cancel_button = new JButton("Cancel Previous Order");
        cancel_button.setBounds(150, 230, 170, 25);
        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new Cancel_GUI(connection, user_ID);
            }
        });
        panel.add(cancel_button);

        // If log out button is clicked, current Order_GUI will be closed and
        // the log in GUI will pop up.
        logout_button = new JButton("Log Out");
        logout_button.setBounds(340, 230, 100, 25);
        logout_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new Login_GUI(conn);
            }
        });
        panel.add(logout_button);



        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     *
     * @param date  rental start date
     * @param hour_str  hour of rental start time
     * @param min_str   minutes of rental start time
     * @param duration_days number of days of rental
     * @return  a String contain information that indicate whether the input is valid,
     *          if the input is invalid, specific information will show.
     */
    public String is_valid_input(String date, String hour_str, String min_str, String duration_days){
        // split input date by '/' to check for format
        String[] parts = date.split("/");
        // the months contains 30 days
        int[] small_month = {4,6,7,11};
        // check input date format (length)
        if(parts.length!=3 || parts[0].length()!=4 || parts[1].length()!=2 || parts[2].length()!=2){
            return "Please enter valid date";
        }
        else{
            // check input date is consist of numbers by applying regular expression
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m_year = p.matcher(parts[0]);
            Matcher m_month = p.matcher(parts[1]);
            Matcher m_day = p.matcher(parts[2]);
            Matcher m_duration = p.matcher(duration_days);

            if (!m_duration.matches() || Integer.valueOf(duration_days)<=0){
                return "Please enter a valid duration days";
            }
            else if (!m_year.matches() || !m_month.matches() || !m_day.matches()){
                return "Please enter a valid date";
            }
            else if (Integer.valueOf(parts[1])>12 || Integer.valueOf(parts[1])<1 || Integer.valueOf(parts[2])>31 || Integer.valueOf(parts[2])<1){
                return "Please enter a valid date";
            }
            else if (Arrays.asList(small_month).contains(Integer.valueOf(parts[1])) || Integer.valueOf(parts[2])>30){
                return "Please enter a valid date";
            }
            else if (Integer.valueOf(parts[1])==2 && Integer.valueOf(parts[2])>29){
                return "Please enter a valid date";
            }
            else if (Integer.valueOf(parts[1])==2 && Integer.valueOf(parts[0])%4!=0 && Integer.valueOf(parts[2])>28){
                return "Please enter a valid date";
            }
            // if input format is valid, check whether the input time is in the future.
            else{
                String time_str = parts[0]+"-"+parts[1]+"-"+parts[2]+" "+hour_str+":"+min_str+":00";  //":00.0"
                Timestamp input_timestamp = java.sql.Timestamp.valueOf(time_str);
                Timestamp current_timestamp = new Timestamp(System.currentTimeMillis());
                // if input time is in the past
                if(input_timestamp.before(current_timestamp)){
                    return "Please enter a time in the future";
                }
                // if input time is in the future
                else{
                    return "valid,"+time_str;
                }
            }

        }
    }

    /**
     * This function produce increment number array in string from 0 to n-1,
     * used for generate dropdown list for hour and minute of rental start time
     * @param n length of the array
     * @return Ex. when n=5, output = {"00", "01", "02", "03", "04"}
     */
    public String[] get_increament_str(int n){
        String[] result = new String[n];
        for(int i=0;i<n;i++){
            result[i] = String.format("%02d", i);
        }
        return result;
    }

    /**
     * If the reserve button is clicked, firstly check whether the input is valid.
     * If input is valid, save the change to database and show success message.
     * Otherwise, show wrong message.
     * @param e event parameter
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String date = date_field.getText();
        String hour = hour_jComboBox.getItemAt(hour_jComboBox.getSelectedIndex());
        String min = min_jComboBox.getItemAt(min_jComboBox.getSelectedIndex());
        String duration = duration_field.getText();
        String valid_date_str = is_valid_input(date, hour, min, duration);

        String[] parts = valid_date_str.split(",");
        // if input is valid, save the change to database and show success message.
        if (parts.length==2){
            Operation operation = new Operation(this.conn);
            Timestamp wanted_timestamp = java.sql.Timestamp.valueOf(parts[1]);
            String car_type = jComboBox.getItemAt(jComboBox.getSelectedIndex());
            String reserve_str = operation.add_reservation(car_type, wanted_timestamp, Integer.valueOf(duration), this.uid);
            JOptionPane.showMessageDialog(null,reserve_str);
        }
        // if input is not valid, a message box show
        else {
            JOptionPane.showMessageDialog(null,valid_date_str);
        }
    }
}
