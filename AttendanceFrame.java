import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class AttendanceFrame extends JFrame {

    private JTextField idField;
    private JComboBox<String> subjectBox, statusBox;
    private JTable table;
    private DefaultTableModel tableModel;

    public AttendanceFrame() {
        setTitle("Mark Attendance");
        setSize(820, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        JLabel titleLabel = new JLabel("MARK ATTENDANCE",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(new Color(44, 62, 80));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Left form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(52, 73, 94));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        formPanel.setPreferredSize(new Dimension(260, 0));
        contentPanel.add(formPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel formTitle = new JLabel("ATTENDANCE ENTRY",
            SwingConstants.CENTER);
        formTitle.setForeground(new Color(52, 152, 219));
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(formTitle, gbc);

        // Student ID
        JLabel idLabel = new JLabel("Student ID");
        idLabel.setForeground(new Color(189, 195, 199));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 3, 0);
        formPanel.add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idField.setBackground(new Color(44, 62, 80));
        idField.setForeground(Color.WHITE);
        idField.setCaretColor(Color.WHITE);
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(idField, gbc);

        // Subject
        JLabel subLabel = new JLabel("Subject");
        subLabel.setForeground(new Color(189, 195, 199));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 3, 0);
        formPanel.add(subLabel, gbc);

        String[] subjects = {"Java", "Data Mining", "MERN", "DBMS", "Python"};
        subjectBox = new JComboBox<>(subjects);
        subjectBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectBox.setBackground(new Color(44, 62, 80));
        subjectBox.setForeground(Color.WHITE);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(subjectBox, gbc);

        // Status
        JLabel statusLbl = new JLabel("Status");
        statusLbl.setForeground(new Color(189, 195, 199));
        statusLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 3, 0);
        formPanel.add(statusLbl, gbc);

        String[] statuses = {"Present", "Absent"};
        statusBox = new JComboBox<>(statuses);
        statusBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusBox.setBackground(new Color(44, 62, 80));
        statusBox.setForeground(Color.WHITE);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(statusBox, gbc);

        // Date
        JLabel dateLabel = new JLabel("Date: " + LocalDate.now().toString());
        dateLabel.setForeground(new Color(52, 152, 219));
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        formPanel.add(dateLabel, gbc);

        // Right table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(44, 62, 80));
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        String[] columns = {"ID", "Student ID", "Subject", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        // Color rows
        table.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,
                Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                String status = t.getValueAt(row, 4).toString();
                setBackground(status.equals("Present") ?
                    new Color(39, 174, 96) : new Color(169, 50, 38));
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
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton markBtn = createButton("Mark Attendance",
            new Color(39, 174, 96));
        JButton updateBtn = createButton("Update Status",
            new Color(211, 84, 0));
        JButton deleteBtn = createButton("Delete",
            new Color(169, 50, 38));
        JButton backBtn = createButton("Back",
            new Color(41, 128, 185));

        btnPanel.add(markBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        markBtn.addActionListener(e -> markAttendance());
        updateBtn.addActionListener(e -> updateAttendance());
        deleteBtn.addActionListener(e -> deleteAttendance());
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame("admin", "admin");
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                idField.setText(
                    tableModel.getValueAt(row, 1).toString());
                subjectBox.setSelectedItem(
                    tableModel.getValueAt(row, 2).toString());
                statusBox.setSelectedItem(
                    tableModel.getValueAt(row, 4).toString());
            }
        });

        loadAttendance();
        setVisible(true);
    }

    private void markAttendance() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID!");
            return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement check = conn.prepareStatement(
                "SELECT * FROM students WHERE student_id=?");
            check.setInt(1, Integer.parseInt(id));
            if (!check.executeQuery().next()) {
                JOptionPane.showMessageDialog(this,
                    "Student ID not found!");
                return;
            }
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO attendance (student_id, subject, " +
                "attendance_date, status) VALUES (?,?,?,?)");
            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, subjectBox.getSelectedItem().toString());
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(4, statusBox.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance marked!");
            loadAttendance();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateAttendance() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }
        int id = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString());
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE attendance SET status=? WHERE id=?");
            ps.setString(1, statusBox.getSelectedItem().toString());
            ps.setInt(2, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated!");
            loadAttendance();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteAttendance() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }
        int id = Integer.parseInt(
            tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete this record?", "Confirm",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM attendance WHERE id=?");
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted!");
                loadAttendance();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
            }
        }
    }

    private void loadAttendance() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM attendance " +
                "ORDER BY attendance_date DESC");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("subject"),
                    rs.getDate("attendance_date"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
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