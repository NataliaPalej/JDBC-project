package makeup_store;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class MakeupStore extends JFrame implements ActionListener {
	
	private int customer_id;
	
	private JMenuItem exitOption, updateOption, deleteOption;
	
	public MakeupStore(int customer_id) throws NataliaException {
		// Sets the window title
		super( "Natalia Palej A00279259 - Makeup Store" ); 
		
		this.customer_id = customer_id;
		
		// Setup menu and its elements
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Options");
		exitOption = new JMenuItem("Exit");
		updateOption = new JMenuItem("Update Account"); 
		deleteOption = new JMenuItem("Delete Account"); 

		fileMenu.add(updateOption);
		fileMenu.add(deleteOption);
		fileMenu.add(exitOption);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		// Add a listener to menu items
		exitOption.addActionListener(this);
        updateOption.addActionListener(this);
        deleteOption.addActionListener(this);
		
		// Create an instance of MakeupStoreContent class 
		MakeupStoreContent makeupStoreContent = new MakeupStoreContent();
		getContentPane().add(makeupStoreContent);
		
		setSize(1000, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(exitOption)){
			this.dispose();
		}
		if(e.getSource().equals(updateOption)){
			try {
				new UpdateCustomer(customer_id);
			} catch (NataliaException ex) {
				ex.printStackTrace();
			} 
		}
		if(e.getSource().equals(deleteOption)){
			if (e.getSource().equals(deleteOption)) {
			    int confirmation = JOptionPane.showConfirmDialog(
			        this,
			        "Are you sure you want to delete your account permanently?\nThis action cannot be undone.",
			        "Confirm Account Deletion",
			        JOptionPane.YES_NO_OPTION,
			        JOptionPane.WARNING_MESSAGE
			    );

			    if (confirmation == JOptionPane.YES_OPTION) {
			        try {
			            // Directly call deleteCustomer without opening UpdateCustomer form
			            UpdateCustomer updateCustomer = new UpdateCustomer(customer_id);
			            updateCustomer.deleteCustomer(customer_id);

			            JOptionPane.showMessageDialog(
			                this,
			                "Your account has been deleted successfully.",
			                "Account Deleted",
			                JOptionPane.INFORMATION_MESSAGE
			            );

			            // Close MakeupStore and open LoginScreen
			            this.dispose();
			            new LoginScreen(); // Redirects the user to the login screen after deletion

			        } catch (NataliaException | SQLException ex) {
			            JOptionPane.showMessageDialog(
			                this,
			                "Error deleting account: " + ex.getMessage(),
			                "Delete Failed",
			                JOptionPane.ERROR_MESSAGE
			            );
			        }
			    }
			}
		}
	}
}