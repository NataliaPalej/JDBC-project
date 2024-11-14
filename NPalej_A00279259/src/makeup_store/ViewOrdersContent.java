package makeup_store;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ViewOrdersContent extends JFrame {
	
	private int customer_id;
	private JTable ordersTable;
	private DefaultTableModel tableModel;

	public ViewOrdersContent(int customer_id) throws NataliaException {
		
		this.customer_id = customer_id;

		setTitle("My Orders Details");
		setSize(800, 600);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		
        JLabel titleLabel = new JLabel("MY ORDERS", JLabel.CENTER);
        titleLabel.setFont(new Font("DialogInput", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Set up orders table with "VIEW" button
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Order Date", "Total Amount", "Tax", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
            	// Only "Action" column is editable
                return column == 4; 
            }
        };
        ordersTable = new JTable(tableModel);
		
        // Adding custom button renderer and editor for the "VIEW" button in the "Action" column
        ordersTable.getColumn("Action").setCellRenderer(new ViewButtonRenderer ());
        ordersTable.getColumn("Action").setCellEditor(new ViewButtonEditor (new JCheckBox()));
		
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        loadOrders();
        
        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            dispose();
        });
        
        mainPanel.add(exitButton, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
	}
	
	// Load orders from the database for customer
    private void loadOrders() throws NataliaException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT order_id, order_date, tax_amount, total_order_amount FROM orders WHERE customer_id = ?")) {

            stmt.setInt(1, customer_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("order_id"));
                row.add(rs.getTimestamp("order_date"));
                row.add(String.format("%.2f", rs.getDouble("total_order_amount")));
                row.add(String.format("%.2f", rs.getDouble("tax_amount")));
                // Action button label
                row.add("VIEW"); 

                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + ex.getMessage(), "\nDatabase Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Custom renderer for the button in the "Action" column
    class ViewButtonRenderer extends JButton implements TableCellRenderer {
        public ViewButtonRenderer() {
            setText("VIEW");
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom editor for the button in the "Action" column
    class ViewButtonEditor extends DefaultCellEditor {
        private JButton viewButton;
        private int selectedOrderId;

        public ViewButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            viewButton = new JButton("VIEW");
            viewButton.addActionListener(e -> {
				try {
					showOrderDetails(selectedOrderId);
				} catch (NataliaException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			});
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        	selectedOrderId = (int) tableModel.getValueAt(row, 0);
            return viewButton;
        }
    }

    // Show order details in a popup
    private void showOrderDetails(int order_id) throws NataliaException {
        StringBuilder detailsMessage = new StringBuilder("<html><div style='text-align: center;'><h3 style='font-family: DialogInput;'>Order Details</h3><br>");
        detailsMessage.append("<table style='width:100%; text-align: left;'>")
                .append("<tr><th>Product Code</th><th>Product Name</th><th>Quantity</th><th>Price</th></tr>");

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM customer_orders_view WHERE customer_id=? and order_id=?")) {

            stmt.setInt(1, customer_id);
            stmt.setInt(2, order_id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detailsMessage.append("<tr>")
                        .append("<td>").append(rs.getString("product_code")).append("</td>")
                        .append("<td>").append(rs.getString("product_name")).append("</td>")
                        .append("<td style='text-align: center;'>").append(rs.getInt("quantity")).append("</td>")
                        .append("<td style='text-align: right;'>").append(String.format("%.2f", rs.getDouble("total_item_cost"))).append("</td>");
                        
            }
            
            PreparedStatement getTax = connection.prepareStatement("SELECT tax_amount, total_order_amount FROM customer_orders_view WHERE customer_id=? AND order_id=?");
            getTax.setInt(1, customer_id);
            getTax.setInt(2, order_id);
            ResultSet rsGetTax = getTax.executeQuery();
            
            if (rsGetTax.next()) {
                detailsMessage.append("<tr><td colspan='3' style='text-align: right;'><strong>Tax:</strong></td>")
                              .append("<td style='text-align: right;'>")
                              .append(String.format("%.2f", rsGetTax.getDouble("tax_amount")))
                              .append("</td></tr>");
                detailsMessage.append("<tr><td colspan='3' style='text-align: right;'><strong>Total:</strong></td>")
                .append("<td style='text-align: right;'>")
                .append(String.format("%.2f", rsGetTax.getDouble("total_order_amount")))
                .append("</td></tr>");
            }
            
            detailsMessage.append("</table></div></html>");

            JOptionPane.showMessageDialog(this, new JLabel(detailsMessage.toString()), "Order Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading order details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
