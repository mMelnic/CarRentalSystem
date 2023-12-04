package carrental.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Car implements Serializable{
    private String manufacturer;
    private String model;
    private String registrationInfo;
    private String color;
    private int yearOfProduction;
    private double price;
    private ComfortLevel comfortLevel;
    private Set<AdditionalFeatures> additionalFeatures;
    private List<RentalInterval> rentalIntervals;
    private static final long serialVersionUID = -9138178394279345304L;

    public enum ComfortLevel {
        BASIC,
        STANDARD,
        SUV,
        LUXURY
    }

    public enum AdditionalFeatures {
        GPS,
        CHILD_SEAT,
        INSURANCE,
        LEATHER_INTERIOR,
        SUNROOF,
        HYBRID_TECHNOLOGY
    }

    public Car(String manufacturer, String model, String registrationInfo, String color, int yearOfProduction,
               double price, ComfortLevel comfortLevel, Set<AdditionalFeatures> additionalFeatures) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.registrationInfo = registrationInfo;
        this.color = color;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.comfortLevel = comfortLevel;
        this.additionalFeatures = additionalFeatures;
        this.rentalIntervals = new ArrayList<>();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationInfo() {
        return registrationInfo;
    }

    public void setRegistrationInfo(String registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ComfortLevel getComfortLevel() {
        return comfortLevel;
    }

    public void setComfortLevel(ComfortLevel comfortLevel) {
        this.comfortLevel = comfortLevel;
    }

    public Set<AdditionalFeatures> getAdditionalFeatures() {
        return additionalFeatures;
    }

    public void setAdditionalFeatures(Set<AdditionalFeatures> additionalFeatures) {
        this.additionalFeatures = additionalFeatures;
    }
    
    public List<RentalInterval> getRentalIntervals() {
        return rentalIntervals;
    }

    public void setRentalIntervals(List<RentalInterval> rentalIntervals) {
        this.rentalIntervals = rentalIntervals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(registrationInfo, car.registrationInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationInfo);
    }

    public void addRentalInterval(UUID rentId, Date startDate, Date endDate) {
        RentalInterval interval = new RentalInterval(rentId, startDate, endDate);
        rentalIntervals.add(interval);
    }

    public RentalInterval getRentalIntervalById(UUID rentId) {
        return rentalIntervals.stream()
                .filter(interval -> interval.getRentId().equals(rentId))
                .findFirst()
                .orElse(null);
    }

    public boolean canCarBeDeleted() {
        return rentalIntervals.isEmpty();
    }

    public void removeRentalIntervalWithId(UUID rentId) {
        rentalIntervals.removeIf(interval -> interval.getRentId().equals(rentId));
    }

    // Method to check if a given interval overlaps with any existing intervals
    public boolean hasOverlapWithExistingIntervals(Date newStartDate, Date newEndDate) {
        for (RentalInterval interval : rentalIntervals) {
            if (areTwoIntervalsOverlapping(interval.getStartDate(), interval.getEndDate(), newStartDate, newEndDate)) {
                return true; // Overlap found
            }
        }
        return false; // No overlap found
    }

    private boolean hasOverlapWithExistingIntervalsExceptItself(UUID rentId, Date newStartDate, Date newEndDate) {
        // Check for overlaps, ignoring the interval with the same rentId
        for (RentalInterval interval : rentalIntervals) {
            if (!interval.getRentId().equals(rentId) && areTwoIntervalsOverlapping(interval.getStartDate(), interval.getEndDate(), newStartDate, newEndDate)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to check if two intervals overlap
    private boolean areTwoIntervalsOverlapping(Date start1, Date end1, Date start2, Date end2) {
        LocalDate localStart1 = dateToLocalDate(start1);
        LocalDate localEnd1 = dateToLocalDate(end1);
        LocalDate localStart2 = dateToLocalDate(start2);
        LocalDate localEnd2 = dateToLocalDate(end2);

        return !localEnd1.isBefore(localStart2) && !localStart1.isAfter(localEnd2);
    }

    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
    }

    public boolean modifyReservation(UUID rentId, Date newStartDate, Date newEndDate) {
        // Check if the modification is allowed
        if (!hasOverlapWithExistingIntervalsExceptItself(rentId, newStartDate, newEndDate)) {
            // Remove the existing rental interval with the given rentId
            removeRentalIntervalWithId(rentId);
            // Add the new rental interval
            addRentalInterval(rentId, newStartDate, newEndDate);

            return true; // Return true if the modification is successful
        }
        return false;
    }
    
    public boolean isModificationAllowed(UUID rentId) {
        // Find the existing rental interval with the given rentID
        RentalInterval existingInterval = getRentalIntervalById(rentId);

        // Check if the existing start date is more than 5 days in the future
        if (existingInterval != null) {
            LocalDate existingStartDate = dateToLocalDate(existingInterval.getStartDate());
            LocalDate currentDate = dateToLocalDate(new Date());

            // Check if the existing start date is more than 5 days in the future
            return existingStartDate.isAfter(currentDate.plusDays(5));
        }

        // If the existing interval is not found, consider it allowed
        return true;
    }

    public Object[] getCarData() {
        Object[] carData = new Object[8]; // Adjust the size based on the number of attributes

        carData[0] = manufacturer;
        carData[1] = model;
        carData[2] = registrationInfo;
        carData[3] = color;
        carData[4] = yearOfProduction;
        carData[5] = price;
        carData[6] = comfortLevel;
        carData[7] = formatAdditionalFeatures(additionalFeatures);

        return carData;
    }

    // Utility method to format additional features as a string
    private String formatAdditionalFeatures(Set<AdditionalFeatures> features) {
        StringBuilder featuresString = new StringBuilder();
        for (AdditionalFeatures feature : features) {
            featuresString.append(feature).append(", ");
        }
        // Remove the trailing ", " if features are present
        if (featuresString.length() > 0) {
            featuresString.delete(featuresString.length() - 2, featuresString.length());
        }
        return featuresString.toString();
    }
}
