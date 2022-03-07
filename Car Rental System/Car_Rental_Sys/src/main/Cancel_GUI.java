package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * This is the GUI for user to cancel previous order.
 * The function of each GUI component can be easily understand from the variable name.
 */
public class Cancel_GUI implements ActionListener {

    private Connection conn;
    private String uid;

    private static JLabel cancel_label;
    private static JComboBox<String> jComboBox;
    private static JButton cancel_button;
    private static JButton back_button;
    private ArrayList<Integer> order_id_list;

    private static JPanel panel;
    private static JFrame frame;

    /**
     * Constructor of Cancel_GUI class, set the position and text of each component.
     * @param connection    a java.sql.Connection object to connect to local MySQL database
     */
    public Cancel_GUI(Connection connection, String user_ID){
        this.conn = connection;
        this.uid = user_ID;

        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(500,350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);


        String show_message = "Connection failed!";

        // Check whether current have previous orders,
        // If yes, show all the order ID in the drop list.
        // if no, show "You do not have order" message.
        try{
            Statement statement = this.conn.createStatement();
            String sql_query = "SELECT * FROM reserve WHERE user_id = \""+user_ID+"\"";
            ResultSet uid_set = statement.executeQuery(sql_query);
            order_id_list = new ArrayList<Integer>();
            while (uid_set.next()){
                order_id_list.add(uid_set.getInt("order_id"));
            }
            if(order_id_list.size()==0){
                show_message = "You do not have order";
            }
            else {
                show_message = "Please select the order ID that you want to cancel";
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        cancel_label = new JLabel(show_message);
        cancel_label.setBounds(10,20,350,25);
        panel.add(cancel_label);

        String[] ordersToChoose = new String[order_id_list.size()];
        for(int i=0;i<order_id_list.size();i++){
            ordersToChoose[i]=Integer.toString(order_id_list.get(i));
        }

        jComboBox = new JComboBox<>(ordersToChoose);
        jComboBox.setBounds(20, 50, 80, 25);
        panel.add(jComboBox);


        cancel_button = new JButton("Cancel Order");
        cancel_button.setBounds(15, 90, 130, 25);
        cancel_button.addActionListener(this);
        panel.add(cancel_button);

        // If the back button is clicked, the current cancel order GUI is closed and
        // Order GUI will pop up.
        back_button = new JButton("Back");
        back_button.setBounds(230, 90, 80, 25);
        back_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new Order_GUI(conn,user_ID );
            }
        });
        panel.add(back_button);

        if(order_id_list.size()==0){
            show_message = "You do not have order";
            this.jComboBox.setVisible(false);
            this.cancel_button.setVisible(false);
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * If cancel order button is clicked, the record with selected order ID will be deleted from database.
     * Current GUI will be updated according to whether the user still have previous order.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            String sql_query = "DELETE FROM reserve WHERE order_id = "+jComboBox.getItemAt(jComboBox.getSelectedIndex());

            Statement statement = this.conn.prepareStatement(sql_query);
            statement.executeUpdate(sql_query);
            JOptionPane.showMessageDialog(null,"Order cancel success!");
            frame.setVisible(false);
            new Cancel_GUI(this.conn, this.uid);

        }catch (Exception exception){
            JOptionPane.showMessageDialog(null,"Connection failed!");
            exception.printStackTrace();
        }


    }
}

