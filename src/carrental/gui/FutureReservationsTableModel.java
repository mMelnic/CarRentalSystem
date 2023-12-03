package carrental.gui;

import javax.swing.table.AbstractTableModel;

import carrental.models.Car;
import carrental.models.RentalInterval;
import carrental.models.RentalRecord;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class FutureReservationsTableModel extends AbstractTableModel {
    private final List<RentalRecord> futureReservations;
    private final String[] columnNames = {"Manufacturer", "Car Model", "Registration Info", "Start Date", "End Date", "Total Price"};

    public FutureReservationsTableModel(List<RentalRecord> futureReservations) {
        this.futureReservations = futureReservations;
    }

    @Override
    public int getRowCount() {
        return futureReservations == null ? 0 : futureReservations.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (futureReservations == null || futureReservations.isEmpty()) {
            return null;
        }
        
        RentalRecord myRecord = futureReservations.get(rowIndex);
        Car rentedCar = myRecord.getRentedCar();
        RentalInterval rentalInterval = rentedCar.getRentalIntervalById(myRecord.getRentId());

        if (rentalInterval != null) {
            LocalDate startDate = rentalInterval.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            switch (columnIndex) {
                case 0:
                    return rentedCar.getManufacturer();
                case 1:
                    return rentedCar.getModel();
                case 2:
                    return rentedCar.getRegistrationInfo();
                case 3:
                    return startDate;
                case 4:
                    return dateFormat.format(rentalInterval.getEndDate());
                case 5:
                    return myRecord.getTotalPrice();
                default:
                    return null;
            }
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public RentalRecord getRentalRecord(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < futureReservations.size()) {
            return futureReservations.get(rowIndex);
        }
        return null;
    }
}
