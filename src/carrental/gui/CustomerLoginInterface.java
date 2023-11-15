package carrental.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;

public class CustomerLoginInterface extends JFrame{
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton backButton;

    public CustomerLoginInterface(JFrame mainInterface) {
        super("Customer Panel");

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // CustomerPanel instance
        CustomerLoginPanel customerPanel = new CustomerLoginPanel();

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
