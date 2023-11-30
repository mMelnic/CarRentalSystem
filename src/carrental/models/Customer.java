package carrental.models;

import java.util.Date;
import java.util.Set;

import carrental.util.CustomerAuthentication;

public class Customer extends User{
    private static final int REGULAR_TO_BRONZE_THRESHOLD = 5;
    private static final int BRONZE_TO_SILVER_THRESHOLD = 10;
    private static final int SILVER_TO_GOLD_THRESHOLD = 20;

    private static final long serialVersionUID = 3493219094674800571L;

    public Customer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    public CarInventory searchCarToRent(String manufacturer, String model, Car.ComfortLevel comfortLevel, Set<Car.AdditionalFeatures> features, Date startDate, Date endDate, CarInventory inventory) {
        return inventory.searchWithMultipleCriteria(manufacturer, model, comfortLevel, features, startDate, endDate);
    }

    public void checkAndUpgrade(RentalHistory rentalHistory) {
        int numberOfReservations = rentalHistory.getNumberOfReservationsForCustomer(getUsername());
        String username = getUsername();

        if (numberOfReservations >= REGULAR_TO_BRONZE_THRESHOLD && !(this instanceof BronzeCustomer)) {
            CustomerAuthentication.upgradeCustomerToBronze(username);
        } else if (numberOfReservations >= BRONZE_TO_SILVER_THRESHOLD && !(this instanceof SilverCustomer)) {
            CustomerAuthentication.upgradeCustomerToSilver(username);
        } else if (numberOfReservations >= SILVER_TO_GOLD_THRESHOLD && !(this instanceof GoldCustomer)) {
            CustomerAuthentication.upgradeCustomerToGold(username);
        }
    }

}
