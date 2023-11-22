package carrental.gui;

import java.awt.GridLayout;

import javax.swing.*;

import carrental.exceptions.AccountCreationException;
import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.models.RentalHistory;
import carrental.models.User;
import carrental.util.CustomerAuthentication;

public class CustomerLoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton loginButton;
    private JButton createAccountButton;
    private CarInventory carInventory;
    private RentalHistory rentalHistory;

    public CustomerLoginPanel(CarInventory carInventory, RentalHistory rentalHistory) {
        this.carInventory = carInventory;
        this.rentalHistory = rentalHistory;
        initComponents();
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Full Name:"));
        add(fullNameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(loginButton);
        add(createAccountButton);

        loginButton.addActionListener(e -> customerLogin());
        createAccountButton.addActionListener(e -> customerCreateAccount());
    }

    private void initComponents() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        fullNameField = new JTextField();
        emailField = new JTextField();
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
    }

    private void customerLogin() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String email = emailField.getText();

    Customer authenticatedUser = CustomerAuthentication.authenticateUser(username, password, email);

    if (authenticatedUser != null) {
        JOptionPane.showMessageDialog(this, "Customer Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Create and show the CustomerMainWindow
        CustomerMainWindow customerMainWindow = new CustomerMainWindow(authenticatedUser, carInventory, rentalHistory);
        customerMainWindow.setVisible(true);

        // Close the current login window if needed
        SwingUtilities.getWindowAncestor(this).dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Customer Login failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void customerCreateAccount() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();

        Customer newUser = new Customer(username, password, fullName, email);
        try {
            CustomerAuthentication.createUser(newUser);
            JOptionPane.showMessageDialog(this, "Customer Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (AccountCreationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
