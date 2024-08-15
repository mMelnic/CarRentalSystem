package carrental.util;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class AdjustColumns {
    private AdjustColumns() {}
    
    public static void adjustColumnSizes(JTable table) {
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
