#FinalProject
public class ReadCustomerData {
    public static void main(String[] args) {
        String filePath = "Customer.txt"; // تأكدي من اسم الملف والمسار

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            System.out.println("Customer Data:");
            System.out.println("--------------");

            while ((line = reader.readLine()) != null) {
                System.out.println(line); // نطبع كل سطر كما هو
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
}

public class Customer {
    String name;
    String email;
    int familySize;
    int emergencyLevel;
    String location;
    String notes;

    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }

    public String toString() {
        return name + " | " + email + " | Level: " + emergencyLevel;
    }
}
import java.io.*;
import java.util.*;

public class AddCustomer {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("أدخل بيانات المستخدم الجديد:");

        System.out.print("الاسم: ");
        String name = input.nextLine();

        System.out.print("البريد الإلكتروني: ");
        String email = input.nextLine();

        System.out.print("عدد أفراد العائلة: ");
        int familySize = Integer.parseInt(input.nextLine());

        System.out.print("مستوى الطوارئ (1 إلى 3): ");
        int emergencyLevel = Integer.parseInt(input.nextLine());

        System.out.print("الموقع (jerusalem / eastjerusalem / westjerusalem): ");
        String location = input.nextLine();

        System.out.print("ملاحظات إضافية: ");
        String notes = input.nextLine();

        // نكتب السطر الجديد في الملف
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Customer.txt", true))) {
            writer.write(name + ", " + email + ", " + familySize + ", " + emergencyLevel + ", " + location + ", " + notes);
            writer.newLine();
            System.out.println("✅ تم إضافة المستخدم بنجاح!");
        } catch (IOException e) {
            System.out.println("❌ خطأ أثناء حفظ المستخدم.");
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AddCustomerGUI {
    public static void main(String[] args) {
        // إنشاء الإطار (window)
        JFrame frame = new JFrame("إضافة مستخدم جديد");
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
        frame.add(new JLabel("الاسم:"));
        frame.add(nameField);

        frame.add(new JLabel("البريد الإلكتروني:"));
        frame.add(emailField);

        frame.add(new JLabel("عدد أفراد العائلة:"));
        frame.add(familyField);

        frame.add(new JLabel("مستوى الطوارئ (1-3):"));
        frame.add(levelField);

        frame.add(new JLabel("الموقع (jerusalem / eastjerusalem / westjerusalem):"));
        frame.add(locationField);

        frame.add(new JLabel("ملاحظات إضافية:"));
        frame.add(notesField);

        // زر الإضافة
        JButton addButton = new JButton("إضافة");
        frame.add(addButton);

        // حقل الحالة (status)
        JLabel statusLabel = new JLabel("");
        frame.add(statusLabel);

        // عند الضغط على زر "إضافة"
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String family = familyField.getText().trim();
            String level = levelField.getText().trim();
            String location = locationField.getText().trim();
            String notes = notesField.getText().trim();

            // تحقق بسيط من الحقول
            if (name.isEmpty() || email.isEmpty() || family.isEmpty() || level.isEmpty() || location.isEmpty()) {
                statusLabel.setText("⚠️ جميع الحقول مطلوبة.");
                return;
            }

            // الكتابة إلى الملف
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Customer.txt", true))) {
                writer.write(name + ", " + email + ", " + family + ", " + level + ", " + location + ", " + notes);
                writer.newLine();
                statusLabel.setText("✅ تم إضافة المستخدم!");

                // تفريغ الحقول بعد الإضافة
                nameField.setText("");
                emailField.setText("");
                familyField.setText("");
                levelField.setText("");
                locationField.setText("");
                notesField.setText("");
            } catch (IOException ex) {
                statusLabel.setText("❌ خطأ أثناء حفظ البيانات.");
            }
        });

        // عرض النافذة
        frame.setVisible(true);
    }
}


// Main application entry point
public class Main {
    public static void main(String[] args) {
        // Get the CustomerManager instance
        CustomerManager manager = CustomerManager.getInstance();

        // Print loaded customer information
        System.out.println("Customer Management System");
        System.out.println("==========================");
        System.out.println("Total customers: " + manager.getCustomerCount());

        // Print all customers
        manager.printAllCustomers();
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddCustomer {
    public static void main(String[] args) {
        // Email validation regex pattern
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Location and priority options
        String[] locations = {"Jerusalem", "East Jerusalem", "West Jerusalem"};
        JComboBox<String> locationBox = new JComboBox<>(locations);

        String[] priorities = {"1-War", "2-Earthquakes", "3-Floods/Hurricanes", "4-Thunder Storm", "5-Fire"};
        JComboBox<String> priorityBox = new JComboBox<>(priorities);

        // Create the frame (window)
        JFrame frame = new JFrame("Add new customer");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 1));

        // Input fields
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField familyField = new JTextField();
        JTextField notesField = new JTextField();

        // Field labels
        frame.add(new JLabel("   Name:"));
        frame.add(nameField);

        frame.add(new JLabel("   e-mail:"));
        frame.add(emailField);

        frame.add(new JLabel("   Number of family members:"));
        frame.add(familyField);

        frame.add(new JLabel("   location:"));
        frame.add(locationBox);

        frame.add(new JLabel("   priority level:"));
        frame.add(priorityBox);

        frame.add(new JLabel("   Additional notes:"));
        frame.add(notesField);

        // Add button
        JButton addButton = new JButton("Add");
        frame.add(addButton);

        // Status label
        JLabel statusLabel = new JLabel("");
        frame.add(statusLabel);

        // Get customer manager instance
        CustomerManager manager = CustomerManager.getInstance();

        // When the "Add" button is pressed
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String family = familyField.getText().trim();
            String location = (String) locationBox.getSelectedItem();
            String levelStr = (String) priorityBox.getSelectedItem();
            String levelChar = (levelStr.charAt(0) + "");
            int emergencyLevel = Integer.parseInt(levelChar);
            String notes = notesField.getText().trim();

            // Validation
            if (name.isEmpty() || email.isEmpty() || family.isEmpty()) {
                statusLabel.setText("Name, email and family size are required.");
                return;
            }

            // Email validation
            if (!isValidEmail(email, pattern)) {
                statusLabel.setText("Please enter a valid email address.");
                return;
            }

            try {
                int familySize = Integer.parseInt(family);
                // Create new customer
                Customer customer = new Customer(name, email, familySize, emergencyLevel, location, notes);

                // Add customer using manager
                if (manager.addCustomer(customer)) {
                    statusLabel.setText("User added successfully! ID: " + customer.getId());

                    // Clear fields
                    nameField.setText("");
                    emailField.setText("");
                    familyField.setText("");
                    notesField.setText("");
                } else {
                    statusLabel.setText("Error: Customer ID already exists.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Family size must be a number.");
            }
        });

        // Display window
        frame.setVisible(true);
    }

    // Email validation function
    private static boolean isValidEmail(String email, Pattern pattern) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}


