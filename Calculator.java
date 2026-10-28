import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A simple calculator built using only AWT controls:
 * Frame, Panel, TextField, Button, layout managers and event listeners.
 *
 * Compile: javac Calculator.java
 * Run    : java Calculator
 */
public class Calculator extends Frame implements ActionListener {

    private final TextField display = new TextField("0");

    private double firstOperand = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    private static final String[] BUTTONS = {
        "C", "<-", "%", "/",
        "7", "8", "9", "*",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "+/-", "0", ".", "="
    };

    public Calculator() {
        super("AWT Calculator");
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(40, 40, 40));

        // ----- Display -----
        display.setEditable(false);
        display.setFont(new Font("SansSerif", Font.BOLD, 28));
        display.setBackground(Color.WHITE);
        display.setForeground(Color.BLACK);

        Panel top = new Panel(new BorderLayout());
        top.setBackground(new Color(40, 40, 40));
        top.add(display, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // ----- Buttons -----
        Panel keypad = new Panel(new GridLayout(5, 4, 5, 5));
        keypad.setBackground(new Color(40, 40, 40));

        for (String label : BUTTONS) {
            Button b = new Button(label);
            b.setFont(new Font("SansSerif", Font.BOLD, 20));
            b.addActionListener(this);

            if (label.equals("=")) {
                b.setBackground(new Color(255, 140, 0));
                b.setForeground(Color.WHITE);
            } else if ("/*-+".contains(label) && label.length() == 1) {
                b.setBackground(new Color(255, 190, 100));
            } else if (label.equals("C") || label.equals("<-")
                    || label.equals("%") || label.equals("+/-")) {
                b.setBackground(new Color(200, 200, 200));
            } else {
                b.setBackground(new Color(240, 240, 240));
            }
            keypad.add(b);
        }
        add(keypad, BorderLayout.CENTER);

        // ----- Frame settings -----
        setSize(320, 440);
        setLocationRelativeTo(null);   // centre on screen
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    // ----- Event handling -----
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.length() == 1 && Character.isDigit(cmd.charAt(0))) {
            appendDigit(cmd);
        } else {
            switch (cmd) {
                case ".":
                    appendDecimalPoint();
                    break;
                case "+": case "-": case "*": case "/":
                    handleOperator(cmd);
                    break;
                case "=":
                    handleEquals();
                    break;
                case "C":
                    clearAll();
                    break;
                case "<-":
                    backspace();
                    break;
                case "+/-":
                    negate();
                    break;
                case "%":
                    percent();
                    break;
            }
        }
    }

    private void appendDigit(String digit) {
        if (startNewNumber || display.getText().equals("0")) {
            display.setText(digit);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + digit);
        }
    }

    private void appendDecimalPoint() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void handleOperator(String op) {
        // Chain calculations: 2 + 3 + ... evaluates 2 + 3 first
        if (!operator.isEmpty() && !startNewNumber) {
            if (!compute()) return;
        }
        firstOperand = currentValue();
        operator = op;
        startNewNumber = true;
    }

    private void handleEquals() {
        if (operator.isEmpty()) return;
        compute();
        operator = "";
        startNewNumber = true;
    }

    /** Performs firstOperand <operator> display. Returns false on error. */
    private boolean compute() {
        double second = currentValue();
        double result;

        switch (operator) {
            case "+": result = firstOperand + second; break;
            case "-": result = firstOperand - second; break;
            case "*": result = firstOperand * second; break;
            case "/":
                if (second == 0) {
                    showError();
                    return false;
                }
                result = firstOperand / second;
                break;
            default:
                return true;
        }

        display.setText(format(result));
        firstOperand = result;
        return true;
    }

    private void clearAll() {
        display.setText("0");
        firstOperand = 0;
        operator = "";
        startNewNumber = true;
    }

    private void backspace() {
        if (startNewNumber) return;
        String text = display.getText();
        if (text.length() <= 1 || (text.length() == 2 && text.startsWith("-"))) {
            display.setText("0");
            startNewNumber = true;
        } else {
            display.setText(text.substring(0, text.length() - 1));
        }
    }

    private void negate() {
        String text = display.getText();
        if (text.equals("0") || text.equals("Error")) return;
        display.setText(text.startsWith("-") ? text.substring(1) : "-" + text);
    }

    private void percent() {
        display.setText(format(currentValue() / 100));
        startNewNumber = true;
    }

    // ----- Helpers -----
    private double currentValue() {
        try {
            return Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void showError() {
        display.setText("Error");
        firstOperand = 0;
        operator = "";
        startNewNumber = true;
    }

    /** Formats a double without trailing zeros or scientific notation. */
    private String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        BigDecimal bd = new BigDecimal(value)
                .setScale(10, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return bd.scale() < 0 ? bd.setScale(0).toPlainString() : bd.toPlainString();
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
