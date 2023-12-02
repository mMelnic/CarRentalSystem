package carrental.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.models.RentalRecord;
import carrental.util.CustomerModificationListener;
import carrental.util.CustomerProgressTracker;

public class CustomerMainWindow extends JFrame implements CustomerModificationListener{
    private Customer customer;
    private CarInventory carInventory;
    private JPanel contentPanel;
    private JTable unrentedCarsTable;
    private RentalHistory customersRentalHistory;
    private PricingAttributes pricingAttributes;
    private transient CustomerProgressTracker progressTracker;
    private RentalHistoryPanel rentalHistoryPanel;
    private RentalHistory currentCustomerHistory;
    private CustomerProgressBarPanel progressBarPanel;

    public CustomerMainWindow(Customer customer, CarInventory carInventory, RentalHistory entireRentalHistory, PricingAttributes pricingAttributes) {
        this.customer = customer;
        this.carInventory = carInventory;
        customersRentalHistory = entireRentalHistory;
        this.pricingAttributes = pricingAttributes;
        this.progressTracker = new CustomerProgressTracker();
        progressTracker.updateProgress(customer.getUsername(), customersRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        initializeComponents();

        // Add a WindowListener to handle window closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Serialize the car inventory when the window is closing
                carInventory.serializeCarInventory("carInventory.ser");
                customersRentalHistory.saveToFile("rental_history.ser");
            }
        });
    }

    private void initializeComponents() {
        setTitle("Car Rental System - Customer");
        setResizable(true);
        setSize(1100, 800);
        setLocationRelativeTo(null); // Center the window on the screen

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
            BorderFactory.createEmptyBorder(15, 5, 10, 5) // Empty border (top, left, bottom, right)
        ));

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
            new UserInterface(carInventory, customersRentalHistory, pricingAttributes);
            carInventory.serializeCarInventory("carInventory.ser");
            customersRentalHistory.saveToFile("rental_history.ser");
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
    
        // Create search components panel
        JPanel searchComponentsPanel = createSearchComponentsPanel();
    
        // Create table and scroll pane
        JScrollPane tableScrollPane = createTableScrollPane();

        // Create a Rent button
        JButton rentButton = new JButton("Rent Selected Car");

        // Add action listener for the Rent button
        rentButton.addActionListener(e -> rentSelectedCar());
    
        // Add search components panel and table to the content panel
        contentPanel.add(searchComponentsPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(rentButton, BorderLayout.SOUTH);
    
        // Repaint and revalidate to update the UI
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createSearchComponentsPanel() {
        // Create text fields for input
        JTextField manufacturerField = new JTextField(10);
        JTextField modelField = new JTextField(10);
        JComboBox<Car.ComfortLevel> comfortLevelComboBox = new JComboBox<>(Car.ComfortLevel.values());
        JCheckBox gpsCheckBox = new JCheckBox("GPS");
        JCheckBox childSeatCheckBox = new JCheckBox("Child Seat");
        JCheckBox insuranceCheckBox = new JCheckBox("Insurance");
        JCheckBox leatherInteriorCheckBox = new JCheckBox("Leather Interior");
        JCheckBox sunroofCheckBox = new JCheckBox("Sunroof");
        JCheckBox hybridTechnologyCheckBox = new JCheckBox("Hybrid Technology");
    
        // Create search and clear buttons
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear All");
    
        // Create panels for each row
        JPanel row1Panel = new JPanel();
        row1Panel.add(new JLabel("Manufacturer:"));
        row1Panel.add(manufacturerField);
        row1Panel.add(new JLabel("Model:"));
        row1Panel.add(modelField);
        row1Panel.add(new JLabel("Comfort Level:"));
        row1Panel.add(comfortLevelComboBox);
    
        JPanel row2Panel = new JPanel();
        row2Panel.add(gpsCheckBox);
        row2Panel.add(childSeatCheckBox);
        row2Panel.add(insuranceCheckBox);
        row2Panel.add(leatherInteriorCheckBox);
        row2Panel.add(sunroofCheckBox);
        row2Panel.add(hybridTechnologyCheckBox);
    
        JPanel row3Panel = new JPanel();
        row3Panel.add(searchButton);
        row3Panel.add(clearButton);

        // Create JCalendar date choosers for start and end dates
        JDateChooser startDateChooser = new JDateChooser();
        JDateChooser endDateChooser = new JDateChooser();

        // Set a minimum date for the start and end date chooser
        startDateChooser.setMinSelectableDate(new Date());
        startDateChooser.addPropertyChangeListener("date", e ->
            endDateChooser.setMinSelectableDate(startDateChooser.getDate()));

        // Set the date format for the date choosers
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setDateFormatString("yyyy-MM-dd");

        // Add date choosers to the UI
        JPanel dateChooserPanel = new JPanel();
        dateChooserPanel.add(new JLabel("Start Date:"));
        dateChooserPanel.add(startDateChooser);
        dateChooserPanel.add(new JLabel("End Date:"));
        dateChooserPanel.add(endDateChooser);
    
        // Create a parent panel using GridLayout with 3 rows and 1 column
        JPanel searchComponentsPanel = new JPanel(new GridLayout(4, 1));
    
        // Add row panels to the parent panel
        searchComponentsPanel.add(row1Panel);
        searchComponentsPanel.add(row2Panel);
        searchComponentsPanel.add(dateChooserPanel);
        searchComponentsPanel.add(row3Panel);
    
        // Add action listeners for search and clear buttons
        searchButton.addActionListener(e -> {
            // Get values from the input fields
            String manufacturer = manufacturerField.getText();
            String model = modelField.getText();
            Car.ComfortLevel comfortLevel = (Car.ComfortLevel) comfortLevelComboBox.getSelectedItem();

            // Get selected start and end dates
            Date startDate = startDateChooser.getDate();
            Date endDate = endDateChooser.getDate();
    
            // Create a set of selected additional features
            Set<Car.AdditionalFeatures> selectedFeatures = new HashSet<>();
            if (gpsCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.GPS);
            if (childSeatCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.CHILD_SEAT);
            if (insuranceCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.INSURANCE);
            if (leatherInteriorCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.LEATHER_INTERIOR);
            if (sunroofCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.SUNROOF);
            if (hybridTechnologyCheckBox.isSelected()) selectedFeatures.add(Car.AdditionalFeatures.HYBRID_TECHNOLOGY);

            // Perform the search and update the table
            CarInventory searchResults = customer.searchCarToRent(manufacturer, model, comfortLevel, selectedFeatures, startDate, endDate, carInventory);
            updateTableWithSearchResults(searchResults);
        });
    
        clearButton.addActionListener(e -> {
            // Clear all input fields and restore the initial table
            manufacturerField.setText("");
            modelField.setText("");
            comfortLevelComboBox.setSelectedIndex(0);
            gpsCheckBox.setSelected(false);
            childSeatCheckBox.setSelected(false);
            insuranceCheckBox.setSelected(false);
            leatherInteriorCheckBox.setSelected(false);
            sunroofCheckBox.setSelected(false);
            hybridTechnologyCheckBox.setSelected(false);
            startDateChooser.setDate(null);
            endDateChooser.setDate(null);
    
            updateTableWithSearchResults(carInventory.getAvailableCarsInventoryToday());
        });
    
        return searchComponentsPanel;
    }
    
    private JScrollPane createTableScrollPane() {
        // Create a table model for the unrented cars
        DefaultTableModel tableModel = createTableModelForUnrentedCars(carInventory.getAvailableCarsInventoryToday());
    
        // Create a JTable with the table model
        unrentedCarsTable = new JTable(tableModel);
        adjustColumnSizes(unrentedCarsTable);
        // Set the auto-resize mode to adjust columns based on content
        unrentedCarsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); //TODO check if it works in the createTableModel and updataTable model instead of adjustColumnSizes
        // Set the selection mode to allow single-row selection
        unrentedCarsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        // Add the table to a scroll pane for better visibility
        return new JScrollPane(unrentedCarsTable);
    }
    
    private void updateTableWithSearchResults(CarInventory searchResults) {
        DefaultTableModel tableModel = createTableModelForUnrentedCars(searchResults);
        unrentedCarsTable = (JTable) ((JScrollPane) contentPanel.getComponent(1)).getViewport().getView();
        unrentedCarsTable.setModel(tableModel);
        adjustColumnSizes(unrentedCarsTable);
        unrentedCarsTable.repaint();
        unrentedCarsTable.revalidate();
    }

    private void adjustColumnSizes(JTable table) {
        // Adjust column widths based on content
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int maxWidth = 0;

            // Find the maximum width of the content in each column
            for (int j = 0; j < table.getRowCount(); j++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(j, i);
                Object value = table.getValueAt(j, i);
                Component cellComponent = cellRenderer.getTableCellRendererComponent(table, value, false, false, j, i);
                maxWidth = Math.max(maxWidth, cellComponent.getPreferredSize().width);
            }

            // Set the column width to the maximum content width + some padding
            column.setPreferredWidth(maxWidth + 10);
        }
    }
    

    private DefaultTableModel createTableModelForUnrentedCars(CarInventory inventory) {

        // Assuming Car class has methods like getManufacturer(), getModel(), etc.
        String[] columnNames = {"Manufacturer", "Model", "Registration Info", "Color", "Year of Production", "Price/day", "Comfort Level", "Additional Features"};

        // Assuming Car class has a method like getCarData() to retrieve an array of car data
        Object[][] data = new Object[inventory.getCarList().size()][columnNames.length];

        for (int i = 0; i < inventory.getCarList().size(); i++) {
            Car car = inventory.getCarList().get(i);
            data[i] = car.getCarData();  // Adjust this based on how your Car class provides data
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };
    }

    private void rentSelectedCar() {
        // Get the selected row from the table
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
                    double finalPrice = customer.calculateRentalPrice(selectedCar, startDate, endDate, pricingAttributes);
                    RentalRecord customerRecord = new RentalRecord(selectedCar, customer, finalPrice);
                    selectedCar.addRentalInterval(customerRecord.getRentId(), startDate, endDate);
                    customersRentalHistory.addRentalRecord(customerRecord);
                    customer = customer.checkAndUpgrade(customersRentalHistory);
                    progressTracker.updateProgress(customer.getUsername(), customersRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
                    updateTableWithSearchResults(carInventory.getAvailableCarsInventoryToday());
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

    private void showAccountView() {
        contentPanel.removeAll();

        // Create instances of RentalHistoryPanel and AccountPanel
        currentCustomerHistory = customersRentalHistory.getRentalHistoryForCustomer(customer.getUsername());
        rentalHistoryPanel = new RentalHistoryPanel(currentCustomerHistory);
        AccountPanel accountPanel = new AccountPanel(customer);
        progressBarPanel = new CustomerProgressBarPanel(progressTracker, customer.getUsername(),
                customersRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));

        // Create FutureReservationsPanel with the customer's rental records
        
        FutureReservationsPanel futureReservationsPanel = new FutureReservationsPanel(
                currentCustomerHistory.getCustomerRentalMap().get(customer.getUsername()), carInventory, customersRentalHistory, customer);

        futureReservationsPanel.setCustomerModificationListener(this);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10)); // Adjust the values as needed

        northPanel.add(accountPanel);
        northPanel.add(progressBarPanel);
        northPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        northPanel.add(futureReservationsPanel);

        // Add the container panel to the content panel
        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(rentalHistoryPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void onCustomerModified(Customer modifiedCustomer) {
        this.customer = modifiedCustomer;
        customersRentalHistory.updateOriginalHistory(currentCustomerHistory);
        progressTracker.updateProgress(customer.getUsername(),
                customersRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        progressBarPanel.setNumberOfReservations(customersRentalHistory.getNumberOfReservationsForCustomer(customer.getUsername()));
        progressBarPanel.updateProgressBar();
        rentalHistoryPanel.updateTextArea(currentCustomerHistory);
    }


}
