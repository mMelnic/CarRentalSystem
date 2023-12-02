package carrental.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.toedter.calendar.JDateChooser;

import carrental.models.RentalHistory;

import java.awt.*;
import java.util.Date;

public class RentalHistorySearchPanel extends JPanel {

    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JButton searchButton;
    private JButton showAllButton;
    private JTable rentalHistoryTable;

    public RentalHistorySearchPanel(RentalHistory rentalHistory) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 20, 30, 20));

        // First Panel: Date Selection and Buttons
        JPanel dateSelectionPanel = new JPanel();
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy/MM/dd");
        endDateChooser.setDateFormatString("yyyy/MM/dd");
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All");
        // Set a minimum date for the start and end date chooser
        startDateChooser.addPropertyChangeListener("date", e ->
            endDateChooser.setMinSelectableDate(startDateChooser.getDate()));

        dateSelectionPanel.add(new JLabel("Start Date:"));
        dateSelectionPanel.add(startDateChooser);
        dateSelectionPanel.add(new JLabel("End Date:"));
        dateSelectionPanel.add(endDateChooser);
        dateSelectionPanel.add(searchButton);
        dateSelectionPanel.add(showAllButton);
        dateSelectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Second Panel: Rental History Table
        RentalHistoryTableModel tableModel = new RentalHistoryTableModel(rentalHistory);
        rentalHistoryTable = new JTable(tableModel);
        adjustColumnSizes(rentalHistoryTable);

        // Add the components to the main panel
        add(dateSelectionPanel, BorderLayout.NORTH);
        add(new JScrollPane(rentalHistoryTable), BorderLayout.CENTER);

        // Add action listeners to buttons
        searchButton.addActionListener(e -> {
            Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();

                if (startDate != null && endDate != null) {
                    // Filter rental history based on date range
                    RentalHistory filteredHistory = rentalHistory.getRentalHistoryInDateRange(startDate, endDate);
                    updateRentalHistoryTable(filteredHistory);
                }
        });

        showAllButton.addActionListener(e -> updateRentalHistoryTable(rentalHistory));
    }

    // Placeholder method for updating the rental history table
    private void updateRentalHistoryTable(RentalHistory updatedRentalHistory) {
        // Update the table model with the new rental history
        rentalHistoryTable.setModel(new RentalHistoryTableModel(updatedRentalHistory));
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

