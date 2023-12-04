package carrental.tables;

import javax.swing.*;

import carrental.models.Administrator;
import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.util.AdjustColumns;
import carrental.util.CarEditor;

public class CarTableManager {
    private JTable carTable;
    private JPanel contentPanel;
    Administrator admin;

    // Updated constructor to accept JTable
    public CarTableManager(JTable carTable, JPanel contentPanel, Administrator admin) {
        this.carTable = carTable;
        this.contentPanel = contentPanel;
        this.admin = admin;
    }

    public JScrollPane createTableScrollPane(CarInventory carInventory) {
        // Create a table model for the cars
        CarTableModel tableModel = createTableModel(carInventory.getAvailableCarsInventoryToday());

        // Set the JTable with the table model
        carTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(carTable);

        // Set the selection mode to allow single-row selection
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setupTableSelectionListener(carInventory);

        // Add the table to a scroll pane for better visibility
        return new JScrollPane(carTable);
    }

    public void updateTable(CarInventory carInventory) {
        CarTableModel tableModel = createTableModel(carInventory.getAvailableCarsInventoryToday());
        carTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(carTable);
        setupTableSelectionListener(carInventory);
        carTable.repaint();
        carTable.revalidate();
    }

    private CarTableModel createTableModel(CarInventory inventory) {
        return new CarTableModel(inventory);
    }

    private void setupTableSelectionListener(CarInventory carInventory) {
        carTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = carTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Display car details for editing
                    Car car = carInventory.getAvailableCarsInventoryToday().getCarList().get(selectedRow);
                    if (CarEditor.displayCarDetailsForEditing(contentPanel, car, carInventory, this, admin)) {
                        updateTable(carInventory);
                    }
                }
            }
        });
    }
}

