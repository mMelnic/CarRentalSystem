package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import carrental.models.Car;
import carrental.models.Customer;
import carrental.models.RentalHistory;
import carrental.models.RentalRecord;
import carrental.models.Car.AdditionalFeatures;
import carrental.models.Car.ComfortLevel;

public class RentalHistoryTest {

    @Test
    public void testAddRentalRecord() {
        RentalHistory rentalHistory = new RentalHistory();
        RentalRecord rentalRecord = createSampleRentalRecord();

        rentalHistory.addRentalRecord(rentalRecord);

        // Check if the rental record is added to customerRentalMap
        assertTrue(rentalHistory.getCustomerRentalMap().containsKey("john_snow"));
        assertEquals(1, rentalHistory.getCustomerRentalMap().get("john_snow").size());

        // Check if the rental record is added to dateRentalMap
        assertTrue(rentalHistory.getDateRentalMap().containsKey(rentalRecord.getTransactionDate()));
        assertEquals(1, rentalHistory.getDateRentalMap().get(rentalRecord.getTransactionDate()).size());

        // Check if the number of reservations is updated
        assertEquals(1, rentalHistory.getNumberOfReservationsForCustomer("john_snow"));
    }

    @Test
    public void testDecreaseNumberOfReservations() {
        RentalHistory rentalHistory = new RentalHistory();
        RentalRecord rentalRecord = createSampleRentalRecord();

        rentalHistory.addRentalRecord(rentalRecord);
        rentalHistory.decreaseNumberOfReservations("john_snow");

        // Check if the number of reservations is decreased
        assertEquals(0, rentalHistory.getNumberOfReservationsForCustomer("john_snow"));

        // Check if the entry is removed from numberOfReservationsMap when reservations become zero
        assertFalse(rentalHistory.getNumberOfReservationsMap().containsKey("john_snow"));
    }

    @Test
    public void testGetRentalHistoryForCustomer() {
        RentalHistory rentalHistory = new RentalHistory();
        RentalRecord rentalRecord = createSampleRentalRecord();

        rentalHistory.addRentalRecord(rentalRecord);

        RentalHistory customerRentalHistory = rentalHistory.getRentalHistoryForCustomer("john_snow");

        // Check if the customer-specific rental history contains the added record
        assertTrue(customerRentalHistory.getCustomerRentalMap().containsKey("john_snow"));
        assertEquals(1, customerRentalHistory.getCustomerRentalMap().get("john_snow").size());
    }

    @Test
    public void testGetRentalHistoryInDateRange() {
        RentalHistory rentalHistory = new RentalHistory();
        RentalRecord rentalRecord = createSampleRentalRecord();

        rentalHistory.addRentalRecord(rentalRecord);

        Date startDate = rentalRecord.getTransactionDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        RentalHistory dateRangeRentalHistory = rentalHistory.getRentalHistoryInDateRange(startDate, endDate);

        // Check if the date range-specific rental history contains the added record
        assertTrue(dateRangeRentalHistory.getDateRentalMap().containsKey(startDate));
        assertEquals(1, dateRangeRentalHistory.getDateRentalMap().get(startDate).size());
    }

    @Test
    public void testUpdateOriginalHistory() {
        RentalHistory rentalHistory = new RentalHistory();
        RentalRecord rentalRecord = createSampleRentalRecord();

        rentalHistory.addRentalRecord(rentalRecord);

        RentalHistory updatedHistory = new RentalHistory();
        RentalRecord updatedRecord = createUpdatedSampleRentalRecord();
        updatedHistory.addRentalRecord(updatedRecord);

        rentalHistory.updateOriginalHistory(updatedHistory);

        // Check if the original history is updated with the records from the updated history
        assertTrue(rentalHistory.getCustomerRentalMap().containsKey("john_snow"));
        assertEquals(1, rentalHistory.getCustomerRentalMap().get("john_snow").size());

        // Check if the number of reservations is updated based on the non-cancelled records
        assertEquals(1, rentalHistory.getNumberOfReservationsForCustomer("john_snow"));
    }

    private RentalRecord createSampleRentalRecord() {
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(AdditionalFeatures.GPS);
        additionalFeatures.add(AdditionalFeatures.LEATHER_INTERIOR);
        additionalFeatures.add(AdditionalFeatures.INSURANCE);
        Customer rentingCustomer = new Customer("john_snow", "password", "John Snow", "john@example.com");
        Car rentedCar = new Car("Toyota", "Camry", "ABC123", "black", 2022, 1000, ComfortLevel.BASIC, additionalFeatures);
        return new RentalRecord(rentedCar, rentingCustomer, 2000);
    }

    private RentalRecord createUpdatedSampleRentalRecord() {
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(AdditionalFeatures.CHILD_SEAT);
        additionalFeatures.add(AdditionalFeatures.HYBRID_TECHNOLOGY);
        additionalFeatures.add(AdditionalFeatures.SUNROOF);
        Customer rentingCustomer = new Customer("john_snow", "password", "John Snow", "john@example.com");
        Car rentedCar = new Car("Honda", "Accord", "XYZ123", "white", 2023, 2250, ComfortLevel.STANDARD, additionalFeatures);
        return new RentalRecord(rentedCar, rentingCustomer, 2000);
    }
}

