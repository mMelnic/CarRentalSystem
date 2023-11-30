package carrental.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;

public class UserInterface extends JFrame {
    private JButton customerButton;
    private JButton adminButton;

    public UserInterface(CarInventory inventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        super("Car Rental System");

        customerButton = new JButton("Customer Panel");
        adminButton = new JButton("Administrator Panel");

        setLayout(new GridLayout(2, 1));
        add(customerButton);
        add(adminButton);

        customerButton.addActionListener(e -> openCustomerPanel(inventory, rentalHistory, pricingAttributes));
        adminButton.addActionListener(e -> openAdminPanel(inventory, rentalHistory, pricingAttributes));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void openCustomerPanel(CarInventory carInventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        new CustomerLoginInterface(this, carInventory, rentalHistory, pricingAttributes);
        setVisible(false);
    }

    private void openAdminPanel(CarInventory carInventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        new AdminLoginInterface(this, carInventory, rentalHistory, pricingAttributes);
        setVisible(false);
    }
}
