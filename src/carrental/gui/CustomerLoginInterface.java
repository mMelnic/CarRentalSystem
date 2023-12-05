package carrental.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;

import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.panels.CustomerLoginPanel;

public class CustomerLoginInterface extends JFrame{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton backButton;

    public CustomerLoginInterface(JFrame mainInterface, CarInventory carInventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        super("Customer Panel");

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        CustomerLoginPanel customerPanel = new CustomerLoginPanel(carInventory, rentalHistory, pricingAttributes);

        cardPanel.add(customerPanel, "Customer");

        // Back button
        backButton = new JButton("Back to Main Window");
        backButton.addActionListener(e -> {
            mainInterface.setVisible(true);
            dispose();
        });

        add(cardPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        cardLayout.show(cardPanel, "Customer");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
