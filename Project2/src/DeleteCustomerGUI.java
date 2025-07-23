import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class DeleteCustomerGUI {
    public static void main(String[] args) {
        // نافذة الواجهة
        JFrame frame = new JFrame("Delete user");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));

        // حقل إدخال الاسم
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter (Customer ID) to delete:");

        JTextField nameField = new JTextField(20);
        panel.add(label);
        panel.add(nameField);

        // زر الحذف
        JButton deleteButton = new JButton("Delete");

        // رسالة الحالة
        JLabel statusLabel = new JLabel("");

        // عند الضغط على زر الحذف
        deleteButton.addActionListener(e -> {
            /*
            deleteButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // الكود اللي بينفّذ لما المستخدم يضغط الزر
    }
});

             */
            String targetID = nameField.getText().trim();  // نسميه targetID بدل targetName

            if (targetID.isEmpty()) {
                statusLabel.setText("Please enter a name");
                return;
            }

            File inputFile = new File("Customer.txt");
            File tempFile = new File("Customer_temp.txt");

            boolean found = false;

            try (
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length > 0 && parts[0].equalsIgnoreCase(targetID)) {
                        found = true;
                        continue; // لا نكتب السطر
                    }

                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException ex) {
                statusLabel.setText("Error while process");
                return;
            }

            // استبدال الملف الأصلي بالملف المؤقت
            if (found) {
                if (inputFile.delete()) {
                    tempFile.renameTo(inputFile);
                    statusLabel.setText("deleted successfully");
                } else {
                    statusLabel.setText("error while converting file");
                }
            } else {
                tempFile.delete(); // لا حاجة للملف المؤقت إذا لم نجد المستخدم
                statusLabel.setText("Customer does not exist!");
            }
        });

        // إضافة العناصر للواجهة
        frame.add(panel);
        frame.add(deleteButton);
        frame.add(statusLabel);

        frame.setVisible(true);
    }
}
