package carrental.gui;

import javax.swing.*;

import java.awt.*;

import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.tables.AvailableCarsTableManager;
import carrental.util.CustomerModificationListener;
import carrental.util.CustomerProgressTracker;
import carrental.util.PriceUpdateListener;

public class CustomerMainWindow extends JFrame implements CustomerModificationListener, PriceUpdateListener {
    private Customer customer;
    private CarInventory carInventory;
    private JPanel contentPanel;
    private JTable unrentedCarsTable;
    private RentalHistory entireRentalHistory;
    private PricingAttributes pricingAttributes;
    private transient CustomerProgressTracker progressTracker;
    private RentalHistoryPanel rentalHistoryPanel;
    private RentalHistory currentCustomerHistory;
    private CustomerProgressBarPanel progressBarPanel;
    private transient AvailableCarsTableManager tableManager;

    public CustomerMainWindow(Customer customer, CarInventory carInventory, RentalHistory entireRentalHistory, PricingAttributes pricingAttributes) {
        this.customer = customer;
        this.carInventory = carInventory;
        this.entireRentalHistory = entireRentalHistory;
        this.pricingAttributes = pricingAttributes;
        unrentedCarsTable = new JTable();
        tableManager = new AvailableCarsTableManager(unrentedCarsTable);
        this.progressTracker = new CustomerProgressTracker();

        progressTracker.updateProgress(customer.getUsername(), entireRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        initializeComponents();

        // Add a WindowListener to handle window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Serialize the car inventory when the window is closing
                carInventory.serializeCarInventory("carInventory.ser");
                entireRentalHistory.saveRentalHistoryToFile("rental_history.ser");
            }
        });
    }

    private void initializeComponents() {
        setTitle("Car Rental System - Customer");
        setResizable(true);
        setSize(1100, 800);
        setLocationRelativeTo(null);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a side panel for navigation
        JPanel sidePanel = createSidePanel();
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Initialize the content panel
        contentPanel = new JPanel(new BorderLayout());

        // Show the Reservation view by default
        showReservationView();

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Set the main panel as the content pane of the frame
        setContentPane(mainPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK), // Left border
            BorderFactory.createEmptyBorder(35, 5, 0, 5) // Empty border (top, left, bottom, right)
        ));

        sidePanel.setBackground(Color.WHITE);
        // Add title
        JLabel titleLabel = new JLabel("Customer Main Window");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sidePanel.add(titleLabel);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton reservationButton = new JButton("Reservation");
        JButton accountButton = new JButton("Account");
        JButton logoutButton = new JButton("Log Out");

        // Add action listeners for navigation buttons
        reservationButton.addActionListener(e -> showReservationView());
        accountButton.addActionListener(e -> showAccountView());
        logoutButton.addActionListener(e -> {
            new UserInterface(carInventory, entireRentalHistory, pricingAttributes);
            carInventory.serializeCarInventory("carInventory.ser");
            entireRentalHistory.saveRentalHistoryToFile("rental_history.ser");
            dispose();
        });

        // Add buttons to the side panel
        reservationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(reservationButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(accountButton);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        sidePanel.add(logoutButton);

        return sidePanel;
    }

    private void showReservationView() {
        // Remove any existing components in the content panel
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        contentPanel.add(new ReservationPanel(tableManager, customer, carInventory, entireRentalHistory, contentPanel, pricingAttributes, progressTracker, unrentedCarsTable));
    
        // Repaint and revalidate to update the UI
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAccountView() {
        contentPanel.removeAll();

        rentalHistoryPanel = createRentalHistoryPanel();
        AccountPanel accountPanel = createAccountPanel();
        progressBarPanel = createProgressBarPanel();
        FutureReservationsPanel futureReservationsPanel = createFutureReservationsPanel();

        JPanel northPanel = createNorthPanel(accountPanel, progressBarPanel, futureReservationsPanel);

        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(rentalHistoryPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private RentalHistoryPanel createRentalHistoryPanel() {
        currentCustomerHistory = entireRentalHistory.getRentalHistoryForCustomer(customer.getUsername());
        return new RentalHistoryPanel(currentCustomerHistory);
    }

    private AccountPanel createAccountPanel() {
        return new AccountPanel(customer);
    }

    private CustomerProgressBarPanel createProgressBarPanel() {
        return new CustomerProgressBarPanel(progressTracker, customer.getUsername(),
                entireRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
    }

    private FutureReservationsPanel createFutureReservationsPanel() {
        FutureReservationsPanel futureReservationsPanel = new FutureReservationsPanel(
                currentCustomerHistory.getCustomerRentalMap().get(customer.getUsername()),
                carInventory, entireRentalHistory, customer, pricingAttributes);
        futureReservationsPanel.setCustomerModificationListener(this);
        futureReservationsPanel.setPriceUpdateListener(this);
        return futureReservationsPanel;
    }

    private JPanel createNorthPanel(AccountPanel accountPanel, CustomerProgressBarPanel progressBarPanel,
            FutureReservationsPanel futureReservationsPanel) {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

        northPanel.add(accountPanel);
        northPanel.add(progressBarPanel);
        northPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        northPanel.add(futureReservationsPanel);

        return northPanel;
    }

    @Override
    public void onCustomerModified(Customer modifiedCustomer) {
        this.customer = modifiedCustomer;
        entireRentalHistory.updateOriginalHistory(currentCustomerHistory);
        progressTracker.updateProgress(customer.getUsername(),
                entireRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        progressBarPanel.setNumberOfReservations(entireRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        progressBarPanel.updateProgressBar();
        rentalHistoryPanel.updateTextArea(currentCustomerHistory);
    }

    @Override
    public void onPriceUpdate() {
        rentalHistoryPanel.updateTextArea(currentCustomerHistory);
    }
}
