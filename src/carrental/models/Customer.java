package carrental.models;

import java.util.Date;
import java.util.Set;

public class Customer extends User{

    private static final long serialVersionUID = 3493219094674800571L;

    public Customer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    public CarInventory searchCarToRent(String manufacturer, String model, Car.ComfortLevel comfortLevel, Set<Car.AdditionalFeatures> features, Date startDate, Date endDate, CarInventory inventory) {
        return inventory.searchWithMultipleCriteria(manufacturer, model, comfortLevel, features, startDate, endDate);
    }

}
