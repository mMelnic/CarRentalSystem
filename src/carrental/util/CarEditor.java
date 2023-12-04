package carrental.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import carrental.models.Administrator;
import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.Car.AdditionalFeatures;
import carrental.models.Car.ComfortLevel;
import carrental.tables.CarTableManager;

public class CarEditor {
    private CarEditor() {}

    public static boolean displayCarDetailsForEditing(Component parentComponent, Car car, CarInventory carInventory, CarTableManager tableManager, Administrator admin) {
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
                checkBox.setSelected(
                        car.getAdditionalFeatures().contains(AdditionalFeatures.valueOf(checkBox.getText())));
            }
        }

        JPanel editPanel = createEditPanel(manufacturerField, modelField, registrationInfoField, colorField,
                yearOfProductionField, priceField, comfortLevelComboBox, featuresPanel);

        int windowHeight = 400;
        int windowWidth = 700;
        editPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteCar(parentComponent, car, carInventory, tableManager, admin));

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deletePanel.add(deleteButton);

        editPanel.add(deletePanel);

        int result = JOptionPane.showConfirmDialog(parentComponent, editPanel, "Edit Car Details",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (isAnyRequiredFieldEmpty(manufacturerField, modelField, registrationInfoField, colorField,
                    yearOfProductionField, priceField)) {
                showWarningMessage(parentComponent, "Please fill in all required fields.", "Missing Information");
                return false;
            }

            updateCarDetails(car, manufacturerField, modelField, registrationInfoField, colorField,
                    yearOfProductionField, priceField, comfortLevelComboBox, featuresPanel);

            return true;
        }

        return false;
    }

    private static JPanel createEditPanel(JTextField manufacturerField, JTextField modelField,
                                          JTextField registrationInfoField, JTextField colorField,
                                          JTextField yearOfProductionField, JTextField priceField,
                                          JComboBox<ComfortLevel> comfortLevelComboBox, JPanel featuresPanel) {
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
        return editPanel;
    }

    private static void updateCarDetails(Car car, JTextField manufacturerField, JTextField modelField,
                                         JTextField registrationInfoField, JTextField colorField,
                                         JTextField yearOfProductionField, JTextField priceField,
                                         JComboBox<ComfortLevel> comfortLevelComboBox, JPanel featuresPanel) {
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
    }

    public static JPanel createFeaturesPanel() {
        JPanel featuresPanel = new JPanel(new GridLayout(2, 3));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.GPS.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.CHILD_SEAT.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.INSURANCE.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.LEATHER_INTERIOR.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.SUNROOF.name()));
        featuresPanel.add(new JCheckBox(AdditionalFeatures.HYBRID_TECHNOLOGY.name()));
        return featuresPanel;
    }

    private static void deleteCar(Component parentComponent, Car car, CarInventory carInventory, CarTableManager tableManager, Administrator admin) {
        if (car.canCarBeDeleted()) {
            int confirm = JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete this car?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Implement the logic to remove the car from the inventory
                admin.removeCar(car.getRegistrationInfo(), carInventory);
                // Update the displayed table
                tableManager.updateTable(carInventory);
                JOptionPane.showMessageDialog(parentComponent, "Car deleted successfully.", "Deletion Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentComponent,
                    "The car cannot be deleted because it has active rental intervals.", "Cannot Delete Car",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static boolean isAnyRequiredFieldEmpty(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static void showWarningMessage(Component parentComponent, String message, String title) {
        JOptionPane.showMessageDialog(parentComponent, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
