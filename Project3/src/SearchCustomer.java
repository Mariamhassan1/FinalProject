import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchCustomer extends JFrame {
    private CustomerManager manager;

    public SearchCustomer(CustomerManager manager) {
        this.manager = manager;
        setTitle("Search Customer");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();

        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Customer ID:"));
        inputPanel.add(idField);

        JButton searchButton = new JButton("Search");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(searchButton, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        add(mainPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();

                if (name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Name and ID to search.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Customer customer = manager.searchByNameAndId(name, id);
                if (customer != null) {
                    resultArea.setText(customer.toString());
                } else {
                    resultArea.setText("Customer not found.");
                }
            }
        });
    }
}
