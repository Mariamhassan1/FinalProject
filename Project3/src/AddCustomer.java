import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCustomer extends JFrame {
    private CustomerManager manager;

    public AddCustomer(CustomerManager manager) {
        this.manager = manager;
        setTitle("Add Customer");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField familySizeField = new JTextField();

        // ComboBox for priority
        String[] priorities = {
                "1-War/fire/Earthquakes",
                "2-family without breadwinner(imprisonment, death, or divorce).",
                "3-Famine / Drought and desertification",
                "4-Displacement/ asylum",
                "5-Elderly"
        };
        JComboBox<String> priorityBox = new JComboBox<>(priorities);

        // ComboBox for location
        String[] locations = {"Jerusalem", "EastJerusalem", "WestJerusalem"};
        JComboBox<String> locationBox = new JComboBox<>(locations);

        JTextField notesField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Family Size:"));
        panel.add(familySizeField);
        panel.add(new JLabel("Emergency Priority:"));
        panel.add(priorityBox);
        panel.add(new JLabel("Location:"));
        panel.add(locationBox);
        panel.add(new JLabel("Notes (optional):"));
        panel.add(notesField);

        JButton addButton = new JButton("Add Customer");
        JLabel statusLabel = new JLabel("");
        panel.add(addButton);
        panel.add(statusLabel);

        add(panel);

        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        // === Add Button Listener ===
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = "C" + System.currentTimeMillis();  // رقم ID فريد
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String familySizeStr = familySizeField.getText().trim();
                String location = (String) locationBox.getSelectedItem();
                String levelStr = (String) priorityBox.getSelectedItem();

                // extract first digit from priority selection (e.g. "2-family..." → 2)
                String levelChar = levelStr.charAt(0) + "";
                int emergencyLevel = Integer.parseInt(levelChar);

                String notes = notesField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || familySizeStr.isEmpty()) {
                    statusLabel.setText("Please fill all required fields.");
                    return;
                }

                if (!isValidEmail(email, pattern)) {
                    statusLabel.setText("Please enter a valid email address.");
                    return;
                }

                try {
                    int familySize = Integer.parseInt(familySizeStr);

                    Customer customer = new Customer(name, email, familySize, emergencyLevel, location, notes);
                    boolean added = manager.addCustomer(customer);

                    if (added) {
                        JOptionPane.showMessageDialog(null, "Customer added successfully!");
                        nameField.setText("");
                        emailField.setText("");
                        familySizeField.setText("");
                        notesField.setText("");
                        statusLabel.setText("");
                        priorityBox.setSelectedIndex(0);
                        locationBox.setSelectedIndex(0);
                    } else {
                        statusLabel.setText("Customer already exists.");
                    }

                } catch (NumberFormatException ex) {
                    statusLabel.setText("Family size must be a number.");
                }
            }
        });
    }

    private static boolean isValidEmail(String email, Pattern pattern) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
