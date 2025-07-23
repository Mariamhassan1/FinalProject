import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteCustomer extends JFrame {
    private CustomerManager manager;

    public DeleteCustomer(CustomerManager manager) {
        this.manager = manager;
        setTitle("Delete Customer");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField();
        JButton deleteButton = new JButton("Delete Customer");

        panel.add(new JLabel("Enter Customer ID to Delete:"));
        panel.add(idField);
        panel.add(deleteButton);

        add(panel);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a customer ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean deleted = manager.deleteCustomer(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
                    idField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Customer ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
