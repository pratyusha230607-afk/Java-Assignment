import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

/*
 * Experiment 7: A form with Name and Password fields, using an Applet as the container.
 *
 * Compile : javac LoginForm.java
 * Run     : java LoginForm               (main() hosts the applet in a Frame)
 *   or    : appletviewer LoginForm.java  (older JDKs)
 *
 * <applet code="LoginForm" width="380" height="240"></applet>
 */
@SuppressWarnings("removal")
public class LoginForm extends Applet implements ActionListener {

    private final TextField nameField = new TextField(20);
    private final TextField passwordField = new TextField(20);
    private final Button submitButton = new Button("Submit");
    private final Button clearButton = new Button("Clear");
    private final Label message = new Label("", Label.CENTER);

    @Override
    public void init() {
        setLayout(new GridBagLayout());
        setBackground(new Color(235, 240, 250));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        Label title = new Label("User Login Form", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        add(title, c);

        c.gridwidth = 1; c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 1; add(new Label("Name:"), c);
        c.gridx = 1;              add(nameField, c);

        passwordField.setEchoChar('*');          // hide the typed characters
        c.gridx = 0; c.gridy = 2; add(new Label("Password:"), c);
        c.gridx = 1;              add(passwordField, c);

        Panel buttons = new Panel(new FlowLayout());
        buttons.add(submitButton);
        buttons.add(clearButton);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        add(buttons, c);

        c.gridy = 4; c.fill = GridBagConstraints.HORIZONTAL;
        add(message, c);

        submitButton.addActionListener(this);
        clearButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = nameField.getText().trim();
            String pass = passwordField.getText();
            if (name.isEmpty() || pass.isEmpty()) {
                message.setForeground(Color.RED);
                message.setText("Please fill in both fields.");
            } else {
                message.setForeground(new Color(0, 120, 0));
                message.setText("Welcome, " + name + "! (password length: " + pass.length() + ")");
            }
        } else {
            nameField.setText("");
            passwordField.setText("");
            message.setText("");
            nameField.requestFocus();
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame("Applet Form");
        LoginForm applet = new LoginForm();
        frame.add(applet);
        frame.setSize(380, 260);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { frame.dispose(); System.exit(0); }
        });
        applet.init();
        applet.start();
        frame.setVisible(true);
    }
}
