import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ReportFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalStudentsLabel, presentLabel,
                   absentLabel, avgLabel;

    public ReportFrame() {
        setTitle("Attendance Report");
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
        JLabel titleLabel = new JLabel("ATTENDANCE REPORT",
            SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Summary cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        cardsPanel.setBackground(new Color(44, 62, 80));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JPanel card1 = createCard("Total Students", "0",
            new Color(52, 73, 94));
        JPanel card2 = createCard("Present", "0",
            new Color(39, 174, 96));
        JPanel card3 = createCard("Absent", "0",
            new Color(169, 50, 38));
        JPanel card4 = createCard("Average %", "0%",
            new Color(41, 128, 185));

        totalStudentsLabel = (JLabel) card1.getComponent(1);
        presentLabel       = (JLabel) card2.getComponent(1);
        absentLabel        = (JLabel) card3.getComponent(1);
        avgLabel           = (JLabel) card4.getComponent(1);

        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        cardsPanel.add(card4);

        // Combine header and cards
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        legendPanel.setBackground(new Color(52, 73, 94));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel legend = new JLabel(
            "🟢  Green = Attendance ≥ 75% (Safe)        " +
            "🔴  Red = Attendance < 75% (Shortage!)");
        legend.setForeground(new Color(189, 195, 199));
        legend.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        legendPanel.add(legend);

        // Table
        String[] columns = {"Student ID", "Student Name",
            "Total", "Present", "Absent", "Attendance %"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        // Color rows by percentage
        table.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,
                Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                try {
                    String p = t.getValueAt(row, 5)
                        .toString().replace("%", "");
                    double perc = Double.parseDouble(p);
                    setBackground(perc < 75 ?
                        new Color(169, 50, 38) : new Color(39, 174, 96));
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

        JPanel tablePanel = new JPanel(new BorderLayout(0, 0));
        tablePanel.setBackground(new Color(44, 62, 80));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        tablePanel.add(legendPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Back button
        JPanel btnPanel = new JPanel(new GridLayout(1, 1));
        btnPanel.setBackground(new Color(44, 62, 80));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
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
            new DashboardFrame("admin", "admin");
        });
        btnPanel.add(backBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        loadReport();
        setVisible(true);
    }

    private void loadReport() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();

            ResultSet rs1 = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM students");
            if (rs1.next())
                totalStudentsLabel.setText(
                    String.valueOf(rs1.getInt(1)));

            ResultSet rs2 = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM attendance " +
                "WHERE status='Present'");
            if (rs2.next())
                presentLabel.setText(
                    String.valueOf(rs2.getInt(1)));

            ResultSet rs3 = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM attendance " +
                "WHERE status='Absent'");
            if (rs3.next())
                absentLabel.setText(
                    String.valueOf(rs3.getInt(1)));

            ResultSet rs4 = conn.createStatement().executeQuery(
                "SELECT ROUND(COUNT(CASE WHEN status='Present' " +
                "THEN 1 END) * 100.0 / COUNT(*), 1) " +
                "as avg_att FROM attendance");
            if (rs4.next())
                avgLabel.setText(rs4.getDouble("avg_att") + "%");

            String query =
                "SELECT s.student_id, s.student_name, " +
                "COUNT(a.id) as total, " +
                "SUM(CASE WHEN a.status='Present' " +
                "THEN 1 ELSE 0 END) as present, " +
                "SUM(CASE WHEN a.status='Absent' " +
                "THEN 1 ELSE 0 END) as absent, " +
                "ROUND(SUM(CASE WHEN a.status='Present' " +
                "THEN 1 ELSE 0 END) * 100.0 / " +
                "COUNT(a.id), 2) as percentage " +
                "FROM students s " +
                "LEFT JOIN attendance a " +
                "ON s.student_id = a.student_id " +
                "GROUP BY s.student_id, s.student_name " +
                "ORDER BY percentage ASC";

            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getInt("total"),
                    rs.getInt("present"),
                    rs.getInt("absent"),
                    rs.getDouble("percentage") + "%"
                });
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
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setForeground(new Color(214, 234, 248));
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(titleLbl);

        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
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