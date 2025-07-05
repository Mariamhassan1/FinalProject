import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerManagementSystem implements CustomerManager.CustomerUpdateListener {
    private static CustomerManagementSystem instance;
    private static JTable customerTable;
    private static DefaultTableModel tableModel;
    private static JFrame allCustomersFrame;
    private static JLabel countLabel;
    private static JFrame mainFrame; // Make main frame accessible

    // Modern Color Scheme
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);      // Modern Blue
    private static final Color PRIMARY_DARK = new Color(52, 112, 186);       // Darker Blue
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);   // Modern Gray
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);       // Success Green
    private static final Color DANGER_COLOR = new Color(220, 53, 69);        // Danger Red
    private static final Color WARNING_COLOR = new Color(255, 193, 7);       // Warning Yellow
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);  // Light Background
    private static final Color CARD_COLOR = Color.WHITE;                     // Card Background
    private static final Color TEXT_COLOR = new Color(33, 37, 41);           // Dark Text
    private static final Color MUTED_TEXT = new Color(108, 117, 125);        // Muted Text

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set modern look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Custom UI properties for modern look
                UIManager.put("Button.focus", new Color(0, 0, 0, 0));
                UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20));

            } catch (Exception e) {
                System.out.println("Could not set system look and feel, using default.");
            }

            instance = new CustomerManagementSystem();
            instance.createWelcomeWindow(); // Start with welcome window instead
        });
    }

    private void createWelcomeWindow() {
        // Create the welcome frame
        mainFrame = new JFrame("Customer Management System - Welcome");
        mainFrame.setSize(600, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Create welcome content
        JPanel welcomeContent = createWelcomeContent();

        mainFrame.add(welcomeContent, BorderLayout.CENTER);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        System.out.println("Customer Management System Welcome Screen started!");
    }

    private JPanel createWelcomeContent() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(BACKGROUND_COLOR);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Title Section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel welcomeTitle = new JLabel("Welcome to");
        welcomeTitle.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        welcomeTitle.setForeground(MUTED_TEXT);
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel systemTitle = new JLabel("Customer Management System");
        systemTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        systemTitle.setForeground(PRIMARY_COLOR);
        systemTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please select your access level");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(welcomeTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(systemTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        titlePanel.add(subtitle);

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer button (only add operation)
        JPanel customerCard = createWelcomeCard("👤", "Customer Access", "Add new customer registration", SUCCESS_COLOR, () -> openAddCustomerWindow());

        // Admin button (full access)
        JPanel adminCard = createWelcomeCard("🔒", "Admin Access", "Full system management", PRIMARY_COLOR, () -> showAdminLogin());

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1;
        buttonPanel.add(customerCard, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(adminCard, gbc);

        welcomePanel.add(titlePanel, BorderLayout.NORTH);
        welcomePanel.add(buttonPanel, BorderLayout.CENTER);

        return welcomePanel;
    }

    private JPanel createWelcomeCard(String icon, String title, String description, Color accentColor, Runnable action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with rounded corners
                g2.setColor(CARD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Subtle shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 15, 15);

                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        card.setPreferredSize(new Dimension(400, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Left side - icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // Right side - text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(MUTED_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);

        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);

        // Accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));

        card.add(accentLine, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Click animation
                Timer timer = new Timer(100, evt -> {
                    card.setBackground(CARD_COLOR);
                    card.repaint();
                    ((Timer) evt.getSource()).stop();
                });
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                card.repaint();
                timer.start();

                // Execute action
                SwingUtilities.invokeLater(action);
            }
        });

        return card;
    }

    private void showAdminLogin() {
        // Create admin login dialog
        JDialog loginDialog = new JDialog(mainFrame, "Admin Authentication", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel headerLabel = new JLabel("Admin Login");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_COLOR);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(250, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Error label
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER_COLOR);

        // Layout form components
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3; gbc.weightx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        formPanel.add(errorLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR);
        JButton cancelButton = createStyledButton("Cancel", SECONDARY_COLOR);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (authenticateAdmin(email, password)) {
                loginDialog.dispose();
                showMainOperations();
            } else {
                errorLabel.setText("Invalid email or password");
                passwordField.setText("");
            }
        });

        cancelButton.addActionListener(e -> loginDialog.dispose());

        // Enter key support
        passwordField.addActionListener(e -> loginButton.doClick());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        loginDialog.add(headerPanel, BorderLayout.NORTH);
        loginDialog.add(formPanel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);

        loginDialog.setLocationRelativeTo(mainFrame);
        loginDialog.setVisible(true);
    }

    private boolean authenticateAdmin(String email, String password) {
        return (email.equals("malakabedallah50@gmail.com") && password.equals("malak123")) ||
                (email.equals("maryamimranhassan@gmail.com") && password.equals("maryam123"));
    }

    private void showMainOperations() {
        // Hide the welcome window and show the main operations
        mainFrame.dispose();
        createMainWindow();
    }

    private void createMainWindow() {
        // Create the main frame with modern styling
        mainFrame = new JFrame("Customer Management System - Admin Panel");
        mainFrame.setSize(900, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Register for updates
        CustomerManager manager = CustomerManager.getInstance();
        manager.addUpdateListener(this);

        // Create beautiful header
        JPanel headerPanel = createHeaderPanel(manager);

        // Create stunning main content
        JPanel mainContent = createMainContent();

        // Create footer with logout button
        JPanel footerPanel = createFooterPanel(manager);

        // Add components to main frame
        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(mainContent, BorderLayout.CENTER);
        mainFrame.add(footerPanel, BorderLayout.SOUTH);

        // Center the window
        mainFrame.setLocationRelativeTo(null);

        // Add subtle shadow effect (if supported)
        mainFrame.setVisible(true);

        System.out.println("Beautiful Customer Management System started!");
    }

    private JPanel createHeaderPanel(CustomerManager manager) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(PRIMARY_COLOR);

        JLabel iconLabel = new JLabel("[CMS]");
        iconLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Customer Management System - Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(PRIMARY_COLOR);

        countLabel = new JLabel("Total: " + manager.getCustomerCount() + " customers");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countLabel.setForeground(Color.WHITE);
        countLabel.setOpaque(true);
        countLabel.setBackground(PRIMARY_DARK);
        countLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Round the corners of count label
        countLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        statsPanel.add(countLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Create beautiful action cards
        JPanel addCard = createActionCard("+", "Add Customer", "Register new customers", SUCCESS_COLOR, () -> openAddCustomerWindow());
        JPanel searchCard = createActionCard("?", "Search Customer", "Find existing customers", PRIMARY_COLOR, () -> openSearchCustomerWindow());
        JPanel deleteCard = createActionCard("X", "Delete Customer", "Remove customers", DANGER_COLOR, () -> openDeleteCustomerWindow());
        JPanel viewCard = createActionCard("=", "View All", "Display all customers", SECONDARY_COLOR, () -> displayAllCustomers());

        // Layout the cards in a 2x2 grid
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1;
        mainContent.add(addCard, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        mainContent.add(searchCard, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainContent.add(deleteCard, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        mainContent.add(viewCard, gbc);

        return mainContent;
    }

    private JPanel createActionCard(String icon, String title, String description, Color accentColor, Runnable action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with rounded corners
                g2.setColor(CARD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Subtle shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);

                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        card.setPreferredSize(new Dimension(300, 200));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon and title panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        topPanel.add(iconLabel, BorderLayout.CENTER);
        topPanel.add(titleLabel, BorderLayout.SOUTH);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(MUTED_TEXT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));

        card.add(topPanel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        card.add(accentLine, BorderLayout.NORTH);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Click animation
                Timer timer = new Timer(100, evt -> {
                    card.setBackground(CARD_COLOR);
                    card.repaint();
                    ((Timer) evt.getSource()).stop();
                });
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                card.repaint();
                timer.start();

                // Execute action
                SwingUtilities.invokeLater(action);
            }
        });

        return card;
    }

    private JPanel createFooterPanel(CustomerManager manager) {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(52, 58, 64));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel statusLabel = new JLabel("Data: customer.txt | Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(173, 181, 189));

        // Right panel with version and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(52, 58, 64));

        JLabel versionLabel = new JLabel("Version 2.0");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        versionLabel.setForeground(new Color(173, 181, 189));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(DANGER_COLOR);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                createWelcomeWindow();
            }
        });

        rightPanel.add(versionLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rightPanel.add(logoutButton);

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(rightPanel, BorderLayout.EAST);

        return footerPanel;
    }

    private void openAddCustomerWindow() {
        SwingUtilities.invokeLater(() -> AddCustomer.main(null));
    }

    private void openSearchCustomerWindow() {
        SwingUtilities.invokeLater(() -> SearchCustomer.main(null));
    }

    private void openDeleteCustomerWindow() {
        SwingUtilities.invokeLater(() -> DeleteCustomer.main(null));
    }

    private void updateCountLabel() {
        if (countLabel != null) {
            CustomerManager manager = CustomerManager.getInstance();
            countLabel.setText("Total: " + manager.getCustomerCount() + " customers");
        }
    }

    public static void displayAllCustomers() {
        CustomerManager manager = CustomerManager.getInstance();

        if (allCustomersFrame != null) {
            refreshCustomerTable();
            allCustomersFrame.setVisible(true);
            allCustomersFrame.toFront();
            return;
        }

        allCustomersFrame = new JFrame("All Customers");
        allCustomersFrame.setSize(1200, 700);
        allCustomersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allCustomersFrame.setLayout(new BorderLayout());
        allCustomersFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Beautiful header for table window
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(PRIMARY_COLOR);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel tableTitle = new JLabel("Customer Database");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tableTitle.setForeground(Color.WHITE);

        JLabel tableCount = new JLabel("Total: " + manager.getCustomerCount() + " customers");
        tableCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCount.setForeground(Color.WHITE);

        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(tableCount, BorderLayout.EAST);

        // Create beautiful table
        String[] columnNames = {"ID", "Name", "Email", "Family Size", "Emergency Level", "Location", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customizeTable(customerTable);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Beautiful control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JButton closeButton = createStyledButton("Close", SECONDARY_COLOR);
        closeButton.addActionListener(e -> allCustomersFrame.setVisible(false));

        controlPanel.add(closeButton);

        allCustomersFrame.add(tableHeader, BorderLayout.NORTH);
        allCustomersFrame.add(scrollPane, BorderLayout.CENTER);
        allCustomersFrame.add(controlPanel, BorderLayout.SOUTH);

        refreshCustomerTable();
        allCustomersFrame.setLocationRelativeTo(null);
        allCustomersFrame.setVisible(true);
    }

    private static void customizeTable(JTable table) {
        // Modern table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(222, 226, 230));
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setPreferredSize(new Dimension(0,45));

        // Custom cell renderer for alternating rows
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 249, 250));
                    }
                }

                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void refreshCustomerTable() {
        if (customerTable != null && tableModel != null) {
            CustomerManager manager = CustomerManager.getInstance();

            tableModel.setRowCount(0);

            for (Customer customer : manager.getAllCustomers()) {
                Object[] row = {
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getFamilySize(),
                        customer.getEmergencyLevel() + " - " + customer.getEmergencyLevelDescription(),
                        customer.getLocation(),
                        customer.getNotes().isEmpty() ? "No notes" : customer.getNotes()
                };
                tableModel.addRow(row);
            }

            tableModel.fireTableDataChanged();
        }
    }

    // CustomerUpdateListener implementation
    @Override
    public void onCustomerAdded(Customer customer) {
        SwingUtilities.invokeLater(() -> {
            updateCountLabel();
            refreshCustomerTable();
        });
    }

    @Override
    public void onCustomerDeleted(String customerId) {
        SwingUtilities.invokeLater(() -> {
            updateCountLabel();
            refreshCustomerTable();
        });
    }

    @Override
    public void onDataRefreshed() {
        SwingUtilities.invokeLater(() -> {
            updateCountLabel();
            refreshCustomerTable();
        });
    }

    public static void onChildWindowClosed() {
        if (instance != null) {
            instance.updateCountLabel();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddCustomer {
    private static JFrame frame;

    //This line is used to ensure that the function is called and to use the thread to avoid facing any issues in the interface display//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAddCustomerWindow());
    }

    private static void createAddCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);   // To check if the email is valid //

        String[] locations = {"Jerusalem", "East Jerusalem", "West Jerusalem"};
        String[] priorities = {
                "1 - Displacement/Asylum",
                "2 - Disabled people-can't work",
                "3 - Elderly",
                "4 - Family without breadwinner/Unemployment",
        };

        frame = new JFrame("Add New Customer");
        frame.setSize(500, 700); // Increased height from 600 to 700
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame = null;
                CustomerManagementSystem.onChildWindowClosed();
            }
        });

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(74, 144, 226));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Customer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel - Using simple GridLayout with SCROLL PANE
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create simple form fields
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField familyField = new JTextField(20);
        JComboBox<String> locationBox = new JComboBox<>(locations);
        JComboBox<String> priorityBox = new JComboBox<>(priorities);
        JTextField notesField = new JTextField(20);

        // Add labels and fields
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Family Size:"));
        formPanel.add(familyField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationBox);
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityBox);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(notesField);

        // PUT FORM IN SCROLL PANE so it can never push buttons off screen
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create FIXED BOTTOM PANEL for buttons - ALWAYS VISIBLE
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(Color.LIGHT_GRAY);

        // Create BEAUTIFUL, CREATIVE button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(52, 58, 64)); // Dark elegant background
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(0, 123, 255)), // Blue accent line
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        // CREATE STUNNING BUTTONS WITH GRADIENT-LIKE EFFECTS
        JButton addButton = new JButton("✓ ADD CUSTOMER");
        addButton.setPreferredSize(new Dimension(180, 50));
        addButton.setBackground(new Color(40, 167, 69)); // Success green
        addButton.setForeground(Color.BLACK); // BLACK text on green
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(25, 135, 84), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton clearButton = new JButton("🔄 CLEAR FORM");
        clearButton.setPreferredSize(new Dimension(140, 50));
        clearButton.setBackground(new Color(108, 117, 125)); // Modern gray
        clearButton.setForeground(Color.BLACK); // BLACK text on gray
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(73, 80, 87), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(25, 135, 84)); // Darker green on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(40, 167, 69)); // Original green
            }
        });

        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(73, 80, 87)); // Darker gray on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(108, 117, 125)); // Original gray
            }
        });

        // ADD BUTTONS TO PANEL
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Space between buttons
        buttonPanel.add(clearButton);

        // Status label - BEAUTIFUL and MODERN
        JLabel statusLabel = new JLabel("Ready to add customer", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(173, 181, 189)); // Light text
        statusLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));
        statusLabel.setBackground(new Color(52, 58, 64)); // Same as button panel
        statusLabel.setOpaque(true);

        // Combine buttons and status - FIXED at bottom
        bottomContainer.add(buttonPanel, BorderLayout.CENTER);
        bottomContainer.add(statusLabel, BorderLayout.SOUTH);

        CustomerManager manager = CustomerManager.getInstance();

        // Add Customer button action
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String familyText = familyField.getText().trim();
            String location = (String) locationBox.getSelectedItem();
            String levelStr = (String) priorityBox.getSelectedItem();
            String notes = notesField.getText().trim();

            // Simple validation
            if (name.isEmpty()) {
                statusLabel.setText("Please enter name");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (email.isEmpty()) {
                statusLabel.setText("Please enter email");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (!isValidEmail(email, pattern)) {
                statusLabel.setText("Please enter valid email");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (familyText.isEmpty()) {
                statusLabel.setText("Please enter family size");
                statusLabel.setForeground(Color.RED);
                return;
            }

            try {
                int familySize = Integer.parseInt(familyText);

                if (familySize < 1) {
                    statusLabel.setText("Family size must be at least 1");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                if (manager.findCustomerByNameAndEmail(name, email)) {
                    statusLabel.setText("Customer already exists");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                String levelChar = levelStr.charAt(0) + "";
                int emergencyLevel = Integer.parseInt(levelChar);

                Customer customer = new Customer(name, email, familySize, emergencyLevel, location, notes);

                if (manager.addCustomer(customer)) {
                    statusLabel.setText("Customer added successfully! ID: " + customer.getId());
                    statusLabel.setForeground(Color.GREEN);

                    // Clear form
                    nameField.setText("");
                    emailField.setText("");
                    familyField.setText("");
                    notesField.setText("");
                    locationBox.setSelectedIndex(0);
                    priorityBox.setSelectedIndex(0);

                    JOptionPane.showMessageDialog(frame,
                            "Customer '" + customer.getName() + "' added successfully!\nID: " + customer.getId(),  //!?ID
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    statusLabel.setText("Error adding customer");
                    statusLabel.setForeground(Color.RED);
                }

            } catch (NumberFormatException ex) {
                statusLabel.setText("Family size must be a number");
                statusLabel.setForeground(Color.RED);
            }
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            nameField.setText("");
            emailField.setText("");
            familyField.setText("");
            notesField.setText("");
            locationBox.setSelectedIndex(0);
            priorityBox.setSelectedIndex(0);
            statusLabel.setText("Form cleared");
            statusLabel.setForeground(Color.BLACK);
        });

        // LAYOUT - GUARANTEES BUTTONS ARE ALWAYS VISIBLE
        frame.add(headerPanel, BorderLayout.NORTH);        // Header at top
        frame.add(formScrollPane, BorderLayout.CENTER);    // Scrollable form in center
        frame.add(bottomContainer, BorderLayout.SOUTH);    // FIXED buttons at bottom

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Force repaint to ensure everything shows
        frame.revalidate();
        frame.repaint();

        // Debug: Print to console
        System.out.println("Add Customer window created!");
        System.out.println("Button panel added: " + (buttonPanel != null));
        System.out.println("Add button created: " + (addButton != null));
    }

    private static boolean isValidEmail(String email, Pattern pattern) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DeleteCustomer {
    private static JFrame frame;

    // Beautiful Color Scheme
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color MUTED_TEXT = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createDeleteCustomerWindow());
    }

    private static void createDeleteCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        frame = new JFrame("Delete Customer");
        frame.setSize(650, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame = null;
                CustomerManagementSystem.onChildWindowClosed();
            }
        });

        // Beautiful header
        JPanel headerPanel = createHeaderPanel();

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search section
        JPanel searchSection = createSearchSection();
        JTextField idField = createStyledTextField("Enter customer ID to delete...");
        addSearchField(searchSection, "[ID] Customer ID", idField);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        JButton searchButton = createStyledButton("Find Customer", PRIMARY_COLOR, "?");
        JButton deleteButton = createStyledButton("Delete Customer", DANGER_COLOR, "X");
        JButton clearButton = createStyledButton("Clear", MUTED_TEXT, "C");

        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearButton);

        // Customer preview panel
        JPanel previewSection = createPreviewSection();
        JTextArea previewArea = createStyledTextArea();
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "Customer Information",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                TEXT_COLOR
        ));
        previewScroll.setPreferredSize(new Dimension(0, 200));
        previewSection.add(previewScroll, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel statusLabel = new JLabel("Enter a customer ID to begin deletion process");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusLabel);

        // Layout main panel
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(BACKGROUND_COLOR);
        topSection.add(searchSection, BorderLayout.NORTH);
        topSection.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(previewSection, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        CustomerManager manager = CustomerManager.getInstance();

        // Initially disable delete button
        deleteButton.setEnabled(false);

        // Search button action
        searchButton.addActionListener(e -> {
            String targetID = idField.getText().trim();

            if (targetID.isEmpty() || targetID.equals("Enter customer ID to delete...")) {
                statusLabel.setText("[ERROR] Please enter a customer ID");
                statusLabel.setForeground(DANGER_COLOR);
                previewArea.setText("");
                deleteButton.setEnabled(false);
                return;
            }

            Customer customer = manager.findCustomerById(targetID);
            if (customer != null) {
                displayCustomerPreview(previewArea, customer);
                statusLabel.setText("[FOUND] Customer found - Review information before deleting");
                statusLabel.setForeground(SUCCESS_COLOR);
                deleteButton.setEnabled(true);
            } else {
                previewArea.setText("[NOT FOUND] Customer Not Found\n" +
                        "===============================================\n" +
                        "No customer found with ID: " + targetID + "\n\n" +
                        "SUGGESTIONS:\n" +
                        "• Double-check the customer ID\n" +
                        "• Verify the customer exists in the system\n" +
                        "• Use 'View All Customers' to browse available IDs\n" +
                        "• Contact administrator if customer should exist");
                statusLabel.setText("[ERROR] Customer not found - Please check the ID");
                statusLabel.setForeground(DANGER_COLOR);
                deleteButton.setEnabled(false);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            String targetID = idField.getText().trim();

            if (targetID.isEmpty() || targetID.equals("Enter customer ID to delete...")) {
                statusLabel.setText("[ERROR] Please enter a customer ID");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            Customer customer = manager.findCustomerById(targetID);
            if (customer == null) {
                statusLabel.setText("[ERROR] Customer not found - Please search first");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            // Show confirmation dialog with customer details
            int choice = showConfirmationDialog(customer);

            if (choice == JOptionPane.YES_OPTION) {
                if (manager.deleteCustomer(targetID)) {
                    statusLabel.setText("[OK] Customer deleted successfully");
                    statusLabel.setForeground(SUCCESS_COLOR);

                    // Clear form
                    clearForm(idField, previewArea, deleteButton);

                    // Show success message
                    showSuccessDialog(customer);
                    idField.requestFocus();
                } else {
                    statusLabel.setText("[ERROR] Could not delete customer - Please try again");
                    statusLabel.setForeground(DANGER_COLOR);
                }
            }
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            clearForm(idField, previewArea, deleteButton);
            statusLabel.setText("Form cleared - Enter customer ID to begin");
            statusLabel.setForeground(MUTED_TEXT);
            idField.requestFocus();
        });

        // Enter key support for search
        idField.addActionListener(e -> searchButton.doClick());
        frame.getRootPane().setDefaultButton(searchButton);

        // Initial content
        previewArea.setText("Customer Preview\n" +
                "===============================================\n" +
                "Enter a customer ID above and click 'Find Customer'\n" +
                "to preview customer information before deletion.\n\n" +
                "SAFETY FEATURES:\n" +
                "• Customer information preview before deletion\n" +
                "• Confirmation dialog with customer details\n" +
                "• Automatic form clearing after deletion\n" +
                "• Cannot delete without finding customer first\n\n" +
                "WARNING: Deletion is permanent and cannot be undone!");

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> idField.requestFocus());
    }
    //GUI header in the deletion gui
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DANGER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("X Delete Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Remove customers from the system (PERMANENT)");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(DANGER_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        JLabel warningLabel = new JLabel("! CAUTION: This action cannot be undone");
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningLabel.setForeground(new Color(255, 255, 255, 220));
        warningLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(warningLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createSearchSection() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        "Step 1: Find Customer",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        return searchPanel;
    }

    private static JPanel createPreviewSection() {
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(BACKGROUND_COLOR);
        previewPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        return previewPanel;
    }

    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        return buttonPanel;
    }

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_COLOR);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setText(placeholder);
        field.setForeground(MUTED_TEXT);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(MUTED_TEXT);
                }
            }
        });

        return field;
    }

    private static JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(CARD_COLOR);
        textArea.setForeground(TEXT_COLOR);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        return textArea;
    }

    private static JButton createStyledButton(String text, Color color, String icon) {
        JButton button = new JButton(icon + " " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = color;
                if (getModel().isPressed()) {
                    bgColor = color.darker();
                } else if (getModel().isRollover()) {
                    bgColor = color.brighter();
                } else if (!isEnabled()) {
                    bgColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
                }

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(isEnabled() ? Color.WHITE : new Color(255, 255, 255, 150));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(140, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static void addSearchField(JPanel parent, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(TEXT_COLOR);
        fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        parent.add(fieldPanel);
    }

    private static void displayCustomerPreview(JTextArea previewArea, Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("[PREVIEW] Customer Information\n");
        sb.append("===============================================\n");
        sb.append("The following customer will be PERMANENTLY deleted:\n\n");

        sb.append("ID: ").append(customer.getId()).append("\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        sb.append("Family Size: ").append(customer.getFamilySize()).append(" members\n");
        sb.append("Emergency Level: ").append(customer.getEmergencyLevel());
        sb.append(" (").append(getEmergencyLevelDescription(customer.getEmergencyLevel())).append(")\n");
        sb.append("Location: ").append(customer.getLocation()).append("\n");
        sb.append("Notes: ").append(customer.getNotes().isEmpty() ? "No additional notes" : customer.getNotes()).append("\n");

        sb.append("===============================================\n");
        sb.append("WARNING: This deletion is PERMANENT!\n");
        sb.append("Click 'Delete Customer' to proceed with deletion.\n");
        sb.append("A confirmation dialog will appear before final deletion.");

        previewArea.setText(sb.toString());
    }

    private static int showConfirmationDialog(Customer customer) {
        String message = String.format(
                "PERMANENT DELETION CONFIRMATION\n\n" +
                        "Are you absolutely sure you want to delete this customer?\n\n" +
                        "Customer Details:\n" +
                        "Name: %s\n" +
                        "Email: %s\n" +
                        "ID: %s\n" +
                        "Family Size: %d members\n" +
                        "Emergency Level: %d\n\n" +
                        "WARNING: This action CANNOT be undone!\n" +
                        "The customer will be permanently removed from the system.",
                customer.getName(),
                customer.getEmail(),
                customer.getId(),
                customer.getFamilySize(),
                customer.getEmergencyLevel()
        );

        return JOptionPane.showConfirmDialog(
                frame,
                message,
                "Confirm Permanent Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
    }

    private static void showSuccessDialog(Customer customer) {
        String message = String.format(
                "Customer Successfully Deleted\n\n" +
                        "The following customer has been permanently removed:\n\n" +
                        "Name: %s\n" +
                        "ID: %s\n\n" +
                        "The customer data has been removed from the system.",
                customer.getName(),
                customer.getId()
        );

        JOptionPane.showMessageDialog(frame, message, "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void clearForm(JTextField idField, JTextArea previewArea, JButton deleteButton) {
        idField.setText("Enter customer ID to delete...");
        idField.setForeground(MUTED_TEXT);
        previewArea.setText("Customer Preview\n" +
                "===============================================\n" +
                "Enter a customer ID above and click 'Find Customer'\n" +
                "to preview customer information before deletion.\n\n" +
                "SAFETY FEATURES:\n" +
                "• Customer information preview before deletion\n" +
                "• Confirmation dialog with customer details\n" +
                "• Automatic form clearing after deletion\n" +
                "• Cannot delete without finding customer first\n\n" +
                "WARNING: Deletion is permanent and cannot be undone!");
        deleteButton.setEnabled(false);
    }

    private static String getEmergencyLevelDescription(int level) {
        switch (level) {
            case 1: return "Displacement/Asylum";
            case 2: return "Disabled people-can't work";
            case 3: return "Elderly";
            case 4: return "Family without breadwinner/Unemployment";

            default: return "Unknown level";
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchCustomer {
    private static JFrame frame;

    // Beautiful Color Scheme
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color MUTED_TEXT = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createSearchCustomerWindow());
    }

    private static void createSearchCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        frame = new JFrame("Search Customer");
        frame.setSize(700, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame = null;
                CustomerManagementSystem.onChildWindowClosed();
            }
        });

        // Beautiful header
        JPanel headerPanel = createHeaderPanel();

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search criteria panel
        JPanel searchPanel = createSearchPanel();
        JTextField idField = createStyledTextField("Enter customer ID...");
        JTextField nameField = createStyledTextField("Enter customer name...");

        addSearchField(searchPanel, "[ID] Customer ID", idField);
        addSearchField(searchPanel, "[Name] Customer Name", nameField);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR, "?");
        JButton clearButton = createStyledButton("Clear", MUTED_TEXT, "X");

        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(clearButton);

        // Results panel
        JPanel resultsPanel = createResultsPanel();
        JTextArea resultArea = createStyledTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "Search Results",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                TEXT_COLOR
        ));
        scrollPane.setBackground(CARD_COLOR);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel statusLabel = new JLabel("Enter Customer ID or Name to search");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusLabel);

        // Layout main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(resultsPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        CustomerManager manager = CustomerManager.getInstance();

        // Search button action
        searchButton.addActionListener(e -> {
            String inputId = idField.getText().trim();
            String inputName = nameField.getText().trim();

            statusLabel.setForeground(TEXT_COLOR);

            // Clean placeholder text
            if (inputId.equals("Enter customer ID...")) inputId = "";
            if (inputName.equals("Enter customer name...")) inputName = "";

            if (inputId.isEmpty() && inputName.isEmpty()) {
                resultArea.setText("[ERROR] SEARCH ERROR\n\n" +
                        "Please enter either Customer ID or Name to search.\n\n" +
                        "Tip: You can search by ID, name, or both for precise results.");
                statusLabel.setText("[ERROR] Validation Error: No search criteria provided");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            Customer foundCustomer = null;
            String searchType = "";

            try {
                if (!inputId.isEmpty() && !inputName.isEmpty()) {
                    foundCustomer = manager.findCustomer(inputId, inputName);
                    searchType = "ID and Name";
                } else if (!inputId.isEmpty()) {
                    foundCustomer = manager.findCustomerById(inputId);
                    searchType = "ID";
                } else {
                    foundCustomer = manager.findCustomerByName(inputName);
                    searchType = "Name";
                }

                if (foundCustomer != null) {
                    displayCustomerResult(resultArea, foundCustomer, searchType);
                    statusLabel.setText("[OK] Search completed successfully - Customer found!");
                    statusLabel.setForeground(SUCCESS_COLOR);
                } else {
                    displayNoResultsFound(resultArea, searchType, inputId, inputName);
                    statusLabel.setText("[ERROR] No customer found with the provided criteria");
                    statusLabel.setForeground(DANGER_COLOR);
                }
            } catch (Exception ex) {
                resultArea.setText("[ERROR] SEARCH ERROR\n\n" +
                        "An unexpected error occurred during search:\n" + ex.getMessage() + "\n\n" +
                        "Please try again or contact support if the problem persists.");
                statusLabel.setText("[ERROR] Search error occurred");
                statusLabel.setForeground(DANGER_COLOR);
            }
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            clearFields(idField, nameField);
            resultArea.setText("Ready to search...\n\n" +
                    "Enter a Customer ID or Name above and click 'Search' to find customer information.\n\n" +
                    "Tips:\n" +
                    "• Use exact ID for fastest results\n" +
                    "• Name search is case-insensitive\n" +
                    "• You can search by both ID and name for verification");
            statusLabel.setText("Form cleared - Ready for new search");
            statusLabel.setForeground(MUTED_TEXT);
            idField.requestFocus();
        });

        // Enter key support
        idField.addActionListener(e -> searchButton.doClick());
        nameField.addActionListener(e -> searchButton.doClick());
        frame.getRootPane().setDefaultButton(searchButton);

        // Initial content
        resultArea.setText("Ready to search...\n\n" +
                "Enter a Customer ID or Name above and click 'Search' to find customer information.\n\n" +
                "Tips:\n" +
                "• Use exact ID for fastest results\n" +
                "• Name search is case-insensitive\n" +
                "• You can search by both ID and name for verification");

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> idField.requestFocus());
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("? Search Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Find customers by ID or name");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        "Search Criteria",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        return searchPanel;
    }

    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        return buttonPanel;
    }

    private static JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BACKGROUND_COLOR);

        return resultsPanel;
    }

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_COLOR);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setText(placeholder);
        field.setForeground(MUTED_TEXT);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(MUTED_TEXT);
                }
            }
        });

        return field;
    }

    private static JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(CARD_COLOR);
        textArea.setForeground(TEXT_COLOR);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        return textArea;
    }

    private static JButton createStyledButton(String text, Color color, String icon) {
        JButton button = new JButton(icon + " " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = color;
                if (getModel().isPressed()) {
                    bgColor = color.darker();
                } else if (getModel().isRollover()) {
                    bgColor = color.brighter();
                }

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(130, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static void addSearchField(JPanel parent, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(TEXT_COLOR);
        fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        parent.add(fieldPanel);
    }

    private static void displayCustomerResult(JTextArea resultArea, Customer customer, String searchType) {
        StringBuilder sb = new StringBuilder();
        sb.append("[FOUND] CUSTOMER FOUND\n");
        sb.append("===============================================\n");
        sb.append("Search Method: ").append(searchType).append("\n\n");

        sb.append("ID: ").append(customer.getId()).append("\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        sb.append("Family Size: ").append(customer.getFamilySize()).append(" members\n");
        sb.append("Emergency Level: ").append(customer.getEmergencyLevel());
        sb.append(" (").append(getEmergencyLevelDescription(customer.getEmergencyLevel())).append(")\n");
        sb.append("Location: ").append(customer.getLocation()).append("\n");
        sb.append("Notes: ").append(customer.getNotes().isEmpty() ? "No additional notes" : customer.getNotes()).append("\n");

        sb.append("===============================================\n");
        sb.append("Record Status: Active\n");
        sb.append("Use this information to assist the customer.");

        resultArea.setText(sb.toString());
    }

    private static void displayNoResultsFound(JTextArea resultArea, String searchType, String inputId, String inputName) {
        StringBuilder sb = new StringBuilder();
        sb.append("❌ NO CUSTOMER FOUND\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("Search Method: ").append(searchType).append("\n");
        if (!inputId.isEmpty()) {
            sb.append("Searched ID: ").append(inputId).append("\n");
        }
        if (!inputName.isEmpty()) {
            sb.append("Searched Name: ").append(inputName).append("\n");
        }
        sb.append("\n🔍 SEARCH SUGGESTIONS:\n");
        sb.append("• Double-check the spelling of the name\n");
        sb.append("• Verify the customer ID format\n");
        sb.append("• Try searching with just first name or last name\n");
        sb.append("• Check if the customer exists in 'View All Customers'\n");
        sb.append("• Ensure the customer was properly registered\n\n");
        sb.append("💡 If you're sure the customer exists, they may need to be re-registered.");

        resultArea.setText(sb.toString());
    }

    private static void clearFields(JTextField idField, JTextField nameField) {
        idField.setText("Enter customer ID...");
        idField.setForeground(MUTED_TEXT);
        nameField.setText("Enter customer name...");
        nameField.setForeground(MUTED_TEXT);
    }

    private static String getEmergencyLevelDescription(int level) {
        switch (level) {
            case 1: return "Displacement/Asylum";
            case 2: return "Disabled people-can't work";
            case 3: return "Elderly";
            case 4: return "Family without breadwinner/Unemployment";

            default: return "Unknown level";
        }
    }
}

import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerManager class to handle all operations related to customers
 * Uses a Hashtable for in-memory operations and syncs with file storage
 * Now includes proper refresh mechanisms and update notifications
 */
public class CustomerManager {
    private static final String FILE_PATH = "src/customer.txt"; // Same directory as code files
    private Hashtable<String, Customer> customerTable;
    private List<CustomerUpdateListener> listeners;

    // Singleton instance, is part of the Singleton design pattern in Java (or similar object-oriented languages)
    private static CustomerManager instance;

    /**
     * Interface for update notifications
     */
    public interface CustomerUpdateListener {
        void onCustomerAdded(Customer customer);
        void onCustomerDeleted(String customerId);
        void onDataRefreshed();
    }

    /**
     * Get singleton instance of CustomerManager
     */
    //To ensure there is only one object (singleton) of the CustomerManager class in the whole application. If it's already created, it returns it; if not, it creates and returns it.
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    /**
     * Private constructor - loads data from file into hashtable
     */
    private CustomerManager() {
        customerTable = new Hashtable<>();
        listeners = new ArrayList<>();
        loadDataFromFile();
    }

    /**
     * Add update listener
     */
    /*Listener is an object that we add to group of listeners that we notify them when we do any action(add, delete, click,..etc)
    because all the listeners should know that the change was done after doing a specific action
    */
    public void addUpdateListener(CustomerUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove update listener
     */
    public void removeUpdateListener(CustomerUpdateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners of customer addition
     */
    private void notifyCustomerAdded(Customer customer) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerAdded(customer);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Notify listeners of customer deletion
     */
    private void notifyCustomerDeleted(String customerId) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerDeleted(customerId);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Notify listeners of data refresh
     */
    private void notifyDataRefreshed() {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onDataRefreshed();
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Refresh data from file - reload everything (internal use only)
     */
    private void refreshFromFile() {
        customerTable.clear();
        loadDataFromFile();
        notifyDataRefreshed();
    }

    /**
     * Load customer data from file into hashtable
     */
    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Customer data file '" + FILE_PATH + "' not found. Starting with empty database.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            int loadedCount = 0;

            System.out.println("Loading data from: " + FILE_PATH);

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                try {
                    // Split by comma and space ", "
                    String[] parts = line.split(", ");

                    if (parts.length >= 6) { // At least 6 parts required
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        int familySize = Integer.parseInt(parts[3].trim());
                        int emergencyLevel = Integer.parseInt(parts[4].trim());
                        String location = parts[5].trim();

                        // Handle notes - might contain commas or be empty
                        String notes = "";
                        if (parts.length > 6) {
                            StringBuilder notesBuilder = new StringBuilder();
                            for (int i = 6; i < parts.length; i++) {
                                if (i > 6) notesBuilder.append(", ");
                                notesBuilder.append(parts[i].trim());
                            }
                            notes = notesBuilder.toString();
                        }

                        Customer customer = new Customer(id, name, email, familySize, emergencyLevel, location, notes);
                        customerTable.put(id, customer);
                        loadedCount++;

                        System.out.println("Loaded: " + name + " (ID: " + id + ")");

                    } else {
                        System.err.println("Invalid data format at line " + lineNumber + " (not enough fields): " + line);
                        System.err.println("Expected format: ID, Name, Email, FamilySize, EmergencyLevel, Location, Notes");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format at line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }

            System.out.println("Successfully loaded " + loadedCount + " customers from " + lineNumber + " lines.");

            if (loadedCount == 0 && lineNumber > 0) {
                System.err.println("WARNING: File has " + lineNumber + " lines but no customers were loaded!");
                System.err.println("Please check the file format. Expected: ID, Name, Email, FamilySize, EmergencyLevel, Location, Notes");
            }

        } catch (IOException e) {
            System.err.println("Error reading customer data file '" + FILE_PATH + "': " + e.getMessage());
        }
    }

    /**
     * Save all customer data from hashtable back to file
     */
    public boolean saveDataToFile() {
        try {
            // Create backup of existing file
            File originalFile = new File(FILE_PATH);
            if (originalFile.exists()) {
                File backupFile = new File("customer.txt.backup");
                try (FileInputStream fis = new FileInputStream(originalFile);
                     FileOutputStream fos = new FileOutputStream(backupFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
            }

            // Write new data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (Customer customer : customerTable.values()) {
                    writer.write(customer.getId() + ", " +
                            customer.getName() + ", " +
                            customer.getEmail() + ", " +
                            customer.getFamilySize() + ", " +
                            customer.getEmergencyLevel() + ", " +
                            customer.getLocation() + ", " +
                            customer.getNotes());
                    writer.newLine();
                }
            }

            System.out.println("Customer data saved successfully to: " + FILE_PATH);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a new customer to the hashtable
     */
    public boolean addCustomer(Customer customer) {
        if (customerTable.containsKey(customer.getId())) {
            return false; // Customer with this ID already exists
        }

        customerTable.put(customer.getId(), customer);

        if (saveDataToFile()) {
            notifyCustomerAdded(customer);
            return true;
        } else {
            // Rollback if save failed
            customerTable.remove(customer.getId());
            return false;
        }
    }

    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String id) {
        return customerTable.get(id);
    }

    /**
     * Find customer by name (returns first match)
     */
    public Customer findCustomerByName(String name) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find customer by ID and name
     */
    public Customer findCustomer(String id, String name) {
        Customer customer = customerTable.get(id);
        if (customer != null && customer.getName().equalsIgnoreCase(name)) {
            return customer;
        }
        return null;
    }

    /**
     * Delete customer by id
     */
    public boolean deleteCustomer(String id) {
        if (!customerTable.containsKey(id)) {
            return false;
        }

        Customer removedCustomer = customerTable.remove(id);

        if (saveDataToFile()) {
            notifyCustomerDeleted(id);
            return true;
        } else {
            // Rollback if save failed
            customerTable.put(id, removedCustomer);
            return false;
        }
    }

    /**
     * Print all customers to console
     */
    public void printAllCustomers() {
        System.out.println("\nCustomer Details:");
        if (customerTable.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (String key : customerTable.keySet()) {
            Customer customer = customerTable.get(key);
            System.out.println("ID: " + key +
                    " | Name: " + customer.getName() +
                    " | Emergency Level: " + customer.getEmergencyLevel() +
                    " | Location: " + customer.getLocation());
        }
    }

    /**
     * Get all customers (returns fresh copy)
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerTable.values());
    }

    /**
     * Get customer count
     */
    public int getCustomerCount() {
        return customerTable.size();
    }

    /**
     * Check if customer already exists by name and email
     */
    public boolean findCustomerByNameAndEmail(String name, String email) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name) &&
                    customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get file path for debugging
     */
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Debug method - print file contents
     */
    public void debugPrintFileContents() {
        System.out.println("\n=== DEBUG: Raw file contents ===");
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + FILE_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + lineNum + ": [" + line + "]");
                lineNum++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        System.out.println("=== End raw file contents ===\n");
    }
}import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerManager class to handle all operations related to customers
 * Uses a Hashtable for in-memory operations and syncs with file storage
 * Now includes proper refresh mechanisms and update notifications
 */
public class CustomerManager {
    private static final String FILE_PATH = "src/customer.txt"; // Same directory as code files
    private Hashtable<String, Customer> customerTable;
    private List<CustomerUpdateListener> listeners;

    // Singleton instance, is part of the Singleton design pattern in Java (or similar object-oriented languages)
    private static CustomerManager instance;

    /**
     * Interface for update notifications
     */
    public interface CustomerUpdateListener {
        void onCustomerAdded(Customer customer);
        void onCustomerDeleted(String customerId);
        void onDataRefreshed();
    }

    /**
     * Get singleton instance of CustomerManager
     */
    //To ensure there is only one object (singleton) of the CustomerManager class in the whole application. If it's already created, it returns it; if not, it creates and returns it.
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    /**
     * Private constructor - loads data from file into hashtable
     */
    private CustomerManager() {
        customerTable = new Hashtable<>();
        listeners = new ArrayList<>();
        loadDataFromFile();
    }

    /**
     * Add update listener
     */
    /*Listener is an object that we add to group of listeners that we notify them when we do any action(add, delete, click,..etc)
    because all the listeners should know that the change was done after doing a specific action
    */
    public void addUpdateListener(CustomerUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Remove update listener
     */
    public void removeUpdateListener(CustomerUpdateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners of customer addition
     */
    private void notifyCustomerAdded(Customer customer) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerAdded(customer);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Notify listeners of customer deletion
     */
    private void notifyCustomerDeleted(String customerId) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerDeleted(customerId);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Notify listeners of data refresh
     */
    private void notifyDataRefreshed() {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onDataRefreshed();
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Refresh data from file - reload everything (internal use only)
     */
    private void refreshFromFile() {
        customerTable.clear();
        loadDataFromFile();
        notifyDataRefreshed();
    }

    /**
     * Load customer data from file into hashtable
     */
    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Customer data file '" + FILE_PATH + "' not found. Starting with empty database.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            int loadedCount = 0;

            System.out.println("Loading data from: " + FILE_PATH);

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                try {
                    // Split by comma and space ", "
                    String[] parts = line.split(", ");

                    if (parts.length >= 6) { // At least 6 parts required
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        int familySize = Integer.parseInt(parts[3].trim());
                        int emergencyLevel = Integer.parseInt(parts[4].trim());
                        String location = parts[5].trim();

                        // Handle notes - might contain commas or be empty
                        String notes = "";
                        if (parts.length > 6) {
                            StringBuilder notesBuilder = new StringBuilder();
                            for (int i = 6; i < parts.length; i++) {
                                if (i > 6) notesBuilder.append(", ");
                                notesBuilder.append(parts[i].trim());
                            }
                            notes = notesBuilder.toString();
                        }

                        Customer customer = new Customer(id, name, email, familySize, emergencyLevel, location, notes);
                        customerTable.put(id, customer);
                        loadedCount++;

                        System.out.println("Loaded: " + name + " (ID: " + id + ")");

                    } else {
                        System.err.println("Invalid data format at line " + lineNumber + " (not enough fields): " + line);
                        System.err.println("Expected format: ID, Name, Email, FamilySize, EmergencyLevel, Location, Notes");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format at line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }

            System.out.println("Successfully loaded " + loadedCount + " customers from " + lineNumber + " lines.");

            if (loadedCount == 0 && lineNumber > 0) {
                System.err.println("WARNING: File has " + lineNumber + " lines but no customers were loaded!");
                System.err.println("Please check the file format. Expected: ID, Name, Email, FamilySize, EmergencyLevel, Location, Notes");
            }

        } catch (IOException e) {
            System.err.println("Error reading customer data file '" + FILE_PATH + "': " + e.getMessage());
        }
    }

    /**
     * Save all customer data from hashtable back to file
     */
    public boolean saveDataToFile() {
        try {
            // Create backup of existing file
            File originalFile = new File(FILE_PATH);
            if (originalFile.exists()) {
                File backupFile = new File("customer.txt.backup");
                try (FileInputStream fis = new FileInputStream(originalFile);
                     FileOutputStream fos = new FileOutputStream(backupFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
            }

            // Write new data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (Customer customer : customerTable.values()) {
                    writer.write(customer.getId() + ", " +
                            customer.getName() + ", " +
                            customer.getEmail() + ", " +
                            customer.getFamilySize() + ", " +
                            customer.getEmergencyLevel() + ", " +
                            customer.getLocation() + ", " +
                            customer.getNotes());
                    writer.newLine();
                }
            }

            System.out.println("Customer data saved successfully to: " + FILE_PATH);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a new customer to the hashtable
     */
    public boolean addCustomer(Customer customer) {
        if (customerTable.containsKey(customer.getId())) {
            return false; // Customer with this ID already exists
        }

        customerTable.put(customer.getId(), customer);

        if (saveDataToFile()) {
            notifyCustomerAdded(customer);
            return true;
        } else {
            // Rollback if save failed
            customerTable.remove(customer.getId());
            return false;
        }
    }

    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String id) {
        return customerTable.get(id);
    }

    /**
     * Find customer by name (returns first match)
     */
    public Customer findCustomerByName(String name) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find customer by ID and name
     */
    public Customer findCustomer(String id, String name) {
        Customer customer = customerTable.get(id);
        if (customer != null && customer.getName().equalsIgnoreCase(name)) {
            return customer;
        }
        return null;
    }

    /**
     * Delete customer by id
     */
    public boolean deleteCustomer(String id) {
        if (!customerTable.containsKey(id)) {
            return false;
        }

        Customer removedCustomer = customerTable.remove(id);

        if (saveDataToFile()) {
            notifyCustomerDeleted(id);
            return true;
        } else {
            // Rollback if save failed
            customerTable.put(id, removedCustomer);
            return false;
        }
    }

    /**
     * Print all customers to console
     */
    public void printAllCustomers() {
        System.out.println("\nCustomer Details:");
        if (customerTable.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (String key : customerTable.keySet()) {
            Customer customer = customerTable.get(key);
            System.out.println("ID: " + key +
                    " | Name: " + customer.getName() +
                    " | Emergency Level: " + customer.getEmergencyLevel() +
                    " | Location: " + customer.getLocation());
        }
    }

    /**
     * Get all customers (returns fresh copy)
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerTable.values());
    }

    /**
     * Get customer count
     */
    public int getCustomerCount() {
        return customerTable.size();
    }

    /**
     * Check if customer already exists by name and email
     */
    public boolean findCustomerByNameAndEmail(String name, String email) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name) &&
                    customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get file path for debugging
     */
    public String getFilePath() {
        return FILE_PATH;
    }

    /**
     * Debug method - print file contents
     */
    public void debugPrintFileContents() {
        System.out.println("\n=== DEBUG: Raw file contents ===");
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + FILE_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + lineNum + ": [" + line + "]");
                lineNum++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        System.out.println("=== End raw file contents ===\n");
    }
}

