import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewAttendanceFrame extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public ViewAttendanceFrame() {
        setTitle("View Attendance Records");
        setSize(820, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel titleLabel = new JLabel("VIEW ATTENDANCE RECORDS",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(new Color(52, 73, 94));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel searchLabel = new JLabel("Search by Student ID:");
        searchLabel.setForeground(new Color(189, 195, 199));
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBackground(new Color(44, 62, 80));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setPreferredSize(new Dimension(180, 32));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        searchPanel.add(searchField);

        JButton searchBtn = createSmallButton("Search",
            new Color(41, 128, 185));
        JButton showAllBtn = createSmallButton("Show All",
            new Color(39, 174, 96));
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);

        totalLabel = new JLabel("Total Records: 0");
        totalLabel.setForeground(new Color(52, 152, 219));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchPanel.add(totalLabel);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Combine header and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Student ID", "Student Name",
                            "Subject", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        // Color rows by status
        table.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,
                Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                try {
                    String status = t.getValueAt(row, 5).toString();
                    setBackground(status.equals("Present") ?
                        new Color(39, 174, 96) : new Color(169, 50, 38));
                } catch (Exception e) {
                    setBackground(new Color(52, 73, 94));
                }
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(52, 152, 219), 1));
        scrollPane.getViewport().setBackground(new Color(52, 73, 94));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(44, 62, 80));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom button
        JPanel btnPanel = new JPanel(new GridLayout(1, 1, 10, 0));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        JButton backBtn = createButton("Back to Dashboard",
            new Color(41, 128, 185));
        btnPanel.add(backBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        searchBtn.addActionListener(e -> searchAttendance());
        showAllBtn.addActionListener(e -> {
            searchField.setText("");
            loadAllAttendance();
        });
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame("admin", "admin");
        });

        loadAllAttendance();
        setVisible(true);
    }

    private void loadAllAttendance() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            String query =
                "SELECT a.id, a.student_id, s.student_name, " +
                "a.subject, a.attendance_date, a.status " +
                "FROM attendance a " +
                "JOIN students s ON a.student_id = s.student_id " +
                "ORDER BY a.attendance_date DESC";
            ResultSet rs = conn.createStatement().executeQuery(query);
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("subject"),
                    rs.getDate("attendance_date"),
                    rs.getString("status")
                });
                count++;
            }
            totalLabel.setText("Total Records: " + count);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchAttendance() {
        String id = searchField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID!");
            return;
        }
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            String query =
                "SELECT a.id, a.student_id, s.student_name, " +
                "a.subject, a.attendance_date, a.status " +
                "FROM attendance a " +
                "JOIN students s ON a.student_id = s.student_id " +
                "WHERE a.student_id=? " +
                "ORDER BY a.attendance_date DESC";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("subject"),
                    rs.getDate("attendance_date"),
                    rs.getString("status")
                });
                count++;
            }
            totalLabel.setText("Total Records: " + count);
            if (count == 0)
                JOptionPane.showMessageDialog(this,
                    "No records found for ID: " + id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    private JButton createSmallButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));
        return btn;
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
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}