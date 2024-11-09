package makeup_store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class MakeupStoreContent extends JInternalFrame implements ActionListener {
    
    private JComboBox<String> filterComboBox;
    private JTable productsTable;
    private JButton orderButton;
    private DefaultTableModel tableModel;
    
    public MakeupStoreContent() throws NataliaException {
        // Set up the frame
        super("Makeup Store", true, true, true, true);
        setSize(800, 400);
        setLayout(new BorderLayout());

        filterComboBox = new JComboBox<>(new String[]{
            "Price Low-High", "Price High-Low", "Category", "A-Z", "Z-A"
        });
        orderButton = new JButton("Order Selected Item");

        // Panel for filters and order button
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Sort By:"));
        topPanel.add(filterComboBox);
        topPanel.add(orderButton);
        
        // Set up product table
        tableModel = new DefaultTableModel(new String[]{"Product Code", "Product Name", "Category", "Brand", "Price", "Stock Status"}, 0);
        productsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        
        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial product data and set up listeners
        loadProducts("SELECT * FROM products");  // Load products initially without sorting
        filterComboBox.addActionListener(this);
        orderButton.addActionListener(this);

        setVisible(true);
    }

    // Load products with a given query
    private void loadProducts(String query) throws NataliaException {
        tableModel.setRowCount(0);  // Clear previous data
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("product_code"));
                row.add(rs.getString("product_name"));
                row.add(rs.getString("category"));
                row.add(rs.getString("product_brand"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("stock_status"));
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filterComboBox) {
            // Sort and filter based on selected option
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            String query = "SELECT * FROM products";
            
            switch (selectedFilter) {
                case "Price Low-High":
                    query += " ORDER BY price ASC";
                    break;
                case "Price High-Low":
                    query += " ORDER BY price DESC";
                    break;
                case "Category":
                    query += " ORDER BY category";
                    break;
                case "A-Z":
                    query += " ORDER BY product_name ASC";
                    break;
                case "Z-A":
                    query += " ORDER BY product_name DESC";
                    break;
            }
            try {
				loadProducts(query);
			} catch (NataliaException e1) {
				e1.printStackTrace();
			}
        } else if (e.getSource() == orderButton) {
            // Order selected item
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                placeOrder(productId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to order.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Placeholder method for placing an order (to be customized as needed)
    private void placeOrder(int productId) {
        // Implement order logic (e.g., inserting into an orders table, showing confirmation dialog, etc.)
        JOptionPane.showMessageDialog(this, "Order placed for product ID: " + productId, "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
    }


	public static void main(String[] args) throws NataliaException {
		new MakeupStoreContent();
	}

}
