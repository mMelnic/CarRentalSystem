package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int getNumberOfReservationsForCustomer(String customerUsername) {
        return numberOfReservationsMap.getOrDefault(customerUsername, 0);
    }

    public void decreaseNumberOfReservations(String customerUsername) {
        numberOfReservationsMap.merge(customerUsername, -1, Integer::sum);
        // If the number becomes zero, remove the entry from the map
        if (numberOfReservationsMap.get(customerUsername) <= 0) {
            numberOfReservationsMap.remove(customerUsername);
        }
    }

    // Getters for the maps

    public Map<String, List<RentalRecord>> getCustomerRentalMap() {
        return customerRentalMap;
    }

    public Map<Date, List<RentalRecord>> getDateRentalMap() {
        return dateRentalMap;
    }

    public RentalHistory getRentalHistoryInDateRange(Date startDate, Date endDate) {
        RentalHistory filteredHistory = new RentalHistory();

        // Convert Date to LocalDate
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (Map.Entry<Date, List<RentalRecord>> entry : dateRentalMap.entrySet()) {
            Date transactionDate = entry.getKey();
            LocalDate transactionLocalDate = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Compare only the date part
            if (!transactionLocalDate.isBefore(startLocalDate) && !transactionLocalDate.isAfter(endLocalDate)) {
                for (RentalRecord myRecord : entry.getValue()) {
                    filteredHistory.addRentalRecord(myRecord);
                }
            }
        }

        return filteredHistory;
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
    

    // Save RentalHistory to a file
    public void saveToFile(String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
