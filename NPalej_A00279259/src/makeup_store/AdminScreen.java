package makeup_store;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AdminScreen extends JFrame {
	
	public AdminScreen (int customer_id) {
		
		setTitle("Admin");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        // Email input
        panel.add(new JLabel("Admin Screen"));

        add(panel);
        setVisible(true);
    }

}
