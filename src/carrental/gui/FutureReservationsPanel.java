package carrental.gui;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.RentalHistory;
import carrental.models.RentalInterval;
import carrental.models.RentalRecord;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FutureReservationsPanel extends JPanel {
    private JTable futureReservationsTable;

    private JButton modifyButton;
    private JButton cancelButton;

    public FutureReservationsPanel(List<RentalRecord> rentalRecords, RentalHistory rentalHistory, CarInventory carInventory) {
        initializeTable(rentalRecords);

        // Initialize buttons
        modifyButton = new JButton("Modify");
        cancelButton = new JButton("Cancel");

        // Add action listeners to the buttons
        modifyButton.addActionListener(e -> modifyReservation(carInventory, rentalHistory));

        cancelButton.addActionListener(e -> cancelReservation(carInventory, rentalHistory));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(modifyButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(futureReservationsTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeTable(List<RentalRecord> rentalRecords) {
        // Filter records based on start date being the current date or any future date
        List<RentalRecord> futureReservations = filterFutureReservations(rentalRecords);

        // Create table model
        FutureReservationsTableModel tableModel = new FutureReservationsTableModel(futureReservations);

        // Create JTable with the populated model
        futureReservationsTable = new JTable(tableModel);
        futureReservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private List<RentalRecord> filterFutureReservations(List<RentalRecord> rentalRecords) {
        // TODO if no rentalRecords empty table
        return rentalRecords.stream()
                .filter(myRecord -> {
                    Date startDate = myRecord.getRentedCar().getRentalIntervals().stream()
                            .filter(interval -> interval.getRentId().equals(myRecord.getRentId()))
                            .findFirst()
                            .map(RentalInterval::getStartDate)
                            .orElse(null);

                    // Convert Date to LocalDate
                    LocalDate localStartDate = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

                    // Compare only the day part
                    return localStartDate != null && localStartDate.isAfter(LocalDate.now());
                })
                .toList();
    }

    private void modifyReservation(CarInventory carInventory, RentalHistory rentalHistory) {
        //TODO recalculate price when modifying the dates
        int selectedRow = futureReservationsTable.getSelectedRow();

        if (selectedRow >= 0) {
            // Get the selected rental record from the table model
            RentalRecord selectedRecord = ((FutureReservationsTableModel) futureReservationsTable.getModel())
                    .getRentalRecord(selectedRow);

            // Check if modification is allowed
            if (selectedRecord != null && selectedRecord.isModificationAllowed()) {
                // Display a dialog to get new start and end dates
                Date[] newDateRange = showDateSelectionDialog(this);

                if (newDateRange != null) {
                    Date newStartDate = newDateRange[0];
                    Date newEndDate = newDateRange[1];

                    // Modify the reservation
                    if (selectedRecord.getRentedCar().modifyReservation(selectedRecord.getRentId(), newStartDate, newEndDate)) {
                        // Update the table with the new reservation data
                        ((FutureReservationsTableModel) futureReservationsTable.getModel()).fireTableDataChanged();
                        JOptionPane.showMessageDialog(this, "Reservation modified successfully", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        carInventory.serializeCarInventory("carInventory.ser");
                        rentalHistory.saveToFile("rental_history.ser");
                    } else {
                        JOptionPane.showMessageDialog(this, "Modification failed. Please check the selected dates.",
                                "Modification Failed", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Modification not allowed for the selected reservation.",
                        "Modification Not Allowed", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to modify.", "No Reservation Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Implement the cancelReservation method (you can adapt this based on your
    // requirements)
    private void cancelReservation(CarInventory carInventory, RentalHistory rentalHistory) {
        //TODO decrease the number of reservations/rental records and upgrade progress bar, downgrade tier if necessary
        int selectedRow = futureReservationsTable.getSelectedRow();

        if (selectedRow >= 0) {
            FutureReservationsTableModel model = (FutureReservationsTableModel) futureReservationsTable.getModel();
            RentalRecord selectedRecord = model.getRentalRecord(selectedRow);

            if (selectedRecord != null && selectedRecord.isModificationAllowed()) {
                Car rentedCar = selectedRecord.getRentedCar();
                UUID rentId = selectedRecord.getRentId();

                // Remove the rental interval from the rentedCar
                rentedCar.removeRentalInterval(rentId);

                // Set isCancelled attribute to true in the RentalRecord
                selectedRecord.setCancelled(true);
                carInventory.serializeCarInventory("carInventory.ser");
                rentalHistory.saveToFile("rental_history.ser");

                // Repaint the table to reflect the changes
                model.fireTableDataChanged();
            }
        }
    }   


    public Date[] showDateSelectionDialog(Component parentComponent) {
        JDateChooser startDateChooser = createDatePicker("Select Start Date:");
        JDateChooser endDateChooser = createDatePicker("Select End Date:");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(startDateChooser);
        panel.add(endDateChooser);

        int result = JOptionPane.showConfirmDialog(parentComponent, panel, "Select Dates", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date startDate = startDateChooser.getDate();
            Date endDate = endDateChooser.getDate();

            if (startDate != null && endDate != null) {
                return new Date[]{startDate, endDate};
            }
        }

        return null; // User canceled or did not select valid dates
    }

    private JDateChooser createDatePicker(String label) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setMinSelectableDate(new Date());  // Set minimum date to today

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(dateChooser);

        return dateChooser;
    }
}

