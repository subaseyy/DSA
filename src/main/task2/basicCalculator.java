package main.task2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// Question 2, a

public class basicCalculator extends JFrame {
    private JTextField displayField;
    private JPanel buttonPanel;

    public basicCalculator() {
        setTitle("Basic Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setLocationRelativeTo(null); // Center the frame

        // Initialize components
        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setEditable(false);
        displayField.setBackground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for the buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10)); // 5 rows, 4 columns

        // Button labels
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "(", ")", "C", "Del"
        };

        // Add buttons to the panel
        for (String text : buttons) {
            JButton button = createButton(text);
            buttonPanel.add(button);
        }

        // Layout setup
        setLayout(new BorderLayout(10, 10));
        add(displayField, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Handle window close event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // Method to create styled buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(new Color(230, 230, 230));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(text);
            }
        });

        return button;
    }

    // Handle button clicks
    private void handleButtonClick(String text) {
        String currentText = displayField.getText();

        if (text.equals("C")) {
            displayField.setText("");
        } else if (text.equals("Del")) {
            if (!currentText.isEmpty()) {
                displayField.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (text.equals("=")) {
            try {
                double result = evaluate(currentText);
                displayField.setText(String.valueOf(result));
            } catch (Exception ex) {
                displayField.setText("Error");
            }
        } else {
            displayField.setText(currentText + text);
        }
    }

    // Method to evaluate the mathematical expression
    private double evaluate(String expression) throws Exception {
        Parser parser = new Parser(expression);
        return parser.parseExpression();
    }

    // Inner class for parsing and evaluating expressions
    private class Parser {
        private String input;
        private int pos;
        private int ch;

        public Parser(String input) {
            this.input = input;
            pos = -1;
            nextChar();
        }

        private void nextChar() {
            pos++;
            if (pos < input.length()) {
                ch = input.charAt(pos);
            } else {
                ch = -1;
            }
        }

        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        public double parseExpression() throws Exception {
            double x = parseTerm();
            while (true) {
                if      (eat('+')) x += parseTerm(); // addition
                else if (eat('-')) x -= parseTerm(); // subtraction
                else return x;
            }
        }

        private double parseTerm() throws Exception {
            double x = parseFactor();
            while (true) {
                if      (eat('*')) x *= parseFactor(); // multiplication
                else if (eat('/')) {
                    double denominator = parseFactor();
                    if (denominator == 0) throw new ArithmeticException("Division by zero");
                    x /= denominator; // division
                }
                else return x;
            }
        }

        private double parseFactor() throws Exception {
            if (eat('+')) return parseFactor(); // unary plus
            if (eat('-')) return -parseFactor(); // unary minus

            double x;
            int startPos = this.pos;

            if (eat('(')) { // parentheses
                x = parseExpression();
                if (!eat(')')) throw new Exception("Missing closing parenthesis");
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                String numberStr = input.substring(startPos, this.pos);
                try {
                    x = Double.parseDouble(numberStr);
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid number format");
                }
            } else {
                throw new Exception("Unexpected character: " + (char)ch);
            }

            return x;
        }
    }

    // Entry point of the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                basicCalculator calculator = new basicCalculator();
                calculator.setVisible(true);
            }
        });
    }
}
