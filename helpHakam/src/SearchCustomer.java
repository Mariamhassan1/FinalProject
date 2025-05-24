import javax.swing.*;
import java.awt.*;

public class SearchCustomer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Search for a user");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Fields: Name and ID
        JPanel topPanel = new JPanel();
        JLabel idLabel = new JLabel("Customer ID:");
        JTextField idField = new JTextField(10);
        JLabel nameLabel = new JLabel("The Name: ");
        JTextField nameField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        // Add the tools to the panel
        topPanel.add(idLabel);
        topPanel.add(idField);
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(searchButton);

        // To display the result after doing the search
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Layout panels
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Get customer manager instance
        CustomerManager manager = CustomerManager.getInstance();

        searchButton.addActionListener(e -> {
            String inputId = idField.getText().trim();
            String inputName = nameField.getText().trim();

            // Validate input
            if (inputId.isEmpty() && inputName.isEmpty()) {
                resultArea.setText("Please enter either ID or name.");
                return;
            }

            Customer foundCustomer = null;

            // Search logic
            if (!inputId.isEmpty() && !inputName.isEmpty()) {
                // Search by both ID and name
                foundCustomer = manager.findCustomer(inputId, inputName);
            } else if (!inputId.isEmpty()) {
                // Search by ID only
                foundCustomer = manager.findCustomerById(inputId);
            } else {
                // Search by name only
                foundCustomer = manager.findCustomerByName(inputName);
            }

            // Display result
            if (foundCustomer != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(" User found:\n");
                sb.append("ID: ").append(foundCustomer.getId()).append("\n");
                sb.append("The Name: ").append(foundCustomer.getName()).append("\n");
                sb.append("e-mail: ").append(foundCustomer.getEmail()).append("\n");
                sb.append("Number of family members: ").append(foundCustomer.getFamilySize()).append("\n");
                sb.append("Emergency level: ").append(foundCustomer.getEmergencyLevel()).append("\n");
                sb.append("The location: ").append(foundCustomer.getLocation()).append("\n");
                sb.append("Notes: ").append(foundCustomer.getNotes()).append("\n");

                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("User not found.");
            }
        });

        frame.setVisible(true);
    }
}