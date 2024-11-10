package makeup_store;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

@SuppressWarnings("serial")
public class EditQuantity extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
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
        quantityField.setEditable(false); // Make it non-editable directly by the user
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
