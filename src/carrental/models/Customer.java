package carrental.models;

import java.util.Date;
import java.util.Set;

public class Customer extends User{
    // private String phoneNumber;
    // private String address;
    private static final long serialVersionUID = 3493219094674800571L;

    public Customer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    // public String getPhoneNumber() {
    //     return phoneNumber;
    // }

    // public void setPhoneNumber(String phoneNumber) {
    //     this.phoneNumber = phoneNumber;
    // }

    // public String getAddress() {
    //     return address;
    // }

    // public void setAddress(String address) {
    //     this.address = address;
    // }

    public CarInventory searchCarToRent(String manufacturer, String model, Car.ComfortLevel comfortLevel, Set<Car.AdditionalFeatures> features, Date startDate, Date endDate, CarInventory inventory) {
        return inventory.searchWithMultipleCriteria(manufacturer, model, comfortLevel, features, startDate, endDate);
    }

}
