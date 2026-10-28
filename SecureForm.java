import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/*
 * Experiment 11: A secure input form (Name + Password) using an Applet container.
 *
 * Security features:
 *   - password characters are masked
 *   - name is validated (letters/spaces only)
 *   - strong-password rules (8+ chars, upper, lower, digit, special)
 *   - password must be confirmed
 *   - live strength indicator
 *   - only a SHA-256 hash is displayed, never the password itself
 *
 * Compile : javac SecureForm.java
 * Run     : java SecureForm              (main() hosts the applet in a Frame)
 *   or    : appletviewer SecureForm.java (older JDKs)
 *
 * <applet code="SecureForm" width="440" height="320"></applet>
 */
@SuppressWarnings("removal")
public class SecureForm extends Applet implements ActionListener, TextListener {

    private final TextField nameField = new TextField(22);
    private final TextField passField = new TextField(22);
    private final TextField confirmField = new TextField(22);
    private final Label strengthLabel = new Label("Strength: -");
    private final Button submitButton = new Button("Submit");
    private final Button clearButton = new Button("Clear");
    private final Label message = new Label("", Label.CENTER);

    @Override
    public void init() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 240, 235));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        Label title = new Label("Secure Registration", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        add(title, c);

        passField.setEchoChar('*');
        confirmField.setEchoChar('*');

        c.gridwidth = 1; c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 1; add(new Label("Name:"), c);
        c.gridx = 1;              add(nameField, c);
        c.gridx = 0; c.gridy = 2; add(new Label("Password:"), c);
        c.gridx = 1;              add(passField, c);
        c.gridx = 1; c.gridy = 3; add(strengthLabel, c);
        c.gridx = 0; c.gridy = 4; add(new Label("Confirm:"), c);
        c.gridx = 1;              add(confirmField, c);

        Panel buttons = new Panel(new FlowLayout());
        buttons.add(submitButton);
        buttons.add(clearButton);
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        add(buttons, c);

        c.gridy = 6; c.fill = GridBagConstraints.HORIZONTAL;
        add(message, c);

        submitButton.addActionListener(this);
        clearButton.addActionListener(this);
        passField.addTextListener(this);      // update strength while typing
    }

    @Override
    public void textValueChanged(TextEvent e) {
        int score = score(passField.getText());
        String[] names = {"Very weak", "Weak", "Fair", "Good", "Strong"};
        Color[] colors = {Color.RED, Color.RED, Color.ORANGE, new Color(150, 150, 0), new Color(0, 130, 0)};
        if (passField.getText().isEmpty()) {
            strengthLabel.setText("Strength: -");
            strengthLabel.setForeground(Color.BLACK);
        } else {
            strengthLabel.setText("Strength: " + names[score]);
            strengthLabel.setForeground(colors[score]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            nameField.setText("");
            passField.setText("");
            confirmField.setText("");
            show("", Color.BLACK);
            return;
        }

        String name = nameField.getText().trim();
        String pass = passField.getText();
        String confirm = confirmField.getText();

        if (!name.matches("[A-Za-z ]{2,40}")) {
            show("Name must be 2-40 letters (spaces allowed).", Color.RED);
        } else if (pass.length() < 8) {
            show("Password must be at least 8 characters.", Color.RED);
        } else if (!pass.matches(".*[A-Z].*") || !pass.matches(".*[a-z].*")) {
            show("Password needs upper- and lower-case letters.", Color.RED);
        } else if (!pass.matches(".*\\d.*")) {
            show("Password needs at least one digit.", Color.RED);
        } else if (!pass.matches(".*[^A-Za-z0-9].*")) {
            show("Password needs at least one special character.", Color.RED);
        } else if (!pass.equals(confirm)) {
            show("Passwords do not match.", Color.RED);
        } else {
            String h = sha256(pass);
            show("Accepted! Hash: " + h.substring(0, 16) + "...", new Color(0, 120, 0));
            System.out.println("User: " + name + "  SHA-256: " + h);
        }
    }

    /** 0 (very weak) .. 4 (strong) */
    private int score(String p) {
        int s = 0;
        if (p.length() >= 8) s++;
        if (p.matches(".*[A-Z].*") && p.matches(".*[a-z].*")) s++;
        if (p.matches(".*\\d.*")) s++;
        if (p.matches(".*[^A-Za-z0-9].*")) s++;
        return s;
    }

    private void show(String text, Color color) {
        message.setForeground(color);
        message.setText(text);
    }

    private static String sha256(String text) {
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
        Frame frame = new Frame("Secure Applet Form");
        SecureForm applet = new SecureForm();
        frame.add(applet);
        frame.setSize(460, 340);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { frame.dispose(); System.exit(0); }
        });
        applet.init();
        applet.start();
        frame.setVisible(true);
    }
}
