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

            Boolean isAdmin;
            try {
                isAdmin = authenticateUser(email, password);
                if (isAdmin == null) {
                    JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                } else if (isAdmin) {
                    JOptionPane.showMessageDialog(null, "Welcome Admin!");
                    // Redirect to admin screen (to be implemented later)
                } else {
                    JOptionPane.showMessageDialog(null, "Welcome User!");
                    new MakeupStore();
                    dispose();
                }
            } catch (NataliaException ne) {
                JOptionPane.showMessageDialog(null, "Connection error: " + ne.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Authentication method
    private Boolean authenticateUser(String email, String password) throws NataliaException, SQLException {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "SELECT phone_no, is_admin FROM customers WHERE email_address = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String phoneNo = resultSet.getString("phone_no");
                boolean isAdmin = resultSet.getBoolean("is_admin");

                // Check if the last 4 digits of phone match the password
                if (phoneNo.endsWith(password)) {
                    return isAdmin;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
