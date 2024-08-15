package carrental.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import carrental.models.Administrator;
import carrental.models.Car.ComfortLevel;
import carrental.panels.PriceSetterPanel;
import carrental.panels.RentalHistorySearchPanel;
import carrental.tables.AvailableCarsTableManager;
import carrental.tables.CarTableManager;
import carrental.util.AdminUtil;
import carrental.util.CarEditor;
import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;

public class AdminMainWindow extends JFrame {
    private Administrator authenticatedUser;
    private JPanel contentPanel;
    private CarInventory carInventory;
    private JTable unrentedCarsTable;
    private PricingAttributes pricingAttributes;
    private JTable rentedCarsTable;
    private transient AvailableCarsTableManager tableManager;
    private transient CarTableManager carTableManager;

    public AdminMainWindow(Administrator authenticatedUser, CarInventory carInventory, RentalHistory rentalHistory, PricingAttributes pricingAttributes) {
        this.authenticatedUser = authenticatedUser;
        this.carInventory = carInventory;
        this.pricingAttributes = pricingAttributes;
        rentedCarsTable = new JTable();
        tableManager = new AvailableCarsTableManager(rentedCarsTable);
        unrentedCarsTable = new JTable();
        carTableManager = new CarTableManager(unrentedCarsTable, contentPanel, authenticatedUser);

        initializeComponents(rentalHistory);
        setupWindowClosingListener();
    }

    private void initializeComponents(RentalHistory rentalHistory) {
        setTitle("Car Rental System");
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a side panel for navigation
        JTree navigationTree = createNavigationTree(rentalHistory);

        // Add the tree to a scroll pane
        JScrollPane treeScrollPane = new JScrollPane(navigationTree);
        treeScrollPane.setPreferredSize(new Dimension(200, 0));

        // Add components to the main frame
        mainPanel.add(treeScrollPane, BorderLayout.WEST);

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

    private void setupWindowClosingListener() {
        // Add a WindowListener to handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Serialize the car inventory when the window is closing
                carInventory.serializeCarInventory("carInventory.ser");
            }
        });
    }

    private JTree createNavigationTree(RentalHistory rentalHistory) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Admin Main Window");

        DefaultMutableTreeNode carDatabaseNode = new DefaultMutableTreeNode("Car Database");
        DefaultMutableTreeNode rentalHistoryNode = new DefaultMutableTreeNode("Rental History");
        DefaultMutableTreeNode pricesNode = new DefaultMutableTreeNode("Service Prices");
        DefaultMutableTreeNode logoutNode = new DefaultMutableTreeNode("Log Out");

        root.add(carDatabaseNode);
        root.add(rentalHistoryNode);
        root.add(pricesNode);
        root.add(logoutNode);

        JTree navigationTree = new JTree(root);
        navigationTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        navigationTree.setBorder(new EmptyBorder(new Insets(35, 20, 0, 0)));
        // Add action listeners for navigation nodes
        navigationTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) navigationTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                String nodeName = selectedNode.toString();
                switch (nodeName) {
                    case "Car Database":
                        showCarDatabaseView();
                        break;
                    case "Rental History":
                        showRentalHistoryView(rentalHistory);
                        break;
                    case "Service Prices":
                        showPricesPanelView();
                        break;
                    case "Log Out":
                        new UserInterface(carInventory, rentalHistory, pricingAttributes);
                        carInventory.serializeCarInventory("carInventory.ser");
                        dispose();
                        break;
                }
            }
        });

        return navigationTree;
    }

    private void showCarDatabaseView() {
        // Logic to switch to the car database view
        contentPanel.removeAll();

        // Create a panel for car information entry
        JPanel carInfoPanel = createCarInfoPanel();
        contentPanel.add(carInfoPanel, BorderLayout.NORTH);

        // Create a panel for available cars
        JPanel availableCarsPanel = createAvailableCarsPanel();
        contentPanel.add(availableCarsPanel, BorderLayout.CENTER);

        // Create a panel for rented cars
        JPanel rentedCarsPanel = createRentedCarsPanel();
        contentPanel.add(rentedCarsPanel, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createCarInfoPanel() {
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
        addFormField(fieldsPanel, gbc, "Additional Features:", CarEditor.createFeaturesPanel());

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
        Font titleFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleFont(titleFont);

        // Set the titled border to the carInfoPanel
        carInfoPanel.setBorder(titledBorder);

        // Add the car information entry panel to the content panel
        carInfoPanel.add(fieldsPanel, BorderLayout.WEST);

        // Call the method to set up the action listener for the "Add" button
        setupAddButtonListener(addButton, fieldsPanel);

        return carInfoPanel;
    }

    private JPanel createAvailableCarsPanel() {
        // Create a titled border with a custom font and size for the table
        TitledBorder tableTitleBorder = BorderFactory.createTitledBorder("Cars for Rent");
        Font tableTitleFont = new Font("Arial", Font.BOLD, 16);
        tableTitleBorder.setTitleFont(tableTitleFont);

        // Create a panel for the title and table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(tableTitleBorder);

        // Add the carTable to the tablePanel
        JScrollPane tableScrollPane = carTableManager.createTableScrollPane(carInventory);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createRentedCarsPanel() {
        // Create a titled border with a custom font and size for the rented cars table
        TitledBorder rentedCarsTitleBorder = BorderFactory.createTitledBorder("Rented Cars");
        Font rentedCarsTitleFont = new Font("Arial", Font.BOLD, 16);
        rentedCarsTitleBorder.setTitleFont(rentedCarsTitleFont);

        // Create a panel for the title and rented cars table
        JPanel rentedCarsPanel = new JPanel(new BorderLayout());
        rentedCarsPanel.setBorder(rentedCarsTitleBorder);

        // Create the rented cars table and make it non-editable
        JScrollPane rentedCarsScrollPane = createRentedCarsScrollPane();
        rentedCarsPanel.add(rentedCarsScrollPane, BorderLayout.CENTER);

        // Add the button to return the selected car
        JButton returnSelectedCarButton = new JButton("Return Selected Car");
        returnSelectedCarButton.addActionListener(e -> {
            AdminUtil.returnSelectedCar(rentedCarsTable, carInventory, tableManager);
            carTableManager.updateTable(carInventory);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnSelectedCarButton);

        rentedCarsPanel.add(buttonPanel, BorderLayout.SOUTH);

        return rentedCarsPanel;
    }

    private JScrollPane createRentedCarsScrollPane() {
        JScrollPane rentedCarsScrollPane = tableManager
                .createTableScrollPane(carInventory.getUnavailableCarsInventoryToday());
        rentedCarsTable
                .setPreferredScrollableViewportSize(new Dimension(rentedCarsTable.getPreferredSize().width, 200));
        return rentedCarsScrollPane;
    }

    private void setupAddButtonListener(JButton addButton, JPanel fieldsPanel) {
        addButton.addActionListener(e -> AdminUtil.addNewCar(fieldsPanel, carTableManager, carInventory, authenticatedUser));
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