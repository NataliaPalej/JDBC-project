package makeup_store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MakeupStoreContent extends JInternalFrame implements ActionListener {
    
	private JComboBox<String> filterComboBox;
    private JTable productsTable;
    private DefaultTableModel tableModel;
    
    public MakeupStoreContent(int customer_id) throws NataliaException {
        // Set up the frame
        super("Makeup Store", false, false, false, false);
        System.out.println("MakeupStoreContent:: Customer ID: " + customer_id);
        setSize(800, 400);
        setLayout(new BorderLayout());

        filterComboBox = new JComboBox<>(new String[]{"Price Low-High", "Price High-Low", "Category", "A-Z", "Z-A"});

        // Panel for filters and order button
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Sort By:"));
        topPanel.add(filterComboBox);

        JButton orderButton = new JButton("Place Order");
        topPanel.add(orderButton);

        // Set up product table
        tableModel = new DefaultTableModel(new String[]{"Product Code", "Product Name", "Category", "Brand", "Description", "Price", "Stock Status", "Image", "Quantity"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                // Set Image column to Icon class and Quantity column to Integer class
                return (column == 7) ? Icon.class : (column == 8) ? Integer.class : Object.class;
            }
        };
        
        productsTable = new JTable(tableModel);
        productsTable.getColumnModel().getColumn(8).setCellEditor(new EditQuantity());
        productsTable.setRowHeight(80);
        
        JScrollPane scrollPane = new JScrollPane(productsTable);
        
        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load products data 
        loadProducts("SELECT * FROM products");
        filterComboBox.addActionListener(this);
        
        // Add action listener for order button
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // List to store selected products with quantities
                java.util.List<ProductOrderItem> selectedItems = new ArrayList<>();

                for (int row = 0; row < productsTable.getRowCount(); row++) {
                    // Get quantity from the Quantity column
                    Integer quantity = (Integer) productsTable.getValueAt(row, 8);
                    if (quantity != null && quantity > 0) {
                        String productCode = (String) productsTable.getValueAt(row, 0);
                        selectedItems.add(new ProductOrderItem(productCode, quantity));
                    }
                }

                // Call processOrder with list of selected items
                if (!selectedItems.isEmpty()) {
                    try {
                        processOrder(customer_id, selectedItems);
                    } catch (NataliaException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }

    // Load products with specific filter
    private void loadProducts(String query) throws NataliaException {
    	// Clear previous data
        tableModel.setRowCount(0);  

        try (Connection connection = DatabaseConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Load and scale the image directly from path
                ImageIcon icon = new ImageIcon(new ImageIcon(rs.getString("product_img")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));

                tableModel.addRow(new Object[]{
                    rs.getString("product_code"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getString("product_brand"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("stock_status"),
                    // Image displayed as icon in the table
                    icon, 
                    // Quantity default to 0
                    0 
                });
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
        }
    }
    
    private void processOrder(int customer_id, java.util.List<ProductOrderItem> items) throws NataliaException {
        String sp_create_order = "{call sp_createOrder(?, ?)}"; 
        int order_id;

        try (Connection connection = DatabaseConnector.getConnection();
             CallableStatement createOrderStmt = connection.prepareCall(sp_create_order)) {

            connection.setAutoCommit(false);
            
            // Call sp_createOrder only once to create a new order and get the order ID
            createOrderStmt.setInt(1, customer_id);
            createOrderStmt.registerOutParameter(2, Types.INTEGER);

            createOrderStmt.execute();
            order_id = createOrderStmt.getInt(2);  
            
            if (order_id == 0) {
                JOptionPane.showMessageDialog(this, "[ERROR] Couldn't create new order", "Order Error", JOptionPane.ERROR_MESSAGE);
                connection.rollback();
                return;
            }

            String sp_addProduct = "{call sp_addProduct(?, ?, ?)}"; 
            StringBuilder orderStatusMessages = new StringBuilder();

            // Loop through items and add each to the same order
            try (CallableStatement addOrderItemStmt = connection.prepareCall(sp_addProduct)) {
                for (ProductOrderItem item : items) {
                    addOrderItemStmt.setInt(1, order_id); 
                    addOrderItemStmt.setString(2, item.getProductCode());
                    addOrderItemStmt.setInt(3, item.getQuantity());

                    // Execute AddOrderItem procedure
                    boolean hasResultSet = addOrderItemStmt.execute();
                    
                    // Accumulate status for each item
                    if (hasResultSet) {
                        try (ResultSet rs = addOrderItemStmt.getResultSet()) {
                            if (rs.next()) {
                                String resultMessage = rs.getString(1);
                                orderStatusMessages.append(resultMessage).append("\n"); // Append each result message
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error while adding order items: " + ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
                connection.rollback();  
                return;
            }
            // Commit the order if all items are successfully added
            connection.commit();
            String msg = "<html>"
            		+ "<div style='text-align: center;'>"
            		+ "<p style='font-size: 16px; font-weight: bold; font-family: DialogInput;'>Order placed successfully</p>"
            		+ "<p></p><p style='text-align: center;'>Item(s) ordered:</p>"
            		+ "<p style='text-align: center; font-family: sans-serif;'>" + orderStatusMessages.toString().replace("\n", "<br>") + "</p>"
            		+ "<p></p><div></html>";
            // Show all item messages in one final confirmation message
            JOptionPane.showMessageDialog(this, msg, "Order Status", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while creating order: " + ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Class to process multiple items in one order 
class ProductOrderItem {
    private String productCode;
    private int quantity;

    public ProductOrderItem(String productCode, int quantity) {
        this.productCode = productCode;
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }
}

// Class to add quantity field 
@SuppressWarnings("serial")
class EditQuantity extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private JPanel panel;
    private JButton minusButton, plusButton;
    private JTextField quantityField;
    private int quantity = 0;

    public EditQuantity() {
        panel = new JPanel(new BorderLayout());

        // Minus button
        minusButton = new JButton("-");
        minusButton.addActionListener(e -> updateQuantity(-1));
        panel.add(minusButton, BorderLayout.WEST);

        // Quantity field
        quantityField = new JTextField("0", 3);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false);
        panel.add(quantityField, BorderLayout.CENTER);

        // Plus button
        plusButton = new JButton("+");
        plusButton.addActionListener(e -> updateQuantity(1));
        panel.add(plusButton, BorderLayout.EAST);
    }

    private void updateQuantity(int value) {
    	// Make sure quantity cannot be negative
        quantity = Math.max(0, quantity + value); 
        quantityField.setText(String.valueOf(quantity));
        // Stop editing JTable
        fireEditingStopped();
    }

    @Override
    public Object getCellEditorValue() {
        return quantity;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        quantity = (value instanceof Integer) ? (int) value : 0;
        quantityField.setText(String.valueOf(quantity));
        return panel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        quantity = (value instanceof Integer) ? (int) value : 0;
        quantityField.setText(String.valueOf(quantity));
        return panel;
    }
}

