import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class StudentProfileFrame extends JFrame {

    private String username;
    private JLabel photoLabel;
    private JLabel nameValue, idValue, classValue,
                   sectionValue, emailValue, phoneValue;

    public StudentProfileFrame(String username) {
        this.username = username;
        setTitle("My Profile");
        setSize(620, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("MY PROFILE",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable content
        JPanel scrollContent = new JPanel(new BorderLayout(20, 0));
        scrollContent.setBackground(new Color(44, 62, 80));
        scrollContent.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left photo panel
        JPanel photoPanel = new JPanel(new BorderLayout(0, 10));
        photoPanel.setBackground(new Color(44, 62, 80));
        photoPanel.setPreferredSize(new Dimension(180, 0));

        // Photo box
        JPanel photoBox = new JPanel(new BorderLayout());
        photoBox.setBackground(new Color(52, 73, 94));
        photoBox.setPreferredSize(new Dimension(160, 200));
        photoBox.setBorder(BorderFactory.createLineBorder(
            new Color(52, 152, 219), 2));

        photoLabel = new JLabel("No Photo",
            SwingConstants.CENTER);
        photoLabel.setForeground(new Color(127, 140, 141));
        photoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        photoBox.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(photoBox, BorderLayout.CENTER);

        // Upload button
        JButton uploadBtn = new JButton("Upload Photo");
        uploadBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        uploadBtn.setBackground(new Color(41, 128, 185));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFocusPainted(false);
        uploadBtn.setOpaque(true);
        uploadBtn.setBorderPainted(false);
        uploadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadBtn.setPreferredSize(new Dimension(160, 35));
        photoPanel.add(uploadBtn, BorderLayout.SOUTH);
        scrollContent.add(photoPanel, BorderLayout.WEST);

        // Right info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(52, 73, 94));
        infoPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20));
        scrollContent.add(infoPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel infoTitle = new JLabel("PERSONAL INFORMATION",
            SwingConstants.CENTER);
        infoTitle.setForeground(new Color(52, 152, 219));
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        infoPanel.add(infoTitle, gbc);

        nameValue    = addInfoRow(infoPanel, gbc, "Full Name", 1);
        idValue      = addInfoRow(infoPanel, gbc, "Student ID", 3);
        classValue   = addInfoRow(infoPanel, gbc, "Class", 5);
        sectionValue = addInfoRow(infoPanel, gbc, "Section", 7);
        emailValue   = addInfoRow(infoPanel, gbc, "Email", 9);
        phoneValue   = addInfoRow(infoPanel, gbc, "Phone", 11);

        // Wrap in scroll
        JScrollPane mainScroll = new JScrollPane(scrollContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setBackground(new Color(44, 62, 80));
        mainScroll.getViewport().setBackground(
            new Color(44, 62, 80));
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        // Back button always visible
        JPanel btnPanel = new JPanel(new GridLayout(1, 1));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 20, 10, 20));

        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backBtn.setBackground(new Color(41, 128, 185));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(0, 38));
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame(username, "student");
        });
        btnPanel.add(backBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        uploadBtn.addActionListener(e -> uploadPhoto());

        loadProfile();
        setVisible(true);
    }

    private JLabel addInfoRow(JPanel panel,
        GridBagConstraints gbc, String labelText, int gridy) {

        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(127, 140, 141));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = gridy;
        gbc.insets = new Insets(5, 0, 1, 0);
        panel.add(label, gbc);

        JLabel value = new JLabel("—");
        value.setForeground(Color.WHITE);
        value.setFont(new Font("Segoe UI", Font.BOLD, 13));
        value.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0,
                new Color(52, 73, 94)),
            BorderFactory.createEmptyBorder(2, 0, 4, 0)));
        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(value, gbc);

        return value;
    }

    private void loadProfile() {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT student_id FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");

                PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT * FROM students WHERE student_id=?");
                ps2.setInt(1, studentId);
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    nameValue.setText(
                        rs2.getString("student_name"));
                    idValue.setText(String.valueOf(
                        rs2.getInt("student_id")));
                    classValue.setText(
                        rs2.getString("class"));
                    sectionValue.setText(
                        rs2.getString("section"));
                    emailValue.setText(
                        rs2.getString("email") != null ?
                        rs2.getString("email") : "Not set");
                    phoneValue.setText(
                        rs2.getString("phone") != null ?
                        rs2.getString("phone") : "Not set");

                    // Load photo
                    byte[] photoBytes = rs2.getBytes("photo");
                    if (photoBytes != null) {
                        ImageIcon icon =
                            new ImageIcon(photoBytes);
                        Image scaled = icon.getImage()
                            .getScaledInstance(
                                160, 200,
                                Image.SCALE_SMOOTH);
                        photoLabel.setIcon(
                            new ImageIcon(scaled));
                        photoLabel.setText("");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Photo");
        fileChooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileInputStream fis =
                    new FileInputStream(selectedFile);
                byte[] photoBytes = fis.readAllBytes();
                fis.close();

                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT student_id FROM users " +
                    "WHERE username=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    PreparedStatement ps2 =
                        conn.prepareStatement(
                        "UPDATE students SET photo=? " +
                        "WHERE student_id=?");
                    ps2.setBytes(1, photoBytes);
                    ps2.setInt(2, studentId);
                    ps2.executeUpdate();

                    ImageIcon icon = new ImageIcon(photoBytes);
                    Image scaled = icon.getImage()
                        .getScaledInstance(
                            160, 200, Image.SCALE_SMOOTH);
                    photoLabel.setIcon(new ImageIcon(scaled));
                    photoLabel.setText("");

                    JOptionPane.showMessageDialog(this,
                        "Photo uploaded successfully!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
            }
        }
    }
}