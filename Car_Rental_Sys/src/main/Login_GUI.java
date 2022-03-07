package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This is the GUI for user to login.
 * The function of each GUI component can be easily understand from the variable name.
 */
public class Login_GUI implements ActionListener{

    private Connection conn;

    private static JPanel panel;
    private static JFrame frame;
    private static JLabel user_label;
    private static JTextField user_text;
    private static JLabel password_label;
    private static JPasswordField password_field;
    private static JButton login_button;

    /**
     * Constructor of Login_GUI class, set the position and text of each component.
     * @param connection    a java.sql.Connection object to connect to local MySQL database
     */
    public Login_GUI(Connection connection){
        this.conn = connection;

        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        user_label = new JLabel("User ID");
        user_label.setBounds(10,20,80,25);
        panel.add(user_label);

        user_text = new JTextField(20); // parameter : length of text field
        user_text.setBounds(100,20,165,25);
        panel.add(user_text);

        password_label = new JLabel("Password");
        password_label.setBounds(10,50,80,25);
        panel.add(password_label);

        password_field = new JPasswordField(20); // parameter : length of text field
        password_field.setBounds(100,50,165,25);
        panel.add(password_field);

        login_button = new JButton("Login");
        login_button.setBounds(120,90,80,25);
        login_button.addActionListener(this);
        panel.add(login_button);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //panel.setVisible(true);
    }


    /**
     * If login button is clicked, check if the user ID is in the database, if not, show error message
     * Then check whether the password match user input, if not, show error message
     * Otherwise, the current Login GUI will closed and Order GUI will pop up.
     * @param e event parameter
     */
    @Override
    public void actionPerformed(ActionEvent e){

        try{
            // Check whether the user name is in database.
            Statement statement = this.conn.createStatement();
            String sql_query = "SELECT COUNT(*) as total FROM users WHERE user_id = \""+user_text.getText()+"\"";
            ResultSet user_id_set = statement.executeQuery(sql_query);
            user_id_set.next();
            // if User ID exist in database, check password with user input
            if (user_id_set.getInt("total")==1){
                sql_query = "SELECT * FROM users WHERE user_id = \""+user_text.getText()+"\"";
                user_id_set = statement.executeQuery(sql_query);
                user_id_set.next();
                String correct_password = user_id_set.getString("password");
                // if password is correct, close log in GUI and open order GUI
                if(correct_password.equals(password_field.getText())){
                    frame.setVisible(false);
                    new Order_GUI(this.conn, user_text.getText());
                }
                // if password is incorrect
                else{
                    JOptionPane.showMessageDialog(null,"Your password is not correct!");
                }

            }
            // if User ID does not exist in database, show error message
            else{
                JOptionPane.showMessageDialog(null,"Can not find your user ID!");
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
