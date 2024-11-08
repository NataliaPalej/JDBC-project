package makeup_store;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MakeupStoreContent extends JInternalFrame implements ActionListener {
	
	private Container container;
	private JPanel makeupStorePanel;
	private JPanel exportButtonPanel;
	
	private Border lineBorder;
	
	private JLabel firstNameLabel, lastNameLabel, address1Label, address2Label, cityLabel, eircodeLabel, phoneLabel, emailLabel;
	private JTextField firstNameField, lastNameField, address1Field, address2Field, cityField, eircodeField, phoneField, emailField;
	
	
	public MakeupStoreContent() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
