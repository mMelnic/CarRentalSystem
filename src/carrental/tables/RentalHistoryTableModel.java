package carrental.tables;

import javax.swing.table.AbstractTableModel;

import carrental.models.Car;
import carrental.models.RentalHistory;
import carrental.models.RentalRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalHistoryTableModel extends AbstractTableModel {

    private RentalHistory rentalHistory;
    private String[] columnNames = {"Username", "Customer Name", "Email", "Registration Number", "Car Details", "Total Price", "Rental Date", "Cancelled"};

    public RentalHistoryTableModel(RentalHistory rentalHistory) {
        this.rentalHistory = rentalHistory;
    }

    @Override
    public int getRowCount() {
        int totalRecords = 0;
        for (List<RentalRecord> records : rentalHistory.getCustomerRentalMap().values()) {
            totalRecords += records.size();
        }
        return totalRecords;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<RentalRecord> allRecords = new ArrayList<>();
        for (List<RentalRecord> records : rentalHistory.getCustomerRentalMap().values()) {
            allRecords.addAll(records);
        }

        RentalRecord rentalRecord = allRecords.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rentalRecord.getRentingCustomer().getUsername();
            case 1:
                return rentalRecord.getRentingCustomer().getFullName();
            case 2:
                return rentalRecord.getRentingCustomer().getEmail();
            case 3:
                return rentalRecord.getRentedCar().getRegistrationInfo();
            case 4:
                return getCarDetails(rentalRecord.getRentedCar());
            case 5:
                return rentalRecord.getTotalPrice();
            case 6:
                return formatDate(rentalRecord.getTransactionDate());
            case 7:
                return rentalRecord.getCancelled() ? "Yes" : "No";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    private String getCarDetails(Car car) {
        return car.getManufacturer() + " " + car.getModel() + ", Base Price: " + car.getPrice();
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
