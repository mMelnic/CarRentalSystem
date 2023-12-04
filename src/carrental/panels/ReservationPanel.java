package carrental.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.models.RentalRecord;
import carrental.tables.AvailableCarsTableManager;
import carrental.util.CustomerProgressTracker;

public class ReservationPanel extends JPanel {

    private final transient AvailableCarsTableManager tableManager;
    private Customer customer;
    private final CarInventory carInventory;
    private RentalHistory rentalHistory;
    private final JPanel contentPanel;
    private final PricingAttributes serviceCharges;
    private transient CustomerProgressTracker customerProgressTracker;
    private JTable unrentedCarsTable;

    public ReservationPanel(AvailableCarsTableManager tableManager, Customer customer, CarInventory carInventory, RentalHistory rentalHistory, JPanel contentPanel, PricingAttributes serviceCharges, CustomerProgressTracker progressTracker, JTable table) {
        this.tableManager = tableManager;
        this.customer = customer;
        this.carInventory = carInventory;
        this.rentalHistory = rentalHistory;
        this.contentPanel = contentPanel;
        this.serviceCharges = serviceCharges;
        this.customerProgressTracker = progressTracker;
        unrentedCarsTable = table;

        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Create search components panel
        SearchComponentsPanel searchComponentsPanel = new SearchComponentsPanel(tableManager, customer, carInventory);

        // Create table and scroll pane
        JScrollPane tableScrollPane = tableManager.createTableScrollPane(carInventory.getAvailableCarsInventoryToday());
        // Create a titled border
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Available Cars per Today");
        Font availableCarsTitleFont = new Font("Arial", Font.BOLD, 16); // Customize the font and size
        titledBorder.setTitleFont(availableCarsTitleFont);

        // Set the titled border to the scroll pane
        tableScrollPane.setBorder(titledBorder);

        // Create a Rent button
        JButton rentButton = new JButton("Rent Selected Car");

        // Add action listener for the Rent button
        rentButton.addActionListener(e -> rentSelectedCar());

        // Add search components panel and table to the panel
        add(searchComponentsPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rentButton, BorderLayout.SOUTH);
    }

    private void rentSelectedCar() {
        int selectedRow = unrentedCarsTable.getSelectedRow();
        int registrationInfoColumnIndex = 2;
    
        if (selectedRow >= 0) {
            // Get the registration info of the selected car from the table model
            String selectedRegistrationInfo = (String) unrentedCarsTable.getValueAt(selectedRow, registrationInfoColumnIndex);
            Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);
            Date[] dateRange = showDateSelectionDialog(contentPanel);

            if (dateRange.length != 0) {
                Date startDate = dateRange[0];
                Date endDate = dateRange[1];
    
                // Perform the rental process (update car inventory, display confirmation, etc.)
                boolean success = carInventory.rentCar(selectedCar, startDate, endDate);
    
                if (success) {
                    // Update the table with the new inventory
                    double finalPrice = customer.calculateRentalPrice(selectedCar, startDate, endDate, serviceCharges);
                    RentalRecord customerRecord = new RentalRecord(selectedCar, customer, finalPrice);
                    selectedCar.addRentalInterval(customerRecord.getRentId(), startDate, endDate);
                    rentalHistory.addRentalRecord(customerRecord);
                    customer = customer.checkAndUpgrade(rentalHistory);
                    customerProgressTracker.updateProgress(customer.getUsername(), rentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
                    tableManager.updateTable(carInventory.getAvailableCarsInventoryToday());
                } else {
                    // Display a message indicating that the car was not found
                    JOptionPane.showMessageDialog(contentPanel, "The car is unavailable for the selected period.", "Rental failed", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            // Display a message indicating that no car is selected
            JOptionPane.showMessageDialog(contentPanel, "Please select a car to rent.", "No Car Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static Date[] showDateSelectionDialog(Component parentComponent) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JDateChooser startDateChooser = new JDateChooser();
        JDateChooser endDateChooser = new JDateChooser();

        // Set a minimum date for the start and end date chooser
        startDateChooser.setMinSelectableDate(new Date());
        startDateChooser.addPropertyChangeListener("date", e ->
            endDateChooser.setMinSelectableDate(startDateChooser.getDate()));
        panel.add(new JLabel("Start Date:"));
        panel.add(startDateChooser);
        panel.add(new JLabel("End Date:"));
        panel.add(endDateChooser);

        int result = JOptionPane.showConfirmDialog(
                parentComponent, panel, "Select Date Range",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Date startDate = startDateChooser.getDate();
            Date endDate = endDateChooser.getDate();
            return new Date[]{startDate, endDate};
        }

        return new Date[0];
    }

}

