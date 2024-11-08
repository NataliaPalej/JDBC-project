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

        JPanel panel = new JPanel(new GridLayout(3, 2));

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

        add(panel);
        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                // Retrieve the customer_id after successful authentication
                Integer customerId = authenticateUser(email, password);
                if (customerId == null) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Successfully authenticated, pass customer_id to the MakeupStore
                    JOptionPane.showMessageDialog(null, "Welcome!");
                    new MakeupStore(customerId); // Pass customerId to MakeupStore
                    dispose(); // Close the login window
                }
            } catch (NataliaException ne) {
                JOptionPane.showMessageDialog(null, "Connection error: " + ne.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Authentication method
    private Integer authenticateUser(String email, String password) throws NataliaException, SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT customer_id, phone_no, is_admin FROM customers WHERE email_address = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id"); // Get the customer_id
                String phoneNo = resultSet.getString("phone_no");
                boolean isAdmin = resultSet.getBoolean("is_admin");

                // Check if the last 4 digits of phone match the password
                if (phoneNo.endsWith(password)) {
                    return customerId; // Return customer_id if authentication is successful
                }
            }
        }
        return null; // Return null if authentication fails
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
