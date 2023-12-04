package carrental.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import carrental.models.Customer;
import carrental.util.CustomerAuthentication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField; // Change to JPasswordField
    private JTextField fullNameField;
    private JTextField emailField;

    public AccountPanel(Customer customer) {
        setLayout(new GridLayout(5, 2));

        // Initialize text fields with existing customer data
        usernameField = new JTextField(customer.getUsername());
        usernameField.setEditable(false); // Make username uneditable
        passwordField = new JPasswordField(customer.getPassword()); // Use JPasswordField
        fullNameField = new JTextField(customer.getFullName());
        emailField = new JTextField(customer.getEmail());

        // Add labels and text fields to the panel
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Full Name:"));
        add(fullNameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel());

        // Add a button to confirm modifications
        JButton confirmButton = new JButton("Confirm Changes");
        confirmButton.addActionListener(createConfirmActionListener(customer));
        add(confirmButton);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private ActionListener createConfirmActionListener(Customer customer) {
        return (ActionEvent e) -> {
            // Validate mandatory fields
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            String fullName = fullNameField.getText();
            String email = emailField.getText();

            if (password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(AccountPanel.this,
                        "Please fill in all mandatory fields (Password, Full Name, Email).",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if mandatory fields are not filled
            }

            // Prompt the user to enter the existing password
            char[] existingPasswordChars = promptForPassword("Enter existing password:");
            String existingPassword = new String(existingPasswordChars);

            if (!existingPassword.equals(customer.getPassword())) {
                JOptionPane.showMessageDialog(AccountPanel.this,
                        "Incorrect existing password.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if existing password is incorrect
            }

            int confirmation = JOptionPane.showConfirmDialog(AccountPanel.this,
                    "Are you sure you want to update your information?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Update the customer information with the values from the text fields
                customer.setPassword(password);
                customer.setFullName(fullName);
                customer.setEmail(email);

                // Update the customer_database.ser file
                CustomerAuthentication.updateUser(customer.getUsername(), customer);

                // Display a message indicating successful update
                JOptionPane.showMessageDialog(AccountPanel.this,
                        "Information updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        };
    }

    private char[] promptForPassword(String prompt) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(prompt);
        JPasswordField password = new JPasswordField();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(label);
        panel.add(password);
    
        int option = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Password Prompt",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    
        if (option == JOptionPane.OK_OPTION) {
            return password.getPassword();
        } else {
            return null; // Return null if the user clicks "Cancel"
        }
    }
    
}
