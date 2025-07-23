import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddCustomerGUI {
    public static void main(String[] args) {

        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
    //chiked lists

                String[] locations = {"Jerusalem", "EastJerusalem", "WestJerusalem"};
        JComboBox<String> locationBox = new JComboBox<>(locations);

        String[] priorities = {"1-War/fire/Earthquakes", "2-family without breadwinner(imprisonment, death, or divorce).", "3-Famine / Drought and desertification ", "4-Displacement/ asylum", "5-Elderly"};
        JComboBox<String> priorityBox = new JComboBox<>(priorities);


        // إنشاء الإطار (window)
        JFrame frame = new JFrame("Adding new customer");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 2));

        // خانات الإدخال (input fields)
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField familyField = new JTextField();
        JTextField levelField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField notesField = new JTextField();

        // تسميات الحقول (labels)
        frame.add(new JLabel("Name:"));
        frame.add(nameField);

        frame.add(new JLabel("e-mail:"));
        frame.add(emailField);

        frame.add(new JLabel("Family members:"));
        frame.add(familyField);

        frame.add(new JLabel("Location:"));
        frame.add(locationBox);

        frame.add(new JLabel("Emergency-Level:"));
        frame.add(priorityBox);

        frame.add(new JLabel("Additional notes:"));
        frame.add(notesField);

        // زر الإضافة
        JButton addButton = new JButton("Add!");
        frame.add(addButton);

        // حقل الحالة (status)
        JLabel statusLabel = new JLabel("");
        frame.add(statusLabel);

        // عند الضغط على زر "إضافة"
        addButton.addActionListener(e -> {
            String customerId = "C" + System.currentTimeMillis();  // رقم ID فريد
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String family = familyField.getText().trim();
            String location = (String) locationBox.getSelectedItem();
            String level = (String) priorityBox.getSelectedItem();
            String levelStr = (String) priorityBox.getSelectedItem();
            String levelChar = levelStr.charAt(0) + "";
            int emergencyLevel = Integer.parseInt(levelChar);
                    String notes = notesField.getText().trim();

            // تحقق بسيط من الحقول
            if (name.isEmpty() || email.isEmpty() || family.isEmpty() || level.isEmpty() || location.isEmpty()) {
                statusLabel.setText("All fields are required!");
                return;
            }
            //email check
            if (!isValidEmail(email, pattern)) {
                statusLabel.setText("Please enter a valid email address.");
                return;
            }

            // الكتابة إلى الملف
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Customer.txt", true))) {
                writer.write(customerId + ", " + name + ", " + email + ", " + family + ", " + level + ", " + location + ", " + notes);
                writer.newLine();
                statusLabel.setText("Customer was added successfully");

                // تفريغ الحقول بعد الإضافة
                nameField.setText("");
                emailField.setText("");
                familyField.setText("");
                levelField.setText("");
                locationField.setText("");
                notesField.setText("");
            } catch (IOException ex) {
                statusLabel.setText("error while adding customer");
            }
        });

        // عرض النافذة
        frame.setVisible(true);
    }
    private static boolean isValidEmail(String email, Pattern pattern) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
