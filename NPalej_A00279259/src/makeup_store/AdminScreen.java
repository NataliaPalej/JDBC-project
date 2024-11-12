package makeup_store;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class AdminScreen extends JFrame {

    private JPanel rightPanel;
    private CardLayout cardLayout;
    private JTextField product_id;
    private JTable productsTable;
    private DefaultTableModel tableModel;

    public AdminScreen(int customer_id) throws NataliaException {
        setTitle("Admin");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Left panel for navigation buttons
        JPanel leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        leftPanel.setPreferredSize(new Dimension(300, 600));

        // Empty labels to add padding at the top and bottom
        JLabel topSpace = new JLabel();
        JLabel bottomSpace = new JLabel();

        JLabel productsLabel = new JLabel("Products");
        productsLabel.setFont(new Font("DialogInput", Font.BOLD, 16));

        // LeftPanel Buttons
        JButton addProductButton = new JButton("ADD");
        JButton viewProductButton = new JButton("VIEW");
        JButton updateProductButton = new JButton("UPDATE");
        JButton deleteProductButton = new JButton("DELETE");

        leftPanel.add(topSpace);
        leftPanel.add(productsLabel);
        leftPanel.add(addProductButton);
        leftPanel.add(viewProductButton);
        leftPanel.add(updateProductButton);
        leftPanel.add(deleteProductButton);
        leftPanel.add(bottomSpace);

        // Right panel for dynamic content
        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        JPanel addProductPanel = createAddProductPanel();
        JPanel viewProductPanel = createViewProductPanel();
        JPanel updateProductPanel = createUpdateProductPanel();
        JPanel deleteProductPanel = createDeleteProductPanel();

        rightPanel.add(addProductPanel, "AddProduct");
        rightPanel.add(viewProductPanel, "ViewProduct");
        rightPanel.add(updateProductPanel, "UpdateProduct");
        rightPanel.add(deleteProductPanel, "DeleteProduct");

        // Button action listeners to switch views
        addProductButton.addActionListener(e -> cardLayout.show(rightPanel, "AddProduct"));
        viewProductButton.addActionListener(e -> cardLayout.show(rightPanel, "ViewProduct"));
        updateProductButton.addActionListener(e -> cardLayout.show(rightPanel, "UpdateProduct"));
        deleteProductButton.addActionListener(e -> cardLayout.show(rightPanel, "DeleteProduct"));

        // Set up the main layout
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    /***********************
     * Add New Product     *
     ***********************/

    private JPanel createAddProductPanel() {
    	// Define text fields for product attributes
        JTextField product_code = new JTextField();
        JTextField category = new JTextField();
        JTextField product_brand = new JTextField();
        JTextField product_name = new JTextField();
        JTextField description = new JTextField();
        JTextField price = new JTextField();
        JTextField discount_percent = new JTextField();
        JTextField stock = new JTextField();
        JTextField product_img = new JTextField();
    	
        JPanel panel = new JPanel(new BorderLayout());

     	// Add new product
        // 11 rows, 2 columns, spacing
        JPanel productPanel = new JPanel(new GridLayout(11, 2, 5, 5));  
 	
        productPanel.add(new JLabel("Add Product"));
        productPanel.add(new JLabel(""));
        
        productPanel.add(new JLabel("Product Code:"));
        productPanel.add(product_code);

        productPanel.add(new JLabel("Category:"));
        productPanel.add(category);

        productPanel.add(new JLabel("Product Brand:"));
        productPanel.add(product_brand);

        productPanel.add(new JLabel("Product Name:"));
        productPanel.add(product_name);

        productPanel.add(new JLabel("Description:"));
        productPanel.add(description);

        productPanel.add(new JLabel("Price:"));
        productPanel.add(price);

        productPanel.add(new JLabel("Discount %:"));
        productPanel.add(discount_percent);

        productPanel.add(new JLabel("Stock:"));
        productPanel.add(stock);
		
        productPanel.add(new JLabel("Product Image Path:"));
        productPanel.add(product_img);
        
        JButton saveButton = new JButton("Save");
        productPanel.add(saveButton);
        productPanel.add(new JLabel(""));
        
        saveButton.addActionListener(e -> {
            // Retrieve values from fields
            String code = product_code.getText();
            String cat = category.getText();
            String brand = product_brand.getText();
            String name = product_name.getText();
            String desc = description.getText();
            String priceText = price.getText();
            String discount = discount_percent.getText();
            String stockText = stock.getText();
            String imgPath = product_img.getText();

            // Call a method to save this product to the database (implement separately)
            try {
				if (addProduct(code, cat, brand, name, desc, priceText, discount, stockText, imgPath)) {
				    JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
				    JOptionPane.showMessageDialog(this, "Failed to add product. Please check your inputs.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (HeadlessException | NataliaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        
        panel.add(productPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private boolean addProduct(String code, String cat, String brand, String name, String desc, 
            String price, String discount, String stock, String imgPath) throws NataliaException {
		
    	String query = "INSERT INTO  products (product_code, category, product_brand, product_name, description, price, discount_percent, stock, product_img) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            // Set parameters for the update query
            stmt.setString(1, code);
            stmt.setString(2, cat);
            stmt.setString(3, brand);
            stmt.setString(4, name);
            stmt.setString(5, desc);
            stmt.setString(6, price);
            stmt.setString(7, discount);
            stmt.setString(8, stock);
            stmt.setString(9, imgPath);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was updated, false if not

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "[ERROR] Couldn't add product: " + ex.getMessage(), "\nDatabase Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
	}
    

    /*******************
     * View products   * 
     *******************/
    private JPanel createViewProductPanel() throws NataliaException {
    	// Set up product table
        tableModel = new DefaultTableModel(new String[]{"Product Code", "Product Name", "Category", "Brand", "Description", "Price", "Stock Status", "Image"}, 0) {
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
        add(scrollPane, BorderLayout.CENTER);
        
        // Load products data 
        loadProducts("SELECT * FROM products");
        
		return rightPanel;
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
                        icon, 
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
    }

    /******************
     * Update Product *
     ******************/
    private JPanel createUpdateProductPanel() {
        JTextField product_code, category, product_brand, product_name, description, price, discount_percent, stock, product_img;

        JPanel panel = new JPanel(new BorderLayout());

        // Product ID entry specific to this view
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter Product ID:"));
        product_id = new JTextField(10);
        topPanel.add(product_id);
        panel.add(topPanel, BorderLayout.NORTH);

        // Update product-specific content
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("Updating Product ID:"));

        JPanel productPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        productPanel.add(new JLabel("Product Code:"));
        product_code = new JTextField();
        productPanel.add(product_code);

        productPanel.add(new JLabel("Category:"));
        category = new JTextField();
        productPanel.add(category);

        productPanel.add(new JLabel("Product Brand:"));
        product_brand = new JTextField();
        productPanel.add(product_brand);

        productPanel.add(new JLabel("Product Name:"));
        product_name = new JTextField();
        productPanel.add(product_name);

        productPanel.add(new JLabel("Description:"));
        description = new JTextField();
        productPanel.add(description);

        productPanel.add(new JLabel("Price:"));
        price = new JTextField();
        productPanel.add(price);

        productPanel.add(new JLabel("Discount %:"));
        discount_percent = new JTextField();
        productPanel.add(discount_percent);

        productPanel.add(new JLabel("Stock:"));
        stock = new JTextField();
        productPanel.add(stock);

        productPanel.add(new JLabel("Product Image:"));
        product_img = new JTextField();
        productPanel.add(product_img);

        contentPanel.add(productPanel);
        JButton updateButton = new JButton("Update");
        contentPanel.add(updateButton);

        panel.add(contentPanel, BorderLayout.CENTER);

        // Action listener for the Update button
        updateButton.addActionListener(e -> {
            String productIdText = product_id.getText().trim();
            if (!productIdText.isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIdText);

                    // Retrieve values from input fields
                    String productCode = product_code.getText();
                    String categoryText = category.getText();
                    String brand = product_brand.getText();
                    String name = product_name.getText();
                    String desc = description.getText();
                    String priceText = price.getText();
                    String discount = discount_percent.getText();
                    String stockText = stock.getText();
                    String img = product_img.getText();

                    // Update the product details in the database
                    if (updateProductDetails(productId, productCode, categoryText, brand, name, desc, priceText, discount, stockText, img)) {
                        JOptionPane.showMessageDialog(this, "Product ID " + productId + " updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No product found with ID: " + productId, "Update Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException | HeadlessException | NataliaException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Product ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a Product ID to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    // Method to update product details in the database
    private boolean updateProductDetails(int productId, String productCode, String category, String brand, String name,
                                         String description, String price, String discount, String stock, String img) throws NataliaException {
        String query = "UPDATE products SET product_code=?, category=?, product_brand=?, product_name=?, description=?, price=?, discount_percent=?, stock=?, product_img=? WHERE product_id=?";
        
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            // Set parameters for the update query
            stmt.setString(1, productCode);
            stmt.setString(2, category);
            stmt.setString(3, brand);
            stmt.setString(4, name);
            stmt.setString(5, description);
            stmt.setString(6, price);
            stmt.setString(7, discount);
            stmt.setString(8, stock);
            stmt.setString(9, img);
            stmt.setInt(10, productId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was updated, false if not

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /*********************
     *  Delete Product   *
     * *******************/

    private JPanel createDeleteProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Product ID entry specific to this view
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter Product ID:"));
        product_id = new JTextField(10);
        topPanel.add(product_id);
        panel.add(topPanel, BorderLayout.NORTH);

        // Delete product-specific content
        JPanel contentPanel = new JPanel();
        JLabel confirmationLabel = new JLabel("Are you sure you want to delete Product ID:");
        contentPanel.add(confirmationLabel);

        JButton yesButton = new JButton("DELETE");
        JButton noButton = new JButton("CANCEL");
        contentPanel.add(yesButton);
        contentPanel.add(noButton);

        panel.add(contentPanel, BorderLayout.CENTER);

        // Action for DELETE button
        yesButton.addActionListener(e -> {
            String productIDText = product_id.getText().trim();
            if (!productIDText.isEmpty()) {
                try {
                    int productId = Integer.parseInt(productIDText);
                    
                    // Attempt to delete the product
                    if (deleteProduct(productId)) {
                        JOptionPane.showMessageDialog(this, "Product ID " + productId + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No product found with ID: " + productId, "Deletion Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException | HeadlessException | NataliaException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Product ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a Product ID to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action for CANCEL button
        noButton.addActionListener(e -> {
            product_id.setText(""); // Clear the input field
        });

        return panel;
    }

    // Method to delete a product from the database
    private boolean deleteProduct(int productId) throws NataliaException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("DELETE FROM products WHERE product_id = ?")) {
            
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            // Return true if a row was deleted, false if not
            return rowsAffected > 0;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
