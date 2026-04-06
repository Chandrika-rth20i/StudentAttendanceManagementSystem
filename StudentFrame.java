import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentFrame extends JFrame {

    private JTextField idField, nameField, classField,
                       sectionField, emailField, phoneField,
                       usernameField, passwordField;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentFrame() {
        setTitle("Manage Students");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("MANAGE STUDENTS",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(new Color(44, 62, 80));
        contentPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 10, 15));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Left form panel with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(52, 73, 94));
        formPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel formTitle = new JLabel("ADD NEW STUDENT",
            SwingConstants.CENTER);
        formTitle.setForeground(new Color(52, 152, 219));
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        formPanel.add(formTitle, gbc);

        idField       = addField(formPanel, gbc, "Student ID", 1);
        nameField     = addField(formPanel, gbc, "Full Name", 3);
        classField    = addField(formPanel, gbc, "Class", 5);
        sectionField  = addField(formPanel, gbc, "Section", 7);
        emailField    = addField(formPanel, gbc, "Email", 9);
        phoneField    = addField(formPanel, gbc, "Phone", 11);
        usernameField = addField(formPanel, gbc, "Username", 13);
        passwordField = addField(formPanel, gbc, "Password", 15);

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setPreferredSize(new Dimension(260, 0));
        formScroll.setBorder(BorderFactory.createLineBorder(
            new Color(52, 152, 219), 1));
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(formScroll, BorderLayout.WEST);

        // Right table panel
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(44, 62, 80));
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        String[] columns = {"ID", "Name", "Class",
                            "Section", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(
            new Color(52, 152, 219), 1));
        scrollPane.getViewport().setBackground(
            new Color(52, 73, 94));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton addBtn = createButton("Add Student",
            new Color(39, 174, 96));
        JButton deleteBtn = createButton("Delete",
            new Color(169, 50, 38));
        JButton clearBtn = createButton("Clear",
            new Color(52, 73, 94));
        JButton backBtn = createButton("Back",
            new Color(41, 128, 185));

        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(backBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener(e -> addStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> {
            dispose();
            new DashboardFrame("admin", "admin");
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                idField.setText(
                    tableModel.getValueAt(row, 0).toString());
                nameField.setText(
                    tableModel.getValueAt(row, 1).toString());
                classField.setText(
                    tableModel.getValueAt(row, 2).toString());
                sectionField.setText(
                    tableModel.getValueAt(row, 3).toString());
                emailField.setText(
                    tableModel.getValueAt(row, 4).toString());
                phoneField.setText(
                    tableModel.getValueAt(row, 5).toString());
            }
        });

        loadStudents();
        setVisible(true);
    }

    private void addStudent() {
        String id       = idField.getText().trim();
        String name     = nameField.getText().trim();
        String cls      = classField.getText().trim();
        String sec      = sectionField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || cls.isEmpty() ||
            sec.isEmpty() || username.isEmpty() ||
            password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all required fields!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            // Add to students table
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students (student_id, student_name," +
                " class, section, email, phone) " +
                "VALUES (?,?,?,?,?,?)");
            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, name);
            ps.setString(3, cls);
            ps.setString(4, sec);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.executeUpdate();

            // Add to users table
            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO users (username, password, " +
                "role, student_id) VALUES (?,?,'student',?)");
            ps2.setString(1, username);
            ps2.setString(2, password);
            ps2.setInt(3, Integer.parseInt(id));
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student added!\n" +
                "Login: " + username + " / " + password);
            clearFields();
            loadStudents();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Select a student first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete student " + id + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DBConnection.getConnection();
                conn.prepareStatement(
                    "DELETE FROM users WHERE student_id=" + id)
                    .executeUpdate();
                conn.prepareStatement(
                    "DELETE FROM attendance WHERE student_id=" + id)
                    .executeUpdate();
                conn.prepareStatement(
                    "DELETE FROM students WHERE student_id=" + id)
                    .executeUpdate();
                JOptionPane.showMessageDialog(this,
                    "Student deleted!");
                clearFields();
                loadStudents();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
            }
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT student_id, student_name, class, " +
                "section, email, phone FROM students");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("class"),
                    rs.getString("section"),
                    rs.getString("email") != null ?
                        rs.getString("email") : "",
                    rs.getString("phone") != null ?
                        rs.getString("phone") : ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        classField.setText("");
        sectionField.setText("");
        emailField.setText("");
        phoneField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    private JTextField addField(JPanel panel,
        GridBagConstraints gbc, String labelText, int gridy) {

        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(189, 195, 199));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = gridy;
        gbc.insets = new Insets(5, 0, 2, 0);
        panel.add(label, gbc);

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(44, 62, 80));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setPreferredSize(new Dimension(220, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(52, 152, 219), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        panel.add(field, gbc);
        return field;
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
        t.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));
    }
}