package carrental.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private boolean isRented;
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
               double price, boolean isRented, ComfortLevel comfortLevel, Set<AdditionalFeatures> additionalFeatures) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.registrationInfo = registrationInfo;
        this.color = color;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.isRented = isRented;
        this.comfortLevel = comfortLevel;
        this.additionalFeatures = additionalFeatures;
        this.rentalIntervals = new ArrayList<>();
    }

    // Getters and Setters for the attributes

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

    public boolean getRentedStatus() {
        return isRented;
    }

    public void setRentedStatus(boolean status) {
        isRented = status;
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

    public void addRentalInterval(UUID rentId, Date startDate, Date endDate) {
        RentalInterval interval = new RentalInterval(rentId, startDate, endDate);
        rentalIntervals.add(interval);
    }

    // Method to check if a given interval overlaps with any existing intervals
    public boolean hasOverlap(Date newStartDate, Date newEndDate) {
        for (RentalInterval interval : rentalIntervals) {
            if (isOverlap(interval.getStartDate(), interval.getEndDate(), newStartDate, newEndDate)) {
                return true; // Overlap found
            }
        }
        return false; // No overlap found
    }

    // Helper method to check if two intervals overlap
    private boolean isOverlap(Date start1, Date end1, Date start2, Date end2) {
        LocalDate localStart1 = dateToLocalDate(start1);
        LocalDate localEnd1 = dateToLocalDate(end1);
        LocalDate localStart2 = dateToLocalDate(start2);
        LocalDate localEnd2 = dateToLocalDate(end2);

        return !localEnd1.isBefore(localStart2) && !localStart1.isAfter(localEnd2);
    }

    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
    }

    public List<RentalInterval> getRentalIntervals() {
        return rentalIntervals;
    }

    public void setRentalIntervals(List<RentalInterval> rentalIntervals) {
        this.rentalIntervals = rentalIntervals;
    }
}
