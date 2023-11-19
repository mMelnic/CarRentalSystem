package carrental.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import carrental.models.CarInventory;

public class UserInterface extends JFrame {
    private JButton customerButton;
    private JButton adminButton;
    private CarInventory carInventory;

    public UserInterface(CarInventory inventory) {
        super("Car Rental System");

        customerButton = new JButton("Customer Panel");
        adminButton = new JButton("Administrator Panel");

        setLayout(new GridLayout(2, 1));
        add(customerButton);
        add(adminButton);

        customerButton.addActionListener(e -> openCustomerPanel());
        adminButton.addActionListener(e -> openAdminPanel());

        carInventory = inventory;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openCustomerPanel() {
        new CustomerLoginInterface(this);
        setVisible(false);
    }

    private void openAdminPanel() {
        new AdminLoginInterface(this, carInventory);
        setVisible(false);
    }
}
