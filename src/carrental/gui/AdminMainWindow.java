package carrental.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import carrental.models.Administrator;
import carrental.models.Car;
import carrental.models.Car.AdditionalFeatures;
import carrental.models.Car.ComfortLevel;
import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;

public class AdminMainWindow extends JFrame {
    private Administrator authenticatedUser;
    private JPanel contentPanel;
    private CarInventory carInventory;
    private JTable carTable; 
    private PricingAttributes pricingAttributes;

    public AdminMainWindow(Administrator authenticatedUser, CarInventory carInventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        this.authenticatedUser = authenticatedUser;
        this.carInventory = carInventory;
        this.pricingAttributes = pricingAttributes;
        initializeComponents(rentalHistory);

        // Add a WindowListener to handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Serialize the car inventory when the window is closing
                carInventory.serializeCarInventory("carInventory.ser");
            }
        });
    }

    private void initializeComponents(RentalHistory rentalHistory) {
        setTitle("Car Rental System");
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a side panel for navigation
        JPanel sidePanel = createSidePanel(rentalHistory);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Initialize the content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Show the car database view by default
        showCarDatabaseView();

        // Add the content panel to the main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Set the main panel as the content pane of the frame
        setContentPane(mainPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel createSidePanel(RentalHistory rentalHistory) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK), // Left border
                BorderFactory.createEmptyBorder(50, 10, 0, 10) // Empty border (top, left, bottom, right)
        ));

        // Add title
        JLabel titleLabel = new JLabel("Admin Main Window");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sidePanel.add(titleLabel);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton carDatabaseButton = new JButton("Car Database");
        JButton rentalHistoryButton = new JButton("Rental History");
        JButton pricesButton = new JButton("Service Prices");
        JButton logoutButton = new JButton("Log Out");

        // Add action listeners for navigation buttons
        carDatabaseButton.addActionListener(e -> showCarDatabaseView());
        rentalHistoryButton.addActionListener(e -> showRentalHistoryView(rentalHistory));
        pricesButton.addActionListener(e -> showPricesPanelView());
        logoutButton.addActionListener(e -> {
            new UserInterface(carInventory, rentalHistory, pricingAttributes);
            carInventory.serializeCarInventory("carInventory.ser");
            dispose();
        });

        // Add buttons to the side panel
        carDatabaseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rentalHistoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pricesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(carDatabaseButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(rentalHistoryButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(pricesButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(logoutButton);

        return sidePanel;
    }

    private void showCarDatabaseView() {
        // Logic to switch to the car database view
        contentPanel.removeAll();
    
        // Create a panel for car information entry
        JPanel carInfoPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
    
        // Add labels and text fields for car information
        addFormField(fieldsPanel, gbc, "Manufacturer:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Model:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Registration Info:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Color:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Year of Production:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Price:", new JTextField(10));
        addFormField(fieldsPanel, gbc, "Comfort Level:", new JComboBox<>(ComfortLevel.values()));
    
        // Add additional features checkboxes
        addFormField(fieldsPanel, gbc, "Additional Features:", createFeaturesPanel());
    
        // Add the "Add" button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JButton addButton = new JButton("Add");
        fieldsPanel.add(addButton, gbc);
    
        // Set an empty border to create space
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Create a titled border with a custom font and size
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Car Information Entry");
        Font titleFont = new Font("Arial", Font.BOLD, 18); // Customize the font and size
        titledBorder.setTitleFont(titleFont);
    
        // Set the titled border to the carInfoPanel
        carInfoPanel.setBorder(titledBorder);
    
        // Add the car information entry panel to the content panel
        carInfoPanel.add(fieldsPanel, BorderLayout.WEST);
    
        // Call the method to set up the action listener for the "Add" button
        setupAddButtonListener(addButton, fieldsPanel);
    
        // Add the car information entry panel to the content panel
        contentPanel.add(carInfoPanel, BorderLayout.NORTH);
    
        // Create and add the table panel to the content panel (CENTER)
        List<Car> carList = carInventory.getAvailableCarsInventoryToday().getCarList();
        carTable = createCarTable(carList);
    
        // Create a titled border with a custom font and size for the table
        TitledBorder tableTitleBorder = BorderFactory.createTitledBorder("Cars for Rent");
        Font tableTitleFont = new Font("Arial", Font.BOLD, 16); // Customize the font and size
        tableTitleBorder.setTitleFont(tableTitleFont);
    
        // Create a panel for the title and table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(tableTitleBorder);
    
        // Add the carTable to the tablePanel
        JScrollPane tableScrollPane = new JScrollPane(carTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
    
        // Add the tablePanel to the content panel
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Create a titled border with a custom font and size for the rented cars table
        TitledBorder rentedCarsTitleBorder = BorderFactory.createTitledBorder("Rented Cars");
        Font rentedCarsTitleFont = new Font("Arial", Font.BOLD, 16); // Customize the font and size
        rentedCarsTitleBorder.setTitleFont(rentedCarsTitleFont);

        // Create a panel for the title and rented cars table
        JPanel rentedCarsPanel = new JPanel(new BorderLayout());
        rentedCarsPanel.setBorder(rentedCarsTitleBorder);

        // Create the rented cars table and make it non-editable
        List<Car> rentedCarsList = carInventory.getUnavailableCarsInventoryToday().getCarList(); // Replace this with your actual rented cars list
        JTable rentedCarsTable = createNonEditableCarTable(rentedCarsList);
        rentedCarsTable.setPreferredScrollableViewportSize(new Dimension(rentedCarsTable.getPreferredSize().width, 200));

        // Create a button to return the selected car
        JButton returnSelectedCarButton = new JButton("Return Selected Car");
        returnSelectedCarButton.addActionListener(createReturnSelectedCarActionListener(rentedCarsTable, carInventory));

        // Add the rentedCarsTable and the button to the rentedCarsPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnSelectedCarButton);

        JScrollPane rentedCarsScrollPane = new JScrollPane(rentedCarsTable);
        rentedCarsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rentedCarsPanel.add(rentedCarsScrollPane, BorderLayout.CENTER);
        rentedCarsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the rentedCarsPanel to the content panel (SOUTH)
        contentPanel.add(rentedCarsPanel, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void setupAddButtonListener(JButton addButton, JPanel fieldsPanel) {
        addButton.addActionListener(e -> addNewCar(fieldsPanel));
    }

    private ActionListener createReturnSelectedCarActionListener(JTable rentedCarsTable, CarInventory carInventory) {
        return (ActionEvent e) -> {
            int selectedRow = rentedCarsTable.getSelectedRow();
            int registrationInfoColumnIndex = 2;

            if (selectedRow != -1) {
                // Get the selected car from the table model
                String selectedRegistrationInfo = (String) rentedCarsTable.getValueAt(selectedRow, registrationInfoColumnIndex);
                Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);  

                // Call the returnCar method in your CarInventory to update the rental status
                carInventory.returnCarToday(selectedCar);
    
                // Refresh the table model to reflect the changes
                DefaultTableModel model = (DefaultTableModel) rentedCarsTable.getModel();
                model.removeRow(selectedRow);
                updateCarTable();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a car to return.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
    

    private void addNewCar(JPanel fieldsPanel) {
        // Extract data from fields
        String manufacturer = ((JTextField) fieldsPanel.getComponent(1)).getText();
        String model = ((JTextField) fieldsPanel.getComponent(3)).getText();
        String registrationInfo = ((JTextField) fieldsPanel.getComponent(5)).getText();
        String color = ((JTextField) fieldsPanel.getComponent(7)).getText();

        JTextField yearField = (JTextField) fieldsPanel.getComponent(9);
        JTextField priceField = (JTextField) fieldsPanel.getComponent(11);

        ComfortLevel comfortLevel = (ComfortLevel) ((JComboBox<?>) fieldsPanel.getComponent(13)).getSelectedItem();

        // Extract selected additional features
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        for (Component component : ((JPanel) fieldsPanel.getComponent(15)).getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    additionalFeatures.add(AdditionalFeatures.valueOf(checkBox.getText()));
                }
            }
        }

        // Validate the required fields
        if (manufacturer.isEmpty() || model.isEmpty() || registrationInfo.isEmpty() || color.isEmpty()
                || yearField.getText().isEmpty() || priceField.getText().isEmpty()) {
            // Show a warning message dialog if any of the required fields is not filled
            JOptionPane.showMessageDialog(fieldsPanel, "Please fill in all required fields.", "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int yearOfProduction = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());

            // Create a new car
            Car newCar = new Car(manufacturer, model, registrationInfo, color, yearOfProduction, price, comfortLevel,
                    additionalFeatures);

            // Add the new car to the CarInventory
            authenticatedUser.addCar(newCar, carInventory);

            // Update the displayed table
            updateCarTable();
        } catch (NumberFormatException e) {
            // Handle the case where the entered year or price is not a valid number
            JOptionPane.showMessageDialog(fieldsPanel, "Please enter valid numeric values for year and price.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JTable createCarTable(List<Car> carList) {
        // Convert car data to a two-dimensional array for the table
        Object[][] data = new Object[carList.size()][8];
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            data[i][0] = car.getManufacturer();
            data[i][1] = car.getModel();
            data[i][2] = car.getRegistrationInfo();
            data[i][3] = car.getColor();
            data[i][4] = car.getYearOfProduction();
            data[i][5] = car.getPrice();
            data[i][6] = car.getComfortLevel();
            data[i][7] = car.getAdditionalFeatures();
        }
    
        // Create a table model
        DefaultTableModel tableModel = new DefaultTableModel(data, new String[]{"Manufacturer", "Model", "Registration Info", "Color", "Year of Production", "Price", "Comfort Level", "Additional Features"});
    
        JTable table = new JTable(tableModel);
        // Set up the ListSelectionListener
        setupTableSelectionListener(table, carList);

        return table;
    }

    private void updateCarTable() {
        // Retrieve the car data from the CarInventory
        List<Car> carList = new ArrayList<>(carInventory.getAvailableCarsInventoryToday().getCarMap().values());
    
        // Create and set the new data to the table model
        DefaultTableModel tableModel = (DefaultTableModel) carTable.getModel();
        tableModel.setDataVector(createTableData(carList), new String[]{"Manufacturer", "Model", "Registration Info", "Color", "Year of Production", "Price", "Comfort Level", "Additional Features"});
    
        // ListSelectionListener
        setupTableSelectionListener(carTable, carList);
    
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setupTableSelectionListener(JTable carTable, List<Car> carList) {
        carTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = carTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Display car details for editing
                    displayCarDetailsForEditing(carList.get(selectedRow));
                }
            }
        });
    }
    
    private void displayCarDetailsForEditing(Car car) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
        JTextField manufacturerField = new JTextField(car.getManufacturer());
        JTextField modelField = new JTextField(car.getModel());
        JTextField registrationInfoField = new JTextField(car.getRegistrationInfo());
        registrationInfoField.setEditable(false);
        JTextField colorField = new JTextField(car.getColor());
        JTextField yearOfProductionField = new JTextField(String.valueOf(car.getYearOfProduction()));
        JTextField priceField = new JTextField(String.valueOf(car.getPrice()));
        JComboBox<ComfortLevel> comfortLevelComboBox = new JComboBox<>(ComfortLevel.values());
    
        // Set the current comfort level as selected
        comfortLevelComboBox.setSelectedItem(car.getComfortLevel());
    
        // Add additional features checkboxes
        JPanel featuresPanel = createFeaturesPanel();
        for (Component component : featuresPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setSelected(car.getAdditionalFeatures().contains(AdditionalFeatures.valueOf(checkBox.getText())));
            }
        }
    
        JPanel editPanel = new JPanel(new GridLayout(0, 2));
        editPanel.add(new JLabel("Manufacturer:"));
        editPanel.add(manufacturerField);
        editPanel.add(new JLabel("Model:"));
        editPanel.add(modelField);
        editPanel.add(new JLabel("Registration Info:"));
        editPanel.add(registrationInfoField);
        editPanel.add(new JLabel("Color:"));
        editPanel.add(colorField);
        editPanel.add(new JLabel("Year of Production:"));
        editPanel.add(yearOfProductionField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Comfort Level:"));
        editPanel.add(comfortLevelComboBox);
        editPanel.add(new JLabel("Additional Features:"));
        editPanel.add(featuresPanel);

        // Add a "Delete" button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // Check if the car can be deleted
            if (car.canCarBeDeleted()) {
                int confirm = JOptionPane.showConfirmDialog(contentPanel, "Are you sure you want to delete this car?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete the car from the table and the inventory
                    deleteCar(car);
                    // Close the edit window
                    Window window = SwingUtilities.getWindowAncestor(deleteButton);
                    if (window != null) {
                        window.dispose();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(contentPanel,
                        "The car cannot be deleted because it has active rental intervals.", "Cannot Delete Car",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        int windowHeight = 400;
        int windowWidth = 700;
        editPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));

        // Panel to hold the "Delete" button
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deletePanel.add(deleteButton);

        // Add the deletePanel to the editPanel
        editPanel.add(deletePanel);

        int result = JOptionPane.showConfirmDialog(contentPanel, editPanel, "Edit Car Details", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            if (manufacturerField.getText().isEmpty() || modelField.getText().isEmpty()
                    || registrationInfoField.getText().isEmpty() || colorField.getText().isEmpty()
                    || yearOfProductionField.getText().isEmpty() || priceField.getText().isEmpty()) {
                // Show a warning message dialog if any of the required fields is not filled
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return; // Prevent the update if any required field is empty
            }

            // Update the car object with the modified data
            car.setManufacturer(manufacturerField.getText());
            car.setModel(modelField.getText());
            car.setRegistrationInfo(registrationInfoField.getText());
            car.setColor(colorField.getText());
            car.setYearOfProduction(Integer.parseInt(yearOfProductionField.getText()));
            car.setPrice(Double.parseDouble(priceField.getText()));
            car.setComfortLevel((ComfortLevel) comfortLevelComboBox.getSelectedItem());
    
            Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
            for (Component component : featuresPanel.getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        additionalFeatures.add(AdditionalFeatures.valueOf(checkBox.getText()));
                    }
                }
            }
            car.setAdditionalFeatures(additionalFeatures);
    
            // Update the displayed table
            updateCarTable();
        }
    }

    private void deleteCar(Car car) {
        // Remove the car from the CarInventory
        authenticatedUser.removeCar(car.getRegistrationInfo(), carInventory);
    
        // Update the displayed table
        updateCarTable();
    }
    
    private Object[][] createTableData(List<Car> carList) {
        // Convert car data to a two-dimensional array for the table
        Object[][] data = new Object[carList.size()][8];
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            data[i][0] = car.getManufacturer();
            data[i][1] = car.getModel();
            data[i][2] = car.getRegistrationInfo();
            data[i][3] = car.getColor();
            data[i][4] = car.getYearOfProduction();
            data[i][5] = car.getPrice();
            data[i][6] = car.getComfortLevel();
            data[i][7] = car.getAdditionalFeatures();
        }
        return data;
    }
    
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
    
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(component, gbc);
    
        gbc.gridy++;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel featuresPanel = new JPanel(new GridLayout(2, 3));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.GPS.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.CHILD_SEAT.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.INSURANCE.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.LEATHER_INTERIOR.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.SUNROOF.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.HYBRID_TECHNOLOGY.name()));
        return featuresPanel;
    }

    private JTable createNonEditableCarTable(List<Car> cars) {
        String[] columnNames = {"Manufacturer", "Model", "Registration Info", "Color", "Year of Production", "Price", "Comfort Level", "Additional Features"};
    
        Object[][] data = new Object[cars.size()][columnNames.length];
    
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            data[i][0] = car.getManufacturer();
            data[i][1] = car.getModel();
            data[i][2] = car.getRegistrationInfo();
            data[i][3] = car.getColor();
            data[i][4] = car.getYearOfProduction();
            data[i][5] = car.getPrice();
            data[i][6] = car.getComfortLevel();
            data[i][7] = car.getAdditionalFeatures();
        }
    
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
    
        return new JTable(tableModel);
    }

    private void showRentalHistoryView(RentalHistory rentalHistory) {
        // Logic to switch to the rental history view
        contentPanel.removeAll();
        RentalHistorySearchPanel rentalHistoryPanel = new RentalHistorySearchPanel(rentalHistory);
        contentPanel.add(rentalHistoryPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPricesPanelView() {
        // Logic to switch to the price setter view
        contentPanel.removeAll();
        PriceSetterPanel priceSetterPanel = new PriceSetterPanel(pricingAttributes);
        contentPanel.add(priceSetterPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}