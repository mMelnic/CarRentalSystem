package carrental.tables;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import carrental.models.CarInventory;
import carrental.util.AdjustColumns;

public class AvailableCarsTableManager {
    private JTable unrentedCarsTable;

    // Updated constructor to accept JTable
    public AvailableCarsTableManager(JTable unrentedCarsTable) {
        this.unrentedCarsTable = unrentedCarsTable;
    }

    public JScrollPane createTableScrollPane(CarInventory carInventory) {
        // Create a table model for the unrented cars
        CarTableModel tableModel = createTableModelForUnrentedCars(carInventory);

        // Set the JTable with the table model
        unrentedCarsTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(unrentedCarsTable);

        // Set the selection mode to allow single-row selection
        unrentedCarsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table to a scroll pane for better visibility
        return new JScrollPane(unrentedCarsTable);
    }

    public void updateTable(CarInventory newInventory) {
        CarTableModel tableModel = createTableModelForUnrentedCars(newInventory);
        unrentedCarsTable.setModel(tableModel);
        AdjustColumns.adjustColumnSizes(unrentedCarsTable);
        unrentedCarsTable.repaint();
        unrentedCarsTable.revalidate();
    }

    private CarTableModel createTableModelForUnrentedCars(CarInventory inventory) {
        return new CarTableModel(inventory);
    }
}
