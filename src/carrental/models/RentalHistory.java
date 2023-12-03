package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carrental.util.Serialization;

public class RentalHistory implements Serializable {
    private Map<String, List<RentalRecord>> customerRentalMap;
    private Map<Date, List<RentalRecord>> dateRentalMap;
    private Map<String, Integer> numberOfReservationsMap;
    private static final long serialVersionUID = 9077929968150495859L;

    public RentalHistory() {
        this.customerRentalMap = new HashMap<>();
        this.dateRentalMap = new HashMap<>();
        this.numberOfReservationsMap = new HashMap<>();
    }

    public Map<String, List<RentalRecord>> getCustomerRentalMap() {
        return customerRentalMap;
    }

    public Map<Date, List<RentalRecord>> getDateRentalMap() {
        return dateRentalMap;
    }

    public int getNumberOfReservationsForCustomer(String customerUsername) {
        return numberOfReservationsMap.getOrDefault(customerUsername, 0);
    }

    public void addRentalRecord(RentalRecord rentalRecord) {
        // Add to customer-based map using customer's unique identifier (e.g., username)
        String customerIdentifier = rentalRecord.getRentingCustomer().getUsername();
        customerRentalMap.computeIfAbsent(customerIdentifier, k -> new ArrayList<>()).add(rentalRecord);

        // Add to date-based map
        Date transactionDate = rentalRecord.getTransactionDate();
        dateRentalMap.computeIfAbsent(transactionDate, k -> new ArrayList<>()).add(rentalRecord);

        // Increment the number of reservations for the customer
        numberOfReservationsMap.merge(customerIdentifier, 1, Integer::sum);
    }

    public void decreaseNumberOfReservations(String customerUsername) {
        numberOfReservationsMap.merge(customerUsername, -1, Integer::sum);
        // If the number becomes zero, remove the entry from the map
        if (numberOfReservationsMap.get(customerUsername) <= 0) {
            numberOfReservationsMap.remove(customerUsername);
        }
    }

    public RentalHistory getRentalHistoryForCustomer(String customerUsername) {
        RentalHistory filteredHistory = new RentalHistory();
    
        if (customerRentalMap != null) {
            List<RentalRecord> records = customerRentalMap.getOrDefault(customerUsername, new ArrayList<>());
    
            for (RentalRecord myRecord : records) {
                filteredHistory.addRentalRecord(myRecord);
            }
        }
    
        return filteredHistory;
    }

    public RentalHistory getRentalHistoryInDateRange(Date startDate, Date endDate) {
        RentalHistory filteredHistory = new RentalHistory();

        // Convert Date to LocalDate
        // todo have a utility class with a method to convert date to local date
        LocalDate startLocalDate = dateToLocalDate(startDate);
        LocalDate endLocalDate = dateToLocalDate(endDate);

        for (Map.Entry<Date, List<RentalRecord>> entry : dateRentalMap.entrySet()) {
            Date transactionDate = entry.getKey();
            LocalDate transactionLocalDate = dateToLocalDate(transactionDate);

            // Compare only the date part
            if (!transactionLocalDate.isBefore(startLocalDate) && !transactionLocalDate.isAfter(endLocalDate)) {
                for (RentalRecord myRecord : entry.getValue()) {
                    filteredHistory.addRentalRecord(myRecord);
                }
            }
        }

        return filteredHistory;
    }

    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
    }

    public void updateOriginalHistory(RentalHistory updatedHistory) {
        for (Map.Entry<String, List<RentalRecord>> entry : updatedHistory.customerRentalMap.entrySet()) {
            int countReservations = 0;
            String customerUsername = entry.getKey();
            List<RentalRecord> updatedRecords = entry.getValue();

            // Remove existing records for the customer
            customerRentalMap.remove(customerUsername);

            // Add updated records to the original map
            customerRentalMap.put(customerUsername, new ArrayList<>(updatedRecords));

            for (RentalRecord rentalRecord : updatedRecords) {
                if (!rentalRecord.getCancelled()) {
                    countReservations++;
                }
            }

            // Update the number of reservations
            numberOfReservationsMap.put(customerUsername, countReservations);
        }

        // Update date-based map
        dateRentalMap.clear();
        for (Map.Entry<String, List<RentalRecord>> entry : customerRentalMap.entrySet()) {
            for (RentalRecord myRecord : entry.getValue()) {
                Date transactionDate = myRecord.getTransactionDate();
                dateRentalMap.computeIfAbsent(transactionDate, k -> new ArrayList<>()).add(myRecord);
            }
        }
    }
    
    // Save RentalHistory to a file when log out and when close customer main window
    public void saveRentalHistoryToFile(String filePath) {
        Serialization.serializeObject(this, filePath);
    }

    public static RentalHistory loadFromFile(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return (RentalHistory) inputStream.readObject();
        } catch (FileNotFoundException e) {
            // File not found, create the file and return an empty RentalHistory
            System.out.println("File not found. Creating a new RentalHistory.");
            return new RentalHistory();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
