package makeup_store;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MakeupStore extends JFrame implements ActionListener {
	
	private int customer_id;
	
	private JMenuItem exitOption, updateOption;
	
	public MakeupStore(int customer_id) throws NataliaException {
		// Sets the window title
		super( "Natalia Palej A00279259 - Makeup Store" ); 
		
		this.customer_id = customer_id;
		
		// Setup menu and its elements
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Options");
		exitOption = new JMenuItem("Exit");
		updateOption = new JMenuItem("Update Account");

		fileMenu.add(updateOption);
		fileMenu.add(exitOption);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		// Add a listener to the Exit Menu Item
		exitOption.addActionListener(this);
        updateOption.addActionListener(this);
		
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
	} 
				

}
