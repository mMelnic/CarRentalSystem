package carrental.panels;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import carrental.models.RentalHistory;
import carrental.tables.RentalHistoryTableModel;
import carrental.util.AdjustColumns;

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
        // Minimum date for the start and end date chooser
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
        AdjustColumns.adjustColumnSizes(rentalHistoryTable);

        // Add the components to the main panel
        add(dateSelectionPanel, BorderLayout.NORTH);
        add(new JScrollPane(rentalHistoryTable), BorderLayout.CENTER);

        // Add action listeners to the buttons
        searchButton.addActionListener(e -> {
            Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();

                if (startDate != null && endDate != null) {
                    // Filter rental history based on date range
                    RentalHistory filteredHistory = rentalHistory.getRentalHistoryInDateRange(startDate, endDate);
                    updateRentalHistoryTable(filteredHistory);
                    AdjustColumns.adjustColumnSizes(rentalHistoryTable);
                }
        });

        showAllButton.addActionListener(e -> updateRentalHistoryTable(rentalHistory));
    }

    private void updateRentalHistoryTable(RentalHistory updatedRentalHistory) {
        // Update the table model with the new rental history
        rentalHistoryTable.setModel(new RentalHistoryTableModel(updatedRentalHistory));
        AdjustColumns.adjustColumnSizes(rentalHistoryTable);
    }
}

