package carrental.panels;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.tables.AvailableCarsTableManager;

import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SearchComponentsPanel extends JPanel {
    private transient AvailableCarsTableManager tableManager;
    private Customer customer;
    private CarInventory carInventory;

    private JTextField manufacturerField;
    private JTextField modelField;
    private JComboBox<Car.ComfortLevel> comfortLevelComboBox;
    private JCheckBox gpsCheckBox;
    private JCheckBox childSeatCheckBox;
    private JCheckBox insuranceCheckBox;
    private JCheckBox leatherInteriorCheckBox;
    private JCheckBox sunroofCheckBox;
    private JCheckBox hybridTechnologyCheckBox;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;

    public SearchComponentsPanel(AvailableCarsTableManager tableManager, Customer customer, CarInventory carInventory) {
        this.tableManager = tableManager;
        this.customer = customer;
        this.carInventory = carInventory;

        initializeComponents();
    }

    private void initializeComponents() {
        // Create text fields, checkboxes, buttons, and date choosers as before
        manufacturerField = new JTextField(10);
        modelField = new JTextField(10);
        comfortLevelComboBox = new JComboBox<>(Car.ComfortLevel.values());
        gpsCheckBox = new JCheckBox("GPS");
        childSeatCheckBox = new JCheckBox("Child Seat");
        insuranceCheckBox = new JCheckBox("Insurance");
        leatherInteriorCheckBox = new JCheckBox("Leather Interior");
        sunroofCheckBox = new JCheckBox("Sunroof");
        hybridTechnologyCheckBox = new JCheckBox("Hybrid Technology");

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

        JPanel dateChooserPanel = createDateChooserPanel();

        JPanel row3Panel = new JPanel();
        row3Panel.add(searchButton);
        row3Panel.add(clearButton);

        setLayout(new GridLayout(4, 1));

        add(row1Panel);
        add(row2Panel);
        add(dateChooserPanel);
        add(row3Panel);

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearAllFields());
    }

    private void clearAllFields() {
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

        tableManager.updateTable(carInventory.getAvailableCarsInventoryToday());
    }

    private void performSearch() {
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
        tableManager.updateTable(searchResults);
    }

    private JPanel createDateChooserPanel() {
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();

        startDateChooser.setMinSelectableDate(new Date());
        startDateChooser.addPropertyChangeListener("date",
                e -> endDateChooser.setMinSelectableDate(startDateChooser.getDate()));

        startDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setDateFormatString("yyyy-MM-dd");

        JPanel dateChooserPanel = new JPanel();
        dateChooserPanel.add(new JLabel("Start Date:"));
        dateChooserPanel.add(startDateChooser);
        dateChooserPanel.add(new JLabel("End Date:"));
        dateChooserPanel.add(endDateChooser);

        return dateChooserPanel;
    }
}
