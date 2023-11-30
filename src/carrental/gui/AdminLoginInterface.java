package carrental.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;

import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;

public class AdminLoginInterface extends JFrame{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton backButton;

    public AdminLoginInterface(JFrame mainInterface, CarInventory inventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        super("Administrator Panel");

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        AdminLoginPanel adminPanel = new AdminLoginPanel(inventory, rentalHistory, pricingAttributes);

        cardPanel.add(adminPanel, "Administrator");

        // Back button
        backButton = new JButton("Back to Main Window");
        backButton.addActionListener(e -> {
            mainInterface.setVisible(true);
            dispose(); // Close the customer window
        });

        add(cardPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        cardLayout.show(cardPanel, "Administrator");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
