import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String username, String role) {
        setTitle("Dashboard - Student Attendance System");
        setSize(440, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel titleLabel = new JLabel(
            "STUDENT ATTENDANCE SYSTEM", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(44, 62, 80));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 45, 20, 45));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        centerPanel.add(welcomeLabel, gbc);

        // Build menu based on role
        if (role.equals("teacher") || role.equals("admin")) {
            buildTeacherMenu(centerPanel, gbc);
        } else {
            buildStudentMenu(centerPanel, gbc, username);
        }

        setVisible(true);
    }

    private void buildTeacherMenu(JPanel panel, GridBagConstraints gbc) {
        String[] labels = {
            "Manage Students",
            "Mark Attendance",
            "View Attendance",
            "Attendance Report",
            "Logout"
        };

        Color[] colors = {
            new Color(52, 73, 94),
            new Color(52, 73, 94),
            new Color(52, 73, 94),
            new Color(52, 73, 94),
            new Color(169, 50, 38)
        };

        JButton[] buttons = new JButton[5];
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = createButton(labels[i], colors[i]);
            gbc.gridy = i + 1;
            gbc.insets = new Insets(6, 0, 6, 0);
            panel.add(buttons[i], gbc);
        }

        buttons[0].addActionListener(e -> {
            dispose(); new StudentFrame();
        });
        buttons[1].addActionListener(e -> {
            dispose(); new AttendanceFrame();
        });
        buttons[2].addActionListener(e -> {
            dispose(); new ViewAttendanceFrame();
        });
        buttons[3].addActionListener(e -> {
            dispose(); new ReportFrame();
        });
        buttons[4].addActionListener(e -> logout());
    }

    private void buildStudentMenu(JPanel panel,
        GridBagConstraints gbc, String username) {

        String[] labels = {
            "My Attendance",
            "My Profile",
            "Logout"
        };

        Color[] colors = {
            new Color(52, 73, 94),
            new Color(52, 73, 94),
            new Color(169, 50, 38)
        };

        JButton[] buttons = new JButton[3];
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = createButton(labels[i], colors[i]);
            gbc.gridy = i + 1;
            gbc.insets = new Insets(6, 0, 6, 0);
            panel.add(buttons[i], gbc);
        }

        buttons[0].addActionListener(e -> {
            dispose(); new StudentAttendanceFrame(username);
        });
        buttons[1].addActionListener(e -> {
            dispose(); new StudentProfileFrame(username);
        });
        buttons[2].addActionListener(e -> logout());
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(320, 45));
        return btn;
    }
}