import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Experiment 9: A small portal containing a student registration form (Swing + JDBC).
 *
 * Setup  : mysql -u root -p < schema.sql   and put MySQL Connector/J on the classpath.
 * Compile: javac StudentPortal.java
 * Run    : java -cp .:mysql-connector-j-x.x.x.jar StudentPortal    (Windows: use ; instead of :)
 */
public class StudentPortal extends JFrame {

    // ---- Database configuration ----
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/labdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private final JTextField rollField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JTextField emailField = new JTextField(15);
    private final JTextField phoneField = new JTextField(15);
    private final JComboBox<String> courseBox = new JComboBox<>(
            new String[]{"B.Tech CSE", "B.Tech IT", "B.Tech ECE", "BCA", "MCA", "M.Tech"});
    private final JRadioButton male = new JRadioButton("Male", true);
    private final JRadioButton female = new JRadioButton("Female");
    private final JRadioButton other = new JRadioButton("Other");
    private final JTextArea addressArea = new JTextArea(3, 15);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Roll No", "Name", "Email", "Phone", "Course", "Gender", "Address"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public StudentPortal() {
        super("Student Registration Portal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildForm(), BorderLayout.WEST);
        add(buildTablePanel(), BorderLayout.CENTER);

        pack();
        setSize(Math.max(getWidth(), 900), Math.max(getHeight(), 480));
        setLocationRelativeTo(null);

        loadStudents();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Registration Form"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        ButtonGroup group = new ButtonGroup();
        group.add(male); group.add(female); group.add(other);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(male); genderPanel.add(female); genderPanel.add(other);

        addressArea.setLineWrap(true);

        int row = 0;
        addRow(form, c, row++, "Roll No:", rollField);
        addRow(form, c, row++, "Name:", nameField);
        addRow(form, c, row++, "Email:", emailField);
        addRow(form, c, row++, "Phone:", phoneField);
        addRow(form, c, row++, "Course:", courseBox);
        addRow(form, c, row++, "Gender:", genderPanel);
        addRow(form, c, row++, "Address:", new JScrollPane(addressArea));

        JButton register = new JButton("Register");
        JButton clear = new JButton("Clear");
        register.addActionListener(e -> register());
        clear.addActionListener(e -> clearForm());
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(register);
        buttons.add(clear);
        c.gridx = 0; c.gridy = row; c.gridwidth = 2;
        form.add(buttons, c);
        return form;
    }

    private void addRow(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        p.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        p.add(field, c);
    }

    private JPanel buildTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Registered Students"));
        p.add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        return p;
    }

    // ---------- Actions ----------
    private void register() {
        String roll = rollField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (roll.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            warn("Roll No, Name, Email and Phone are required.");
            return;
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            warn("Please enter a valid email address.");
            return;
        }
        if (!phone.matches("\\d{10}")) {
            warn("Phone number must be exactly 10 digits.");
            return;
        }

        String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "Other";
        String sql = "INSERT INTO students (roll_no, name, email, phone, course, gender, address) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, roll);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, (String) courseBox.getSelectedItem());
            ps.setString(6, gender);
            ps.setString(7, addressArea.getText().trim());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student registered successfully!");
            clearForm();
            loadStudents();

        } catch (SQLIntegrityConstraintViolationException dup) {
            warn("Roll number " + roll + " is already registered.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students ORDER BY roll_no")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("roll_no"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone"), rs.getString("course"), rs.getString("gender"),
                        rs.getString("address")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load students: " + ex.getMessage()
                            + "\nCheck that MySQL is running and the JDBC driver is on the classpath.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        rollField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseBox.setSelectedIndex(0);
        male.setSelected(true);
        addressArea.setText("");
        rollField.requestFocus();
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentPortal().setVisible(true));
    }
}
