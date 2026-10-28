import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Experiment 5: A Notepad-like text editor built with Swing.
 *
 * Features: New / Open / Save / Save As / Exit, Cut / Copy / Paste /
 * Select All / Find, Word Wrap, Font size, status bar, unsaved-changes prompt.
 *
 * Compile: javac Notepad.java
 * Run    : java Notepad
 */
public class Notepad extends JFrame {

    private final JTextArea textArea = new JTextArea();
    private final JLabel status = new JLabel(" Ln 1, Col 1");
    private final JFileChooser chooser = new JFileChooser();
    private File currentFile = null;
    private boolean modified = false;
    private int fontSize = 16;

    public Notepad() {
        super("Untitled - Notepad");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        textArea.setMargin(new Insets(5, 5, 5, 5));
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        setJMenuBar(buildMenuBar());
        wireListeners();
    }

    // ---------- Menu ----------
    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        // File menu
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.add(item("New", KeyEvent.VK_N, e -> newFile()));
        file.add(item("Open...", KeyEvent.VK_O, e -> openFile()));
        file.add(item("Save", KeyEvent.VK_S, e -> saveFile(false)));
        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(e -> saveFile(true));
        file.add(saveAs);
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> exitApp());
        file.add(exit);

        // Edit menu (uses ready-made actions from DefaultEditorKit)
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');
        edit.add(editorAction(new DefaultEditorKit.CutAction(), "Cut", KeyEvent.VK_X));
        edit.add(editorAction(new DefaultEditorKit.CopyAction(), "Copy", KeyEvent.VK_C));
        edit.add(editorAction(new DefaultEditorKit.PasteAction(), "Paste", KeyEvent.VK_V));
        edit.addSeparator();
        edit.add(item("Select All", KeyEvent.VK_A, e -> textArea.selectAll()));
        edit.add(item("Find...", KeyEvent.VK_F, e -> find()));

        // Format menu
        JMenu format = new JMenu("Format");
        format.setMnemonic('o');
        JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Word Wrap");
        wrap.addActionListener(e -> {
            textArea.setLineWrap(wrap.isSelected());
            textArea.setWrapStyleWord(wrap.isSelected());
        });
        format.add(wrap);
        format.add(item("Increase Font Size", KeyEvent.VK_EQUALS, e -> changeFont(+2)));
        format.add(item("Decrease Font Size", KeyEvent.VK_MINUS, e -> changeFont(-2)));

        // Help menu
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Simple Notepad built with Java Swing", "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file); bar.add(edit); bar.add(format); bar.add(help);
        return bar;
    }

    private JMenuItem item(String text, int key, ActionListener al) {
        JMenuItem mi = new JMenuItem(text);
        mi.setAccelerator(KeyStroke.getKeyStroke(key,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        mi.addActionListener(al);
        return mi;
    }

    private JMenuItem editorAction(Action action, String text, int key) {
        JMenuItem mi = new JMenuItem(action);
        mi.setText(text);
        mi.setAccelerator(KeyStroke.getKeyStroke(key,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        return mi;
    }

    // ---------- Listeners ----------
    private void wireListeners() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { markModified(); }
            public void removeUpdate(DocumentEvent e)  { markModified(); }
            public void changedUpdate(DocumentEvent e) { markModified(); }
        });

        textArea.addCaretListener(e -> {
            try {
                int pos = textArea.getCaretPosition();
                int line = textArea.getLineOfOffset(pos);
                int col = pos - textArea.getLineStartOffset(line);
                status.setText(" Ln " + (line + 1) + ", Col " + (col + 1));
            } catch (Exception ignored) { }
        });

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { exitApp(); }
        });
    }

    private void markModified() {
        modified = true;
        updateTitle();
    }

    private void updateTitle() {
        String name = (currentFile == null) ? "Untitled" : currentFile.getName();
        setTitle((modified ? "*" : "") + name + " - Notepad");
    }

    // ---------- File operations ----------
    private boolean confirmDiscard() {
        if (!modified) return true;
        int choice = JOptionPane.showConfirmDialog(this,
                "Do you want to save changes?", "Notepad",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) return saveFile(false);
        return choice == JOptionPane.NO_OPTION;
    }

    private void newFile() {
        if (!confirmDiscard()) return;
        textArea.setText("");
        currentFile = null;
        modified = false;
        updateTitle();
    }

    private void openFile() {
        if (!confirmDiscard()) return;
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = chooser.getSelectedFile();
        try {
            String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            textArea.setText(content);
            textArea.setCaretPosition(0);
            currentFile = f;
            modified = false;
            updateTitle();
        } catch (IOException ex) {
            error("Could not open file: " + ex.getMessage());
        }
    }

    private boolean saveFile(boolean saveAs) {
        if (currentFile == null || saveAs) {
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return false;
            currentFile = chooser.getSelectedFile();
        }
        try {
            Files.write(currentFile.toPath(), textArea.getText().getBytes(StandardCharsets.UTF_8));
            modified = false;
            updateTitle();
            return true;
        } catch (IOException ex) {
            error("Could not save file: " + ex.getMessage());
            return false;
        }
    }

    private void exitApp() {
        if (confirmDiscard()) {
            dispose();
            System.exit(0);
        }
    }

    // ---------- Edit / Format ----------
    private void find() {
        String term = JOptionPane.showInputDialog(this, "Find what:", "Find",
                JOptionPane.QUESTION_MESSAGE);
        if (term == null || term.isEmpty()) return;
        String text = textArea.getText();
        int start = textArea.getSelectionEnd();
        int idx = text.indexOf(term, start);
        if (idx < 0) idx = text.indexOf(term);      // wrap to the beginning
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Cannot find \"" + term + "\"");
            return;
        }
        textArea.requestFocusInWindow();
        textArea.select(idx, idx + term.length());
    }

    private void changeFont(int delta) {
        fontSize = Math.max(8, Math.min(48, fontSize + delta));
        textArea.setFont(textArea.getFont().deriveFont((float) fontSize));
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad().setVisible(true));
    }
}
