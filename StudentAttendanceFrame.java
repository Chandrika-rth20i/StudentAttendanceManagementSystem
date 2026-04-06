import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class StudentAttendanceFrame extends JFrame {

    private String username;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel, presentLabel,
                   absentLabel, percentageLabel;

    public StudentAttendanceFrame(String username) {
        this.username = username;
        setTitle("My Attendance");
        setSize(750, 600);
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
        JLabel titleLabel = new JLabel("MY ATTENDANCE",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable center content
        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.setBackground(new Color(44, 62, 80));

        // Summary cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        cardsPanel.setBackground(new Color(44, 62, 80));
        cardsPanel.setBorder(
            BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JPanel card1 = createCard("Total Classes", "0",
            new Color(52, 73, 94));
        JPanel card2 = createCard("Present", "0",
            new Color(39, 174, 96));
        JPanel card3 = createCard("Absent", "0",
            new Color(169, 50, 38));
        JPanel card4 = createCard("Attendance %", "0%",
            new Color(41, 128, 185));

        totalLabel      = (JLabel) card1.getComponent(1);
        presentLabel    = (JLabel) card2.getComponent(1);
        absentLabel     = (JLabel) card3.getComponent(1);
        percentageLabel = (JLabel) card4.getComponent(1);

        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        cardsPanel.add(card4);
        centerContent.add(cardsPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"#", "Subject", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable(table);

        // Color rows by status
        table.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel,
                boolean foc, int row, int col) {
                super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                String status = t.getValueAt(row, 3).toString();
                setBackground(status.equals("Present") ?
                    new Color(39, 174, 96) :
                    new Color(169, 50, 38));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(
            new Color(52, 152, 219), 1));
        tableScroll.getViewport().setBackground(
            new Color(52, 73, 94));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(44, 62, 80));
        tablePanel.setBorder(
            BorderFactory.createEmptyBorder(0, 15, 10, 15));
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        centerContent.add(tablePanel, BorderLayout.CENTER);

        // Wrap everything in scroll pane
        JScrollPane mainScroll = new JScrollPane(centerContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setBackground(new Color(44, 62, 80));
        mainScroll.getViewport().setBackground(
            new Color(44, 62, 80));
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        // Back button - always visible at bottom
        JPanel btnPanel = new JPanel(new GridLayout(1, 1));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 15, 10, 15));

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

        loadMyAttendance();
        setVisible(true);
    }

    private void loadMyAttendance() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT student_id FROM users WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int studentId = rs.getInt("student_id");

                PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT * FROM attendance " +
                    "WHERE student_id=? " +
                    "ORDER BY attendance_date DESC");
                ps2.setInt(1, studentId);
                ResultSet rs2 = ps2.executeQuery();

                int total = 0, present = 0, absent = 0;
                while (rs2.next()) {
                    total++;
                    String status = rs2.getString("status");
                    if (status.equals("Present")) present++;
                    else absent++;
                    tableModel.addRow(new Object[]{
                        total,
                        rs2.getString("subject"),
                        rs2.getDate("attendance_date"),
                        status
                    });
                }

                totalLabel.setText(String.valueOf(total));
                presentLabel.setText(String.valueOf(present));
                absentLabel.setText(String.valueOf(absent));

                double perc = total > 0 ?
                    Math.round((present * 100.0 / total)
                        * 10.0) / 10.0 : 0;
                percentageLabel.setText(perc + "%");

                if (perc < 75) {
                    percentageLabel.setForeground(
                        new Color(255, 100, 100));
                } else {
                    percentageLabel.setForeground(
                        new Color(100, 255, 100));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private JPanel createCard(String title,
        String value, Color bgColor) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(bgColor);
        card.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLbl = new JLabel(title,
            SwingConstants.CENTER);
        titleLbl.setForeground(new Color(214, 234, 248));
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(titleLbl);

        JLabel valueLbl = new JLabel(value,
            SwingConstants.CENTER);
        valueLbl.setForeground(Color.WHITE);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        card.add(valueLbl);

        return card;
    }

    private void styleTable(JTable t) {
        t.setBackground(new Color(52, 73, 94));
        t.setForeground(Color.WHITE);
        t.setGridColor(new Color(44, 62, 80));
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(41, 128, 185));
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(new Color(41, 128, 185));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));
    }
}