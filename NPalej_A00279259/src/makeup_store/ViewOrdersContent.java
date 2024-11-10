package makeup_store;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class ViewOrdersContent extends JFrame {
	
	public ViewOrdersContent(int customer_id) throws NataliaException {
		
		setSize(800, 600);
		
		JPanel topPanel = new JPanel();
		
		// Set up orders table
		// the table is supposed to show the orders that have been placed
		// There should be a button beside each order, that button should say "VIEW"
		// it should open a popup screen that will display the details of that specific 
		// order. it should join tables, it should:
		// show date of order
		// product_code, product_name, quantity, total price for that product 
		// tax amount 
		// discount - if any ?
		// total price for the order
		// it should also be nicely structured, for example, prices should be to the 
		// right, tax and total cost should be to the right too
	}

}
