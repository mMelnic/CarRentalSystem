package carrental.util;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import carrental.models.Administrator;
import carrental.models.Car;
import carrental.models.Car.AdditionalFeatures;
import carrental.models.Car.ComfortLevel;
import carrental.models.CarInventory;
import carrental.tables.AvailableCarsTableManager;
import carrental.tables.CarTableManager;

public class AdminUtil {

    private AdminUtil() {}

    public static void returnSelectedCar(JTable rentedCarsTable, CarInventory carInventory, AvailableCarsTableManager tableManager) {
        int selectedRow = rentedCarsTable.getSelectedRow();
        int registrationInfoColumnIndex = 2;

        if (selectedRow != -1) {
            // Get the selected car from the table model
            String selectedRegistrationInfo = (String) rentedCarsTable.getValueAt(selectedRow, registrationInfoColumnIndex);
            Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);

            // Call the returnCar method in your CarInventory to update the rental status
            carInventory.returnCarToday(selectedCar);

            tableManager.updateTable(carInventory.getUnavailableCarsInventoryToday());
        } else {
            JOptionPane.showMessageDialog(null, "Please select a car to return.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void addNewCar(JPanel fieldsPanel, CarTableManager carTableManager, CarInventory carInventory,
                                 Administrator authenticatedUser) {
        // Extract data from fields
        JTextField manufacturerField = getTextField(fieldsPanel, 1);
        JTextField modelField = getTextField(fieldsPanel, 3);
        JTextField registrationInfoField = getTextField(fieldsPanel, 5);
        JTextField colorField = getTextField(fieldsPanel, 7);

        JTextField yearField = getTextField(fieldsPanel, 9);
        JTextField priceField = getTextField(fieldsPanel, 11);

        ComfortLevel comfortLevel = getSelectedComfortLevel(fieldsPanel, 13);

        // Extract selected additional features
        Set<AdditionalFeatures> additionalFeatures = getSelectedFeatures(fieldsPanel, 15);

        // Validate the required fields
        if (CarEditor.isAnyRequiredFieldEmpty(manufacturerField, modelField, registrationInfoField, colorField,
                yearField, priceField)) {
            // Show a warning message dialog if any of the required fields is not filled
            JOptionPane.showMessageDialog(fieldsPanel, "Please fill in all required fields.", "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int yearOfProduction = Integer.parseInt(yearField.getText());
            double price = Double.parseDouble(priceField.getText());

            // Create a new car
            Car newCar = new Car(manufacturerField.getText(), modelField.getText(),
                    registrationInfoField.getText(), colorField.getText(), yearOfProduction, price,
                    comfortLevel, additionalFeatures);

            // Add the new car to the CarInventory
            authenticatedUser.addCar(newCar, carInventory);

            // Update the displayed table
            carTableManager.updateTable(carInventory);
        } catch (NumberFormatException e) {
            // Handle the case where the entered year or price is not a valid number
            JOptionPane.showMessageDialog(fieldsPanel, "Please enter valid numeric values for year and price.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static JTextField getTextField(JPanel fieldsPanel, int index) {
        return (JTextField) fieldsPanel.getComponent(index);
    }

    private static ComfortLevel getSelectedComfortLevel(JPanel fieldsPanel, int index) {
        return (ComfortLevel) ((JComboBox<?>) fieldsPanel.getComponent(index)).getSelectedItem();
    }

    private static Set<AdditionalFeatures> getSelectedFeatures(JPanel fieldsPanel, int index) {
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        for (Component component : ((JPanel) fieldsPanel.getComponent(index)).getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    additionalFeatures.add(AdditionalFeatures.valueOf(checkBox.getText()));
                }
            }
        }
        return additionalFeatures;
    }
}
