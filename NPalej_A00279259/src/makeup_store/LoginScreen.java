package makeup_store;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Email input
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        // Password input
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        panel.add(loginButton);
        
        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> new Register());
        panel.add(registerButton);
        
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paddingPanel.add(panel, BorderLayout.CENTER);

        add(paddingPanel);
        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @SuppressWarnings("unused")
		@Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Retrieve the customer_id and is_admin status after successful authentication
                UserAuthResult authResult = authenticateUser(email, password);
                System.out.println("authResult: " + authResult.getCustomerID());
                if (authResult == null) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Successfully authenticated
                	String message = "<html><div style='text-align: center;'><h3 style='font-family: DialogInput;'>Welcome to Makeup Store</h3><br></div></html>";
                    JOptionPane.showMessageDialog(null, message);
                    if (authResult.isAdmin) {
                    	 // Open admin screen 
                        new AdminScreen(authResult.getCustomerID());
                        
                    } else {
                    	// Open customer screen
                        new MakeupStore(authResult.getCustomerID()); 
                    }
                 // Close the login window
                    dispose(); 
                }
            } catch (NataliaException ne) {
                JOptionPane.showMessageDialog(null, "Connection error: " + ne.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Authentication method, returning an object that includes customer_id and is_admin status
    private UserAuthResult authenticateUser(String email, String password) throws NataliaException, SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT customer_id, phone_no, is_admin FROM customers WHERE email_address = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int customer_id = resultSet.getInt("customer_id");
                String phoneNo = resultSet.getString("phone_no");
                boolean isAdmin = resultSet.getBoolean("is_admin");

                // Check if the last 4 digits of phone match the password
                if (phoneNo.endsWith(password)) {
                    return new UserAuthResult(customer_id, isAdmin); // Return auth result if authentication is successful
                }
            }
        }
        return null; // Return null if authentication fails
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}

// Store the logged in user details
class UserAuthResult {
    int customer_id;
    boolean isAdmin;

    UserAuthResult(int customer_id, boolean isAdmin) {
        this.customer_id = customer_id;
        this.isAdmin = isAdmin;
    }
    
    public int getCustomerID() {
    	return this.customer_id;
    } 
}
