import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SearchCustomerGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Search");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // الحقول: الاسم و ID
        JPanel topPanel = new JPanel();
        JLabel idLabel = new JLabel("Customer ID:");
        JTextField idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        topPanel.add(idLabel);
        topPanel.add(idField);
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(searchButton);

        // منطقة عرض النتائج
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String inputId = idField.getText().trim();
            String inputName = nameField.getText().trim();

            if (inputId.isEmpty() || inputName.isEmpty()) {
                resultArea.setText("Enter Name & ID.");
                return;
            }

            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("Customer.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length >= 7 && parts[0].equalsIgnoreCase(inputId) && parts[1].equalsIgnoreCase(inputName)) {
                        // وجدنا المستخدم الصحيح
                        StringBuilder sb = new StringBuilder();
                        sb.append("Customer founded successfully!:\n");
                        sb.append("ID: ").append(parts[0]).append("\n");
                        sb.append("Name: ").append(parts[1]).append("\n");
                        sb.append("E-mail: ").append(parts[2]).append("\n");
                        sb.append("Family members: ").append(parts[3]).append("\n");
                        sb.append("Emergency level: ").append(parts[4]).append("\n");
                        sb.append("Location: ").append(parts[5]).append("\n");
                        sb.append("Notes: ").append(parts[6]).append("\n");

                        resultArea.setText(sb.toString());
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    resultArea.setText("Can't find customer.");
                }

            } catch (IOException ex) {
                resultArea.setText("Error while reading file");
            }
        });

        frame.setVisible(true);
    }
}
