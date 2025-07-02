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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerManagementSystem {
    public static void main(String[] args) {
        // Create the main frame
        JFrame mainFrame = new JFrame("Customer Management System");
        mainFrame.setSize(500, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Create info panel
        JPanel infoPanel = new JPanel();
        CustomerManager manager = CustomerManager.getInstance();
        JLabel countLabel = new JLabel("Total Customers: " + manager.getCustomerCount());
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
