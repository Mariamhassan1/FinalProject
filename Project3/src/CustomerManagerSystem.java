import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerManagerSystem extends JFrame {
    private CustomerManager manager;

    public CustomerManagerSystem() {
        manager = new CustomerManager();
        setTitle("Emergency Food Distribution System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton addButton = new JButton("Add Customer");
        JButton deleteButton = new JButton("Delete Customer");
        JButton searchButton = new JButton("Search Customer");
        JButton exitButton = new JButton("Exit");


        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(exitButton);

        add(panel);

        // فتح نافذة إضافة زبون
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCustomer(manager).setVisible(true);
            }
        });

        // فتح نافذة حذف زبون
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteCustomer(manager).setVisible(true);
            }
        });

        // فتح نافذة بحث عن زبون
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchCustomer(manager).setVisible(true);
            }
        });

        // الخروج من البرنامج
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // أو System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerManagerSystem().setVisible(true);
        });
    }
}
