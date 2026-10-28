import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

/*
 * Experiment 12: Reuses the Name / Password form to perform basic database operations
 * (Create, Read, Update, Delete) using JDBC.
 *
 * Setup  : mysql -u root -p < schema.sql   and put MySQL Connector/J on the classpath.
 * Compile: javac CrudForm.java
 * Run    : java -cp .:mysql-connector-j-x.x.x.jar CrudForm       (Windows: use ; instead of :)
 *
 * <applet code="CrudForm" width="520" height="440"></applet>
 *
 * Name is the key:
 *   Create  - inserts Name + Password
 *   Read    - finds the record for Name
 *   Update  - sets a new Password for Name
 *   Delete  - removes the record for Name
 *   View All- lists every record
 */
@SuppressWarnings("removal")
public class CrudForm extends Applet implements ActionListener {

    // ---- Database configuration ----
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/labdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private final TextField nameField = new TextField(20);
    private final TextField passwordField = new TextField(20);
    private final Button createBtn = new Button("Create");
    private final Button readBtn = new Button("Read");
    private final Button updateBtn = new Button("Update");
    private final Button deleteBtn = new Button("Delete");
    private final Button viewAllBtn = new Button("View All");
    private final Button clearBtn = new Button("Clear");
    private final TextArea output = new TextArea(10, 50);
    private final Label status = new Label("Ready");

    @Override
    public void init() {
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(235, 245, 245));

        // --- the form (same fields as the earlier experiments) ---
        Panel form = new Panel(new GridLayout(2, 2, 6, 6));
        form.add(new Label("Name:"));
        form.add(nameField);
        passwordField.setEchoChar('*');
        form.add(new Label("Password:"));
        form.add(passwordField);

        Panel buttons = new Panel(new FlowLayout());
        for (Button b : new Button[]{createBtn, readBtn, updateBtn, deleteBtn, viewAllBtn, clearBtn}) {
            b.addActionListener(this);
            buttons.add(b);
        }

        Panel north = new Panel(new BorderLayout(5, 5));
        Label title = new Label("User Records (CRUD with JDBC)", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        north.add(title, BorderLayout.NORTH);
        north.add(form, BorderLayout.CENTER);
        north.add(buttons, BorderLayout.SOUTH);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        add(north, BorderLayout.NORTH);
        add(output, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == clearBtn) {
            nameField.setText("");
            passwordField.setText("");
            output.setText("");
            setStatus("Ready", Color.BLACK);
        } else if (src == viewAllBtn) {
            viewAll();
        } else {
            String name = nameField.getText().trim();
            String pass = passwordField.getText();
            if (name.isEmpty()) {
                setStatus("Name is required.", Color.RED);
                return;
            }
            if ((src == createBtn || src == updateBtn) && pass.isEmpty()) {
                setStatus("Password is required.", Color.RED);
                return;
            }
            if (src == createBtn) create(name, pass);
            else if (src == readBtn) read(name);
            else if (src == updateBtn) update(name, pass);
            else if (src == deleteBtn) delete(name);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // ---------- CRUD operations ----------
    private void create(String name, String pass) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users (name, password) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, hash(pass));
            ps.executeUpdate();
            setStatus("Created user \"" + name + "\".", new Color(0, 120, 0));
        } catch (SQLIntegrityConstraintViolationException dup) {
            setStatus("User \"" + name + "\" already exists.", Color.RED);
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void read(String name) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT name, password FROM users WHERE name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    output.setText("Name     : " + rs.getString("name")
                            + "\nPassword : " + rs.getString("password") + "  (SHA-256 hash)");
                    setStatus("Record found.", new Color(0, 120, 0));
                } else {
                    output.setText("");
                    setStatus("No user named \"" + name + "\".", Color.RED);
                }
            }
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void update(String name, String newPass) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE users SET password = ? WHERE name = ?")) {
            ps.setString(1, hash(newPass));
            ps.setString(2, name);
            int rows = ps.executeUpdate();
            if (rows > 0) setStatus("Updated password for \"" + name + "\".", new Color(0, 120, 0));
            else setStatus("No user named \"" + name + "\".", Color.RED);
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void delete(String name) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE name = ?")) {
            ps.setString(1, name);
            int rows = ps.executeUpdate();
            if (rows > 0) setStatus("Deleted user \"" + name + "\".", new Color(0, 120, 0));
            else setStatus("No user named \"" + name + "\".", Color.RED);
        } catch (SQLException ex) {
            error(ex);
        }
    }

    private void viewAll() {
        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT name, password FROM users ORDER BY name")) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-20s %s%n", "NAME", "PASSWORD (SHA-256)"));
            sb.append("-".repeat(70)).append('\n');
            int count = 0;
            while (rs.next()) {
                String h = rs.getString("password");
                sb.append(String.format("%-20s %s...%n", rs.getString("name"),
                        h.substring(0, Math.min(24, h.length()))));
                count++;
            }
            output.setText(sb.toString());
            setStatus(count + " record(s).", Color.BLACK);
        } catch (SQLException ex) {
            error(ex);
        }
    }

    // ---------- helpers ----------
    private void setStatus(String text, Color color) {
        status.setForeground(color);
        status.setText(text);
    }

    private void error(SQLException ex) {
        setStatus("Database error: " + ex.getMessage(), Color.RED);
    }

    private static String hash(String text) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame("CRUD with JDBC");
        CrudForm applet = new CrudForm();
        frame.add(applet);
        frame.setSize(560, 460);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { frame.dispose(); System.exit(0); }
        });
        applet.init();
        applet.start();
        frame.setVisible(true);
    }
}
