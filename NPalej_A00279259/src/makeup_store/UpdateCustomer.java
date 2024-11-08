package makeup_store;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class UpdateCustomer extends JFrame {
	
	private int customerId;
	private JTextField firstNameField, lastNameField, address1Field, address2Field, cityField, eircodeField, phoneNoField, emailField;
	
	// Button Colours 
	Color redColor = new Color(207, 60, 60);
	Color greenColor = new Color(51, 184, 87);
	Color pinkColor = new Color(224, 49, 154);
	
	public UpdateCustomer(int customerId) throws NataliaException {
		this.customerId = customerId;
		
		setTitle("Update Details");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("UPDATE PERSONAL DETAILS");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("DialogInput", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.CENTER);
        
        // Details Panel with GridLayout for form fields
        JPanel detailsPanel = new JPanel(new GridLayout(8, 2, 5, 5)); // 8 rows, 2 columns, 5px padding
        detailsPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        detailsPanel.add(firstNameField);

        detailsPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        detailsPanel.add(lastNameField);

        detailsPanel.add(new JLabel("Address Line 1:"));
        address1Field = new JTextField();
        detailsPanel.add(address1Field);

        detailsPanel.add(new JLabel("Address Line 2:"));
        address2Field = new JTextField();
        detailsPanel.add(address2Field);

        detailsPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        detailsPanel.add(cityField);

        detailsPanel.add(new JLabel("Eircode:"));
        eircodeField = new JTextField();
        detailsPanel.add(eircodeField);

        detailsPanel.add(new JLabel("Phone Number:"));
        phoneNoField = new JTextField();
        detailsPanel.add(phoneNoField);

        detailsPanel.add(new JLabel("Email Address:"));
        emailField = new JTextField();
        detailsPanel.add(emailField);

        // Button Panel for Update and Cancel buttons
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Update");
        updateButton.setBackground(greenColor);
        updateButton.addActionListener(new updateCustomer());
        buttonPanel.add(updateButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(redColor); 
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        // Add panels to mainPanel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add mainPanel to frame
        add(mainPanel);
        prepopulateFields();
        setVisible(true);
    }
	
	// Update customer details method
	private class updateCustomer implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Get input values from form fields
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address1 = address1Field.getText();
            String address2 = address2Field.getText();
            String city = cityField.getText();
            String eircode = eircodeField.getText();
            String phoneNo = phoneNoField.getText();
            String email = emailField.getText();

            try {
                updateCustomerDetails(firstName, lastName, address1, address2, city, eircode, phoneNo, email);
                JOptionPane.showMessageDialog(null, "Your details were successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); 
            } catch (SQLException | NataliaException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Update Details Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

     // Method to update customer details
        private void updateCustomerDetails(String firstName, String lastName, String address1, String address2, String city,
                                           String eircode, String phoneNo, String email) throws SQLException, NataliaException {
            try (Connection connection = DatabaseConnector.getConnection()) {
                String query = "UPDATE customers SET first_name=?, last_name=?, address1=?, address2=?, city=?, eircode=?, phone_no=?, email_address=? WHERE customer_id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, address1);
                statement.setString(4, address2);
                statement.setString(5, city);
                statement.setString(6, eircode);
                statement.setString(7, phoneNo);
                statement.setString(8, email);
                statement.setInt(9, customerId);
                statement.executeUpdate();
            }
        }
    }
		
		// Fetch current customer details
		private void prepopulateFields() throws NataliaException {
	        try (Connection connection = DatabaseConnector.getConnection()) {
	            String query = "SELECT * FROM customers WHERE customer_id = ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setInt(1, customerId);
	            var resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                firstNameField.setText(resultSet.getString("first_name"));
	                lastNameField.setText(resultSet.getString("last_name"));
	                address1Field.setText(resultSet.getString("address1"));
	                address2Field.setText(resultSet.getString("address2"));
	                cityField.setText(resultSet.getString("city"));
	                eircodeField.setText(resultSet.getString("eircode"));
	                phoneNoField.setText(resultSet.getString("phone_no"));
	                emailField.setText(resultSet.getString("email_address"));
	            }
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(this, "Error fetching details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

}
