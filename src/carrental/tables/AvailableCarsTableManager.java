package carrental.tables;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import carrental.models.CarInventory;

public class AvailableCarsTableManager {
    private JTable unrentedCarsTable;

    // Updated constructor to accept JTable
    public AvailableCarsTableManager(JTable unrentedCarsTable) {
        this.unrentedCarsTable = unrentedCarsTable;
    }

    public JScrollPane createTableScrollPane(CarInventory carInventory) {
        // Create a table model for the unrented cars
        CarTableModel tableModel = createTableModelForUnrentedCars(carInventory.getAvailableCarsInventoryToday());

        // Set the JTable with the table model
        unrentedCarsTable.setModel(tableModel);
        adjustColumnSizes(unrentedCarsTable);

        // Set the selection mode to allow single-row selection
        unrentedCarsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table to a scroll pane for better visibility
        return new JScrollPane(unrentedCarsTable);
    }

    public void updateTableWithSearchResults(CarInventory searchResults) {
        CarTableModel tableModel = createTableModelForUnrentedCars(searchResults);
        unrentedCarsTable.setModel(tableModel);
        adjustColumnSizes(unrentedCarsTable);
        unrentedCarsTable.repaint();
        unrentedCarsTable.revalidate();
    }

    private CarTableModel createTableModelForUnrentedCars(CarInventory inventory) {
        return new CarTableModel(inventory);
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
}
