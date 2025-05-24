import javax.swing.*;
import java.awt.*;

public class CustomerManagementSystem {
    public static void main(String[] args) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Customer Management System");
        mainFrame.setSize(800, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Create info panel
        JPanel infoPanel = new JPanel();
        CustomerManager manager = CustomerManager.getInstance();
        JLabel countLabel = new JLabel("HI !  Choose operation:" );
        infoPanel.add(countLabel);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create buttons
        JButton addButton = new JButton("Add New Customer");
        JButton searchButton = new JButton("Search Customer");
        JButton deleteButton = new JButton("Delete Customer");
        JButton viewAllButton = new JButton("View All Customers");

        // Add action listeners
        addButton.addActionListener(e -> {
            AddCustomer.main(null);
        });

        searchButton.addActionListener(e -> {
            SearchCustomer.main(null);
        });

        deleteButton.addActionListener(e -> {
            DeleteCustomer.main(null);
        });

        viewAllButton.addActionListener(e -> {
            displayAllCustomers();
        });

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewAllButton);

        // Add components to main frame
        mainFrame.add(infoPanel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.CENTER);

        // Show the frame
        mainFrame.setVisible(true);
    }

    private static void displayAllCustomers() {
        CustomerManager manager = CustomerManager.getInstance();

        JFrame frame = new JFrame("All Customers");
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Create table model
        String[] columnNames = {"ID", "Name", "Email", "Family Size", "Emergency Level", "Location", "Notes"};
        Object[][] data = new Object[manager.getCustomerCount()][7];

        // Fill data
        int index = 0;
        for (Customer customer : manager.getAllCustomers()) {
            data[index][0] = customer.getId();
            data[index][1] = customer.getName();
            data[index][2] = customer.getEmail();
            data[index][3] = customer.getFamilySize();
            data[index][4] = customer.getEmergencyLevel();
            data[index][5] = customer.getLocation();
            data[index][6] = customer.getNotes();
            index++;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}