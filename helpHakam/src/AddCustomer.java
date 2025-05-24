import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddCustomer {
    public static void main(String[] args) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        String[] locations = {"Jerusalem", "East Jerusalem", "West Jerusalem"};
        JComboBox<String> locationBox = new JComboBox<>(locations);
        //<> Generics :to decide data type

        String[] priorities = {"1-War/fire/Earthquakes", "2-family without breadwinner(imprisonment, death, or divorce).", "3-Famine / Drought and desertification ", "4-Displacement/ asylum", "5-Elderly"};
        JComboBox<String> priorityBox = new JComboBox<>(priorities);

        JFrame frame = new JFrame("Add new customer");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 1));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField familyField = new JTextField();
        JTextField notesField = new JTextField();

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

        JButton addButton = new JButton("Add");
        frame.add(addButton);

        JLabel statusLabel = new JLabel("");
        frame.add(statusLabel);

        CustomerManager manager = CustomerManager.getInstance();

        addButton.addActionListener(e -> {  /**
         event handling:
         (using lambds)e--> when ...event ,do...{}
         instead of:
         deleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        // الكود اللي بينفّذ لما المستخدم يضغط الزر
        }
        });
         */
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String family = familyField.getText().trim();
            String location = (String) locationBox.getSelectedItem();
            String levelStr = (String) priorityBox.getSelectedItem();
            String levelChar = levelStr.charAt(0) + "";
            int emergencyLevel = Integer.parseInt(levelChar);
            String notes = notesField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || family.isEmpty()) {
                statusLabel.setText("Name, email and family size are required.");
                return;
            }

            if (!isValidEmail(email, pattern)) {
                statusLabel.setText("Please enter a valid email address.");
                return;
            }

            try {
                int familySize = Integer.parseInt(family);

                //  Check if user with same name already exists
                if (manager.findCustomerByNameAndEmail(name, email)) {
                    statusLabel.setText(" A customer with this name and email already exists.");
                    return;
                }

                Customer customer = new Customer(name, email, familySize, emergencyLevel, location, notes);

                if (manager.addCustomer(customer)) {
                    statusLabel.setText(" User added successfully! ID: " + customer.getId());

                    nameField.setText("");
                    emailField.setText("");
                    familyField.setText("");
                    notesField.setText("");
                } else {
                    statusLabel.setText(" Error: Could not add customer.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Family size must be a number.");
            }
        });

        frame.setVisible(true);
    }

    private static boolean isValidEmail(String email, Pattern pattern) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}