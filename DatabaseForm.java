import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

/*
 * Experiment 8: The Name / Password entered in the applet form are stored in a database
 * using JDBC.
 *
 * Setup  : 1) mysql -u root -p < schema.sql
 *          2) Download MySQL Connector/J (mysql-connector-j-x.x.x.jar)
 *          3) Edit DB_USER / DB_PASS below
 * Compile: javac DatabaseForm.java
 * Run    : java -cp .:mysql-connector-j-x.x.x.jar DatabaseForm      (Linux/macOS)
 *          java -cp .;mysql-connector-j-x.x.x.jar DatabaseForm      (Windows)
 *
 * <applet code="DatabaseForm" width="400" height="260"></applet>
 */
@SuppressWarnings("removal")
public class DatabaseForm extends Applet implements ActionListener {

    // ---- Database configuration ----
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/labdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    private final TextField nameField = new TextField(20);
    private final TextField passwordField = new TextField(20);
    private final Button saveButton = new Button("Save to Database");
    private final Button clearButton = new Button("Clear");
    private final Label message = new Label("", Label.CENTER);

    @Override
    public void init() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 245, 235));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        Label title = new Label("Register User (JDBC)", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(title, c);

        c.gridwidth = 1; c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 1; add(new Label("Name:"), c);
        c.gridx = 1;              add(nameField, c);

        passwordField.setEchoChar('*');
        c.gridx = 0; c.gridy = 2; add(new Label("Password:"), c);
        c.gridx = 1;              add(passwordField, c);

        Panel buttons = new Panel(new FlowLayout());
        buttons.add(saveButton);
        buttons.add(clearButton);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        add(buttons, c);

        c.gridy = 4; c.fill = GridBagConstraints.HORIZONTAL;
        add(message, c);

        saveButton.addActionListener(this);
        clearButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            nameField.setText("");
            passwordField.setText("");
            show("", Color.BLACK);
            return;
        }

        String name = nameField.getText().trim();
        String pass = passwordField.getText();
        if (name.isEmpty() || pass.isEmpty()) {
            show("Please fill in both fields.", Color.RED);
            return;
        }

        String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, hash(pass));     // never store plain-text passwords
            ps.executeUpdate();
            show("Saved \"" + name + "\" to the database.", new Color(0, 120, 0));
            nameField.setText("");
            passwordField.setText("");

        } catch (SQLIntegrityConstraintViolationException dup) {
            show("User \"" + name + "\" already exists.", Color.RED);
        } catch (SQLException ex) {
            show("Database error: " + ex.getMessage(), Color.RED);
        }
    }

    private void show(String text, Color color) {
        message.setForeground(color);
        message.setText(text);
    }

    // SHA-256 hash (fine for a lab; use bcrypt/Argon2 with a salt in real systems).
    static String hash(String text) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame("Applet Form + JDBC");
        DatabaseForm applet = new DatabaseForm();
        frame.add(applet);
        frame.setSize(420, 280);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { frame.dispose(); System.exit(0); }
        });
        applet.init();
        applet.start();
        frame.setVisible(true);
    }
}
