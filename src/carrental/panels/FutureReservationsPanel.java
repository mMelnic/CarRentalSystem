package carrental.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import carrental.models.Car;
import carrental.models.CarInventory;
import carrental.models.Customer;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.models.RentalInterval;
import carrental.models.RentalRecord;
import carrental.tables.FutureReservationsTableModel;
import carrental.util.CustomerModificationListener;
import carrental.util.PriceUpdateListener;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FutureReservationsPanel extends JPanel {
    private JTable futureReservationsTable;
    private JButton modifyButton;
    private JButton cancelButton;
    private Customer customer;
    private transient CustomerModificationListener customerModificationListener;
    private transient PriceUpdateListener priceUpdateListener;

    public FutureReservationsPanel(List<RentalRecord> rentalRecords, CarInventory carInventory, RentalHistory rentalHistory, Customer customer, PricingAttributes charges) {
        this.customer = customer;
        setPreferredSize(new Dimension(300, 200));
        initializeTable(rentalRecords);

        // Initialize buttons
        modifyButton = new JButton("Modify");
        cancelButton = new JButton("Cancel");

        // Add action listeners to the buttons
        modifyButton.addActionListener(e -> modifyReservation(carInventory, customer, charges, rentalRecords));

        cancelButton.addActionListener(e -> cancelReservation(carInventory, rentalHistory, rentalRecords));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(modifyButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(futureReservationsTable);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Future Reservations");
        Font futureReservationsTitleFont = new Font("Arial", Font.BOLD, 16); // Customize the font and size
        titledBorder.setTitleFont(futureReservationsTitleFont);

        scrollPane.setBorder(titledBorder);

        // Add the scroll pane to the panel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public Customer getModifiedCustomer() {
        return customer;
    }
    
    public void setCustomerModificationListener(CustomerModificationListener listener) {
        this.customerModificationListener = listener;
    }

    public void setPriceUpdateListener(PriceUpdateListener priceUpdateListener) {
        this.priceUpdateListener = priceUpdateListener;
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

    private void updateFutureReservationsTable(List<RentalRecord> reservations) {
        // Update the table model with the new rental history
        futureReservationsTable.setModel(new FutureReservationsTableModel(reservations));
    }

    private List<RentalRecord> filterFutureReservations(List<RentalRecord> rentalRecords) {
        if (rentalRecords == null) {
            return Collections.emptyList();
        }

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

    private void modifyReservation(CarInventory carInventory, Customer customer, PricingAttributes charges,
            List<RentalRecord> rentalRecords) {
        int selectedRow = futureReservationsTable.getSelectedRow();
        int registrationInfoColumnIndex = 2;

        if (selectedRow >= 0) {
            RentalRecord selectedRecord = getSelectedRentalRecord(selectedRow);
            String selectedRegistrationInfo = getSelectedRegistrationInfo(selectedRow, registrationInfoColumnIndex);

            if (canModifyReservation(selectedRecord, carInventory, selectedRegistrationInfo)) {
                Date[] newDateRange = showDateSelectionDialog(this);

                if (newDateRange.length != 0) {
                    modifyReservationWithNewDates(selectedRecord, selectedRegistrationInfo, carInventory, customer,
                            charges, newDateRange, rentalRecords);
                }
            }
        } else {
            showNoReservationSelectedMessage();
        }
    }

    private RentalRecord getSelectedRentalRecord(int selectedRow) {
        return ((FutureReservationsTableModel) futureReservationsTable.getModel()).getRentalRecord(selectedRow);
    }

    private String getSelectedRegistrationInfo(int selectedRow, int registrationInfoColumnIndex) {
        return (String) futureReservationsTable.getValueAt(selectedRow, registrationInfoColumnIndex);
    }

    private boolean canModifyReservation(RentalRecord selectedRecord, CarInventory carInventory, String selectedRegistrationInfo) {
        if (selectedRecord != null && selectedRecord.isModificationAllowed()) {
            Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);
            if (selectedCar != null) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Selected car is no longer available for modification.",
                        "Car Unavailable", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Modification not allowed for the selected reservation.",
                    "Modification Not Allowed", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void modifyReservationWithNewDates(RentalRecord selectedRecord, String selectedRegistrationInfo,
            CarInventory carInventory, Customer customer, PricingAttributes charges,
            Date[] newDateRange, List<RentalRecord> rentalRecords) {
        Date newStartDate = newDateRange[0];
        Date newEndDate = newDateRange[1];
        Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);
        Car rentedCarInTable = selectedRecord.getRentedCar();

        if (selectedCar.modifyReservation(selectedRecord.getRentId(), newStartDate, newEndDate)) {
            updateReservationAndTable(selectedRecord, rentedCarInTable, newStartDate, newEndDate, customer, charges,
                    rentalRecords);
        } else {
            showModificationFailedMessage();
        }
    }

    private void updateReservationAndTable(RentalRecord selectedRecord, Car rentedCarInTable, Date newStartDate,
            Date newEndDate,
            Customer customer, PricingAttributes charges, List<RentalRecord> rentalRecords) {
        rentedCarInTable.modifyReservation(selectedRecord.getRentId(), newStartDate, newEndDate);
        double newPrice = customer.calculateRentalPrice(rentedCarInTable, newStartDate, newEndDate, charges);
        selectedRecord.setTotalPrice(newPrice);

        if (priceUpdateListener != null) {
            priceUpdateListener.onPriceUpdate();
        }

        updateFutureReservationsTable(filterFutureReservations(rentalRecords));

        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Reservation modified successfully",
                "Success", JOptionPane.INFORMATION_MESSAGE));
    }

    private void showModificationFailedMessage() {
        JOptionPane.showMessageDialog(this, "Modification failed. The selected dates are not available.",
                "Modification Failed", JOptionPane.WARNING_MESSAGE);
    }

    private void showNoReservationSelectedMessage() {
        JOptionPane.showMessageDialog(this, "Please select a reservation to modify.", "No Reservation Selected",
                JOptionPane.WARNING_MESSAGE);
    }

    private void cancelReservation(CarInventory carInventory, RentalHistory rentalHistory,
            List<RentalRecord> rentalRecords) {
        int selectedRow = futureReservationsTable.getSelectedRow();
        int registrationInfoColumnIndex = 2;

        if (selectedRow >= 0) {
            FutureReservationsTableModel model = (FutureReservationsTableModel) futureReservationsTable.getModel();
            RentalRecord selectedRecord = model.getRentalRecord(selectedRow);
            String selectedRegistrationInfo = (String) futureReservationsTable.getValueAt(selectedRow,
                        registrationInfoColumnIndex);

            if (canCancelReservation(selectedRecord, carInventory, selectedRegistrationInfo)) {
                Car rentedCar = carInventory.getCarMap().get(selectedRegistrationInfo);
                UUID rentId = selectedRecord.getRentId();

                removeRentalIntervalAndCancelReservation(rentedCar, selectedRecord, rentId, rentalHistory);
                updateFutureReservationsTable(filterFutureReservations(rentalRecords));

                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showNoReservationSelectedMessage();
        }
    }

    private boolean canCancelReservation(RentalRecord selectedRecord, CarInventory carInventory,
            String selectedRegistrationInfo) {
        if (selectedRecord != null && selectedRecord.isModificationAllowed()) {
            Car selectedCar = carInventory.getCarMap().get(selectedRegistrationInfo);
            if (selectedCar != null) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Selected car is no longer available for cancellation.",
                        "Car Unavailable", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cancellation not allowed for the selected reservation.",
                    "Cancellation Not Allowed", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void removeRentalIntervalAndCancelReservation(Car rentedCar, RentalRecord selectedRecord, UUID rentId,
            RentalHistory rentalHistory) {
        // Remove the rental interval from the rentedCar
        rentedCar.removeRentalIntervalWithId(rentId);
        selectedRecord.getRentedCar().removeRentalIntervalWithId(rentId);

        // Set isCancelled attribute to true in the RentalRecord
        selectedRecord.setCancelled(true);

        rentalHistory.decreaseNumberOfReservations(customer.getUsername());
        this.customer = customer.checkAndDowngrade(rentalHistory);

        if (customerModificationListener != null) {
            customerModificationListener.onCustomerModified(customer);
        }
    }

    public Date[] showDateSelectionDialog(Component parentComponent) {
        JDateChooser startDateChooser = createDatePicker("Select Start Date:");
        JDateChooser endDateChooser = createDatePicker("Select End Date:");
        startDateChooser.addPropertyChangeListener("date",
                e -> endDateChooser.setMinSelectableDate(startDateChooser.getDate()));

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

        return new Date[0]; // User canceled or did not select valid dates
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

