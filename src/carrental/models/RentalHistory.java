package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalHistory implements Serializable {
    private Map<String, List<RentalRecord>> customerRentalMap;
    private Map<Date, List<RentalRecord>> dateRentalMap;

    public RentalHistory() {
        this.customerRentalMap = new HashMap<>();
        this.dateRentalMap = new HashMap<>();
    }

    public void addRentalRecord(RentalRecord rentalRecord) {
        // Add to customer-based map using customer's unique identifier (e.g., username)
        String customerIdentifier = rentalRecord.getRentingCustomer().getUsername();
        customerRentalMap.computeIfAbsent(customerIdentifier, k -> new ArrayList<>()).add(rentalRecord);

        // Add to date-based map
        Date transactionDate = rentalRecord.getTransactionDate();
        dateRentalMap.computeIfAbsent(transactionDate, k -> new ArrayList<>()).add(rentalRecord);
    }

    // Getters for the maps

    public Map<String, List<RentalRecord>> getCustomerRentalMap() {
        return customerRentalMap;
    }

    public Map<Date, List<RentalRecord>> getDateRentalMap() {
        return dateRentalMap;
    }

    //TODO delete the following two methods
    public List<RentalRecord> getRentalHistoryForCustomer(String customerUsername) {
        return customerRentalMap.getOrDefault(customerUsername, new ArrayList<>());
    }

    public List<RentalRecord> getRentalHistoryInDateRange(Date startDate, Date endDate) {
        List<RentalRecord> result = new ArrayList<>();

        for (Map.Entry<Date, List<RentalRecord>> entry : dateRentalMap.entrySet()) {
            Date transactionDate = entry.getKey();
            if (transactionDate.after(startDate) && transactionDate.before(endDate)) {
                result.addAll(entry.getValue());
            }
        }

        return result;
    }

    public RentalHistory getRentalHistoryInDateRangeRH(Date startDate, Date endDate) {
        RentalHistory filteredHistory = new RentalHistory();

        for (Map.Entry<Date, List<RentalRecord>> entry : dateRentalMap.entrySet()) {
            Date transactionDate = entry.getKey();
            if (transactionDate.after(startDate) && transactionDate.before(endDate)) {
                for (RentalRecord myRecord : entry.getValue()) {
                    filteredHistory.addRentalRecord(myRecord);
                }
            }
        }

        return filteredHistory;
    }

    public RentalHistory getRentalHistoryForCustomerRH(String customerUsername) {
        RentalHistory filteredHistory = new RentalHistory();
    
        if (customerRentalMap != null) {
            List<RentalRecord> records = customerRentalMap.getOrDefault(customerUsername, new ArrayList<>());
    
            for (RentalRecord myRecord : records) {
                filteredHistory.addRentalRecord(myRecord);
            }
        }
    
        return filteredHistory;
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
