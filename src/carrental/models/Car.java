package carrental.models;

import java.io.Serializable;
import java.util.Set;

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
}
