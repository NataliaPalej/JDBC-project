package makeup_store;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	
	private JTextField product_code, category, product_brand, product_name, description, price, discount_percent, stock, product_img;

	public AdminScreen(int customer_id) throws NataliaException {
		setTitle("Admin");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setupLeftPanel();
		setupRightPanel();

		setVisible(true);
	}

	/**********************
	 * Left Panel Method *
	 **********************/
	private void setupLeftPanel() {
		// Create the main leftPanel
		JPanel leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
		leftPanel.setPreferredSize(new Dimension(300, 600));

		// Add labels and buttons
		String optionsLabel = "<html><div style='text-align: center; font-size: 16px; font-family: DialogInput;'>Options</div></html>";
		leftPanel.add(new JLabel(optionsLabel, SwingConstants.CENTER));
		leftPanel.add(optionButton("ADD", "AddProduct"));
		leftPanel.add(optionButton("VIEW", "ViewProduct"));
		leftPanel.add(optionButton("UPDATE", "UpdateProduct"));
		leftPanel.add(optionButton("DELETE", "DeleteProduct"));
		leftPanel.add(new JLabel());

		// Create a wrapper panel for leftPanel with padding
		JPanel leftPanelPadding = new JPanel(new BorderLayout());
		leftPanelPadding.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Adds 20px padding to the left and
																					// right

		// Add leftPanel to the wrapper
		leftPanelPadding.add(leftPanel, BorderLayout.CENTER);

		// Add wrapper to the main frame on the left side
		add(leftPanelPadding, BorderLayout.WEST);
	}

	private JButton optionButton(String label, String panelName) {
		JButton button = new JButton(label);
		button.addActionListener(e -> {
			cardLayout.show(rightPanel, panelName);
		});
		return button;
	}

	/***********************
	 * Right Panel Method *
	 ***********************/
	private void setupRightPanel() throws NataliaException {
		// Create rightPanel with CardLayout
		rightPanel = new JPanel();
		cardLayout = new CardLayout();
		rightPanel.setLayout(cardLayout);

		// Add each panels to rightPanel
		rightPanel.add(createAddProductPanel(), "AddProduct");
		rightPanel.add(createViewProductPanel(), "ViewProduct");
		rightPanel.add(createUpdateProductPanel(), "UpdateProduct");
		rightPanel.add(createDeleteProductPanel(), "DeleteProduct");

		// Create wrapper panel to add padding
		JPanel rightPanelPadding = new JPanel(new BorderLayout());
		rightPanelPadding.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20)); // Adds 20px padding to the left

		// Add rightPanel to the wrapper panel
		rightPanelPadding.add(rightPanel, BorderLayout.CENTER);

		// Add the wrapper to the main frame
		add(rightPanelPadding, BorderLayout.CENTER);
	}

	/***********************
	 * Add New Product *
	 ***********************/
	private JPanel createAddProductPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		String addProductLabel = "<html><div style='text-align: center; font-size: 16px; font-family: DialogInput;'>Add Product</div></html>";

		JPanel productPanel = new JPanel(new GridLayout(11, 2, 5, 5));
		productPanel.add(new JLabel(addProductLabel));
		productPanel.add(new JLabel(""));

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

		productPanel.add(new JLabel("Product Image Path:"));
		product_img = new JTextField();
		productPanel.add(product_img);

		productPanel.add(new JLabel(""));
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveProductAction());
		productPanel.add(saveButton);

		panel.add(productPanel, BorderLayout.CENTER);
		return panel;
	}

	private class SaveProductAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Get input values from form fields
			String code = product_code.getText();
			String cat = category.getText();
			String brand = product_brand.getText();
			String name = product_name.getText();
			String desc = description.getText();
			String imgPath = "images/" + product_img.getText() + ".jpg";

			// Convert and validate numeric fields
			BigDecimal priceValue, discountValue;
			int stockValue;
			try {
				priceValue = new BigDecimal(price.getText().isEmpty() ? "0.00" : price.getText()).setScale(2,
						RoundingMode.HALF_UP);
				discountValue = new BigDecimal(
						discount_percent.getText().isEmpty() ? "0.00" : discount_percent.getText())
						.setScale(2, RoundingMode.HALF_UP);
				stockValue = stock.getText().isEmpty() ? 0 : Integer.parseInt(stock.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Please enter valid numbers for Price, Discount % and Stock.",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				boolean success = saveProductToDatabase(code, cat, brand, name, desc, priceValue, discountValue,
						stockValue, imgPath);
				if (success) {
					String message = "<html><div style='text-align: center;'>"
							+ "<p style='font-size: 16px; font-weight: bold; font-family: DialogInput;'>Product added successfully</p>"
							+ "<p style='text-align: center;'>Product " + code + " : " + name + " added.</p>"
							+ "</div></html>";
					JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);

					// Clear fields 
					product_code.setText("");
			        category.setText("");
			        product_brand.setText("");
			        product_name.setText("");
			        description.setText("");
			        price.setText("");
			        discount_percent.setText("");
			        stock.setText("");
			        product_img.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "Failed to add product. Please check your inputs.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "\nAdd Product Failed",
						JOptionPane.ERROR_MESSAGE);
			} catch (NataliaException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private boolean saveProductToDatabase(String code, String category, String brand, String name, String desc,
			BigDecimal price, BigDecimal discount, int stock, String imgPath) throws SQLException, NataliaException {
		String query = "INSERT INTO products (product_code, category, product_brand, product_name, description, price, discount_percent, stock, product_img) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement stmt = connection.prepareStatement(query)) {

			// Set parameters for the prepared statement
			stmt.setString(1, code);
			stmt.setString(2, category);
			stmt.setString(3, brand);
			stmt.setString(4, name);
			stmt.setString(5, desc);
			stmt.setBigDecimal(6, price);
			stmt.setBigDecimal(7, discount);
			stmt.setInt(8, stock);
			stmt.setString(9, imgPath);

			int rowsAffected = stmt.executeUpdate();
			// Return true if a row was inserted
			return rowsAffected > 0; 
		}
	}

	/*******************
	 *  View Products  *
	 *******************/
	private JPanel createViewProductPanel() throws NataliaException {
		JPanel searchPanel = new JPanel();
	    searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel searchLabel = new JLabel("Enter Product ID:");
	    JTextField searchField = new JTextField(10); 
	    JButton searchButton = new JButton("SEARCH");
	    
	    searchButton.addActionListener(e -> {
	        String productIdText = searchField.getText().trim();
	        if (!productIdText.isEmpty()) {
	            try {
	                int productId = Integer.parseInt(productIdText);
	                searchProductById(productId);
	            } catch (NumberFormatException | NataliaException ex) {
	                JOptionPane.showMessageDialog(this, "Please enter a valid numeric Product ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Product ID cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
	        }
	    });
	    
	    searchPanel.add(searchLabel);
	    searchPanel.add(searchField);
	    searchPanel.add(searchButton);
	    
	    tableModel = new DefaultTableModel(new String[] { "Product Code", "Product Name", "Category", "Brand",
				"Description", "Price", "Stock Status", "Image" }, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				return (column == 7) ? Icon.class : Object.class;
			}
		};

		productsTable = new JTable(tableModel);
		productsTable.setRowHeight(80);

		JScrollPane scrollPane = new JScrollPane(productsTable);
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(searchPanel, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Load products data
		loadProducts("SELECT * FROM products");

		return panel;
	}

	// Load products
	private void loadProducts(String query) throws NataliaException {
		// Clear previous data
		tableModel.setRowCount(0);

		try (Connection connection = DatabaseConnector.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				// Load and scale the image directly from path
				ImageIcon icon = new ImageIcon(new ImageIcon(rs.getString("product_img")).getImage()
						.getScaledInstance(70, 70, Image.SCALE_SMOOTH));

				tableModel.addRow(new Object[] { rs.getString("product_code"), rs.getString("product_name"),
						rs.getString("category"), rs.getString("product_brand"), rs.getString("description"),
						rs.getDouble("price"), rs.getString("stock_status"), icon, });
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Method to load a specific product by product ID
	private void searchProductById(int productId) throws NataliaException {
	    String query = "SELECT * FROM products WHERE product_id = ?";
	    tableModel.setRowCount(0); // Clear previous data

	    try (Connection connection = DatabaseConnector.getConnection();
	         PreparedStatement stmt = connection.prepareStatement(query)) {
	        
	        stmt.setInt(1, productId); // Set product_id in query
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // Load and scale the image directly from path
	                ImageIcon icon = new ImageIcon(new ImageIcon(rs.getString("product_img")).getImage()
	                        .getScaledInstance(70, 70, Image.SCALE_SMOOTH));

	                tableModel.addRow(new Object[] { rs.getString("product_code"), rs.getString("product_name"),
	                        rs.getString("category"), rs.getString("product_brand"), rs.getString("description"),
	                        rs.getDouble("price"), rs.getString("stock_status"), icon });
	            } else {
	                JOptionPane.showMessageDialog(this, "No product found with ID: " + productId, "Search Result", JOptionPane.INFORMATION_MESSAGE);
	            }
	        }
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "Error searching product: " + ex.getMessage(), "Database Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	/******************
	 * Update Product *
	 ******************/
	private JPanel createUpdateProductPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel productPanel = new JPanel(new GridLayout(11, 2, 5, 5));

		// Product ID entry specific to this view
		// JPanel topPanel = new JPanel();
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		// Title Label
		String updateTitle = "<html><div style='text-align: center; font-size: 16px; font-family: DialogInput;'>Update Product</div></html>";
		JLabel titleLabel = new JLabel(updateTitle);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(titleLabel);

		// Product ID Label and Input Field
		JPanel productIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center align this line
		productIdPanel.add(new JLabel("Enter Product ID:"));
		product_id = new JTextField(10);
		productIdPanel.add(product_id);
		JButton searchButton = new JButton("Search");
		productIdPanel.add(searchButton);

		// Add to topPanel
		topPanel.add(productIdPanel);
		panel.add(topPanel, BorderLayout.NORTH);

		productPanel.add(new JLabel("Product Code:"));
		JTextField product_code = new JTextField();
		productPanel.add(product_code);

		productPanel.add(new JLabel("Category:"));
		JTextField category = new JTextField();
		productPanel.add(category);

		productPanel.add(new JLabel("Product Brand:"));
		JTextField product_brand = new JTextField();
		productPanel.add(product_brand);

		productPanel.add(new JLabel("Product Name:"));
		JTextField product_name = new JTextField();
		productPanel.add(product_name);

		productPanel.add(new JLabel("Description:"));
		JTextField description = new JTextField();
		productPanel.add(description);

		productPanel.add(new JLabel("Price:"));
		JTextField price = new JTextField();
		productPanel.add(price);

		productPanel.add(new JLabel("Discount %:"));
		JTextField discount_percent = new JTextField();
		productPanel.add(discount_percent);

		productPanel.add(new JLabel("Stock:"));
		JTextField stock = new JTextField();
		productPanel.add(stock);

		productPanel.add(new JLabel("Product Image:"));
		JTextField product_img = new JTextField();
		productPanel.add(product_img);

		productPanel.add(new JLabel(""));
		JButton updateButton = new JButton("Update");
		productPanel.add(updateButton);

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
					if (updateProductDetails(productId, productCode, categoryText, brand, name, desc, priceText,
							discount, stockText, img)) {
						JOptionPane.showMessageDialog(this, "Product ID " + productId + " updated successfully.",
								"Success", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "No product found with ID: " + productId, "Update Failed",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (NumberFormatException | HeadlessException | NataliaException ex) {
					JOptionPane.showMessageDialog(this, "Invalid Product ID. Please enter a numeric value.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a Product ID to update.", "Input Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		panel.add(productPanel, BorderLayout.CENTER);
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
			JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/*********************
	 * Delete Product *
	 *******************/

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
						JOptionPane.showMessageDialog(this, "Product ID " + productId + " deleted successfully.",
								"Success", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "No product found with ID: " + productId, "Deletion Failed",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (NumberFormatException | HeadlessException | NataliaException ex) {
					JOptionPane.showMessageDialog(this, "Invalid Product ID. Please enter a numeric value.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a Product ID to delete.", "Input Error",
						JOptionPane.WARNING_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static void main(String[] args) throws NataliaException {
		AdminScreen adminScreen = new AdminScreen(1);

	}

}
