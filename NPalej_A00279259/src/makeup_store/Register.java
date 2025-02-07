package makeup_store;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Register extends JFrame {
    private JTextField firstNameField, lastNameField, address1Field, address2Field, cityField, eircodeField, phoneNoField, emailField;

    public Register() {
        setTitle("Registration");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        // Panel with 11 rows 2 columns
        panel.setLayout(new GridLayout(11, 2));
        // Add margin to panel (top, left, bottom, right)
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Customer Details Fields
        panel.add(new JLabel("First Name*:"));
        firstNameField = new JTextField();
        setPlaceholder(firstNameField, "Your Name");
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name*:"));
        lastNameField = new JTextField();
        setPlaceholder(lastNameField, "Your Last Name");
        panel.add(lastNameField);

        panel.add(new JLabel("Address Line 1*:"));
        address1Field = new JTextField();
        setPlaceholder(address1Field, "Your Address");
        panel.add(address1Field);

        panel.add(new JLabel("Address Line 2:"));
        address2Field = new JTextField();
        setPlaceholder(address2Field, "Your Address (Optional)");
        panel.add(address2Field);

        panel.add(new JLabel("City*:"));
        cityField = new JTextField();
        setPlaceholder(cityField, "City");
        panel.add(cityField);

        panel.add(new JLabel("Eircode:"));
        eircodeField = new JTextField();
        setPlaceholder(eircodeField, "A01 B2C34");
        panel.add(eircodeField);

        panel.add(new JLabel("Phone Number*:"));
        phoneNoField = new JTextField();
        setPlaceholder(phoneNoField, "089 123 4567");
        panel.add(phoneNoField);

        panel.add(new JLabel("Email Address*:"));
        emailField = new JTextField();
        setPlaceholder(emailField, "youremail@example.com");
        panel.add(emailField);
        
        // Mandatory fields info
        JLabel mandatoryFieldsLabel = new JLabel("* mandatory fields");
        panel.add(mandatoryFieldsLabel);
        // Add empty field to align with 2 columns 
        panel.add(new JLabel());

        // Add register button 
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new registerAction());
        panel.add(registerButton);
        panel.add(new JLabel());

        add(panel);
        setVisible(true);
    }

    // RegisterAction method for RegisterButton
    private class registerAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Get input values from the form fields
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address1 = address1Field.getText();
            String address2 = address2Field.getText();
            String city = cityField.getText();
            String eircode = eircodeField.getText();
            String phoneNo = phoneNoField.getText();
            String email = emailField.getText();

            try {
                // Register customer 
                registerCustomer(firstName, lastName, address1, address2, city, eircode, phoneNo, email);
                // Display successful registration message
                JOptionPane.showMessageDialog(null, "Welcome " + firstName + "!\nYour registration was successful!\nYour "
                		+ "login password is the last four digits of your phone number.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Close registration window
                dispose(); 
            } catch (SQLException | NataliaException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // registerCustomer method to insert new customer into makeupdb
    private void registerCustomer(String firstName, String lastName, String address1, String address2, String city,
                                  String eircode, String phoneNo, String email) throws SQLException, NataliaException {
        // Establish database connection and insert new customer record
        try (Connection connection = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO customers (first_name, last_name, address1, address2, city, eircode, phone_no, email_address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, address1);
            statement.setString(4, address2);
            statement.setString(5, city);
            statement.setString(6, eircode);
            statement.setString(7, phoneNo);
            statement.setString(8, email);
            statement.executeUpdate();
        }
    }
    
    // Method to add placeholder for insert fields
    private void setPlaceholder(JTextField textField, String placeholder) {
        // Set initial placeholder text
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        // Add focus listener to placeholder
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
}