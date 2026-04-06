import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Student Attendance Management System");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel titleLabel = new JLabel(
            "STUDENT ATTENDANCE SYSTEM",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(44, 62, 80));
        centerPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 45, 10, 45));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Username label
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(189, 195, 199));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        centerPanel.add(userLabel, gbc);

        // Username field
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBackground(new Color(52, 73, 94));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setPreferredSize(new Dimension(300, 38));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        centerPanel.add(usernameField, gbc);

        // Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(189, 195, 199));
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        centerPanel.add(passLabel, gbc);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBackground(new Color(52, 73, 94));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setPreferredSize(new Dimension(300, 38));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        centerPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        centerPanel.add(loginButton, gbc);

        // Status label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(231, 76, 60));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(statusLabel, gbc);

        // Footer
        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(44, 62, 80));
        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel footer = new JLabel(
            "© 2026 Aditya University  |  Batch 03");
        footer.setForeground(new Color(80, 100, 120));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        bottomPanel.add(footer);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(
            passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText(
                "Please enter username and password!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String query =
                "SELECT * FROM users WHERE username=? " +
                "AND password=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                dispose();
                new DashboardFrame(username, role);
            } else {
                statusLabel.setText(
                    "Invalid username or password!");
                passwordField.setText("");
            }

        } catch (SQLException e) {
            statusLabel.setText("DB Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Button.focus",
                new Color(0, 0, 0, 0));
            UIManager.put("ScrollBar.thumb",
                new Color(52, 152, 219));
            UIManager.put("ScrollBar.track",
                new Color(44, 62, 80));
            UIManager.put("ScrollBar.thumbHighlight",
                new Color(52, 152, 219));
            UIManager.put("ScrollBar.thumbDarkShadow",
                new Color(44, 62, 80));
            UIManager.put("ScrollBar.thumbShadow",
                new Color(44, 62, 80));
            UIManager.put("ComboBox.selectionBackground",
                new Color(52, 152, 219));
            UIManager.put("ComboBox.selectionForeground",
                Color.WHITE);
            UIManager.put("ComboBox.background",
                new Color(44, 62, 80));
            UIManager.put("ComboBox.foreground",
                Color.WHITE);
            UIManager.put("List.selectionBackground",
                new Color(52, 152, 219));
            UIManager.put("List.selectionForeground",
                Color.WHITE);
            UIManager.put("List.background",
                new Color(44, 62, 80));
            UIManager.put("List.foreground",
                Color.WHITE);
            UIManager.put("ScrollPane.background",
                new Color(44, 62, 80));
            UIManager.put("Viewport.background",
                new Color(44, 62, 80));
            UIManager.put("Table.focusCellHighlightBorder",
                BorderFactory.createEmptyBorder());
            UIManager.put("TableHeader.background",
                new Color(41, 128, 185));
            UIManager.put("TableHeader.foreground",
                Color.WHITE);
            UIManager.put("OptionPane.background",
                new Color(44, 62, 80));
            UIManager.put("Panel.background",
                new Color(44, 62, 80));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}