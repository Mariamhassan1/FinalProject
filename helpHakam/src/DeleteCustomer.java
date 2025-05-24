import javax.swing.*;
import java.awt.*;

public class DeleteCustomer {
    public static void main(String[] args) {
        // Interface window
        JFrame frame = new JFrame("Delete customer");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));

        // Input fields
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter customer ID:");
        JTextField idField = new JTextField(15);
        panel.add(label);
        panel.add(idField);

        // Delete button
        JButton deleteButton = new JButton("Delete");

        // Status message
        JLabel statusLabel = new JLabel("");

        // Get customer manager instance
        CustomerManager manager = CustomerManager.getInstance();

        // When delete button is pressed
        deleteButton.addActionListener(e -> {
            String targetID = idField.getText().trim();

            if (targetID.isEmpty()) {
                statusLabel.setText("Please enter an ID.");
                return;
            }

            // Try to delete the customer using manager
            if (manager.deleteCustomer(targetID)) {
                statusLabel.setText("The user has been successfully deleted.");
                idField.setText("");
            } else {
                statusLabel.setText("User does not exist.");
            }
        });

        // Add elements to the interface
        frame.add(panel);
        frame.add(deleteButton);
        frame.add(statusLabel);

        frame.setVisible(true);
    }
}