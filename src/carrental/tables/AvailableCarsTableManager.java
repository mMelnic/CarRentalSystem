package carrental.tables;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import carrental.models.CarInventory;
import carrental.util.AdjustColumns;

public class AvailableCarsTableManager {
    private JTable carsTable;

    // Updated constructor to accept JTable
    public AvailableCarsTableManager(JTable unrentedCarsTable) {
        this.carsTable = unrentedCarsTable;
    }

    public JScrollPane createTableScrollPane(CarInventory carInventory) {
        // Create a table model for the unrented cars
        CarTableModel tableModel = createTableModelForUnrentedCars(carInventory);

        // Set the JTable with the table model
        carsTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(carsTable);

        // Set the selection mode to allow single-row selection
        carsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table to a scroll pane for better visibility
        return new JScrollPane(carsTable);
    }

    public void updateTable(CarInventory newInventory) {
        CarTableModel tableModel = createTableModelForUnrentedCars(newInventory);
        carsTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(carsTable);
        carsTable.repaint();
        carsTable.revalidate();
    }

    private CarTableModel createTableModelForUnrentedCars(CarInventory inventory) {
        return new CarTableModel(inventory);
    }
}
