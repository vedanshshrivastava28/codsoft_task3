import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ATMGUI extends JFrame implements ActionListener {

    private BankAccount userAccount;
    private JTextField amountField;
    private JTextArea displayArea;

    public ATMGUI() {
        userAccount = new BankAccount(1000); // Example initial balance

        setTitle("ATM Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel amountLabel = new JLabel("Enter Amount:");
        amountField = new JTextField(10);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        contentPane.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton withdrawButton = createButton("Withdraw");
        JButton depositButton = createButton("Deposit");
        JButton balanceButton = createButton("Check Balance");
        buttonPanel.add(withdrawButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(balanceButton);
        contentPane.add(buttonPanel, BorderLayout.CENTER);

        displayArea = new JTextArea(8, 20);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATMGUI atmGUI = new ATMGUI();
            atmGUI.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Withdraw")) {
            performWithdraw();
        } else if (e.getActionCommand().equals("Deposit")) {
            performDeposit();
        } else if (e.getActionCommand().equals("Check Balance")) {
            displayBalance();
        }
    }

    private void performWithdraw() {
        String amountStr = amountField.getText();
        double amount = parseAmount(amountStr);

        if (amount <= 0) {
            showMessageDialog("Invalid amount format.");
            return;
        }

        if (amount > userAccount.getBalance()) {
            showMessageDialog("Insufficient funds.");
        } else {
            userAccount.withdraw(amount);
            showMessageDialog("Withdrawal successful. Remaining balance: $" + userAccount.getBalance());
        }
    }

    private void performDeposit() {
        String amountStr = amountField.getText();
        double amount = parseAmount(amountStr);

        if (amount <= 0) {
            showMessageDialog("Invalid amount format.");
            return;
        }

        userAccount.deposit(amount);
        showMessageDialog("Deposit successful. New balance: $" + userAccount.getBalance());
    }

    private void displayBalance() {
        double balance = userAccount.getBalance();
        displayArea.setText("Current Balance: $" + balance);
        amountField.setText(""); // Clear amount field after displaying balance
    }

    private double parseAmount(String amountStr) {
        double amount = 0;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            amount = -1; // Invalid amount indicator
        }
        return amount;
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    // BankAccount class
    private static class BankAccount {
        private double balance;

        public BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public void withdraw(double amount) {
            balance -= amount;
        }

        public double getBalance() {
            return balance;
        }
    }
}
