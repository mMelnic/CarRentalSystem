package carrental.models;

import java.util.Date;
import java.util.Set;

import carrental.util.CustomerAuthentication;
import carrental.util.PriceCalculation;
import carrental.util.Thresholds;

public class Customer extends User{
    private static final long serialVersionUID = 3493219094674800571L;

    public Customer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    public CarInventory searchCarToRent(String manufacturer, String model, Car.ComfortLevel comfortLevel, Set<Car.AdditionalFeatures> features, Date startDate, Date endDate, CarInventory inventory) {
        return inventory.searchWithMultipleCriteria(manufacturer, model, comfortLevel, features, startDate, endDate);
    }

    public Customer checkAndUpgrade(RentalHistory rentalHistory) {
        int numberOfReservations = rentalHistory.getNumberOfReservationsForCustomer(getUsername());
        String username = getUsername();

        if (numberOfReservations >= Thresholds.REGULAR_TO_BRONZE.getThreshold() && !(this instanceof BronzeCustomer) && !(this instanceof SilverCustomer) && !(this instanceof GoldCustomer)) {
            return CustomerAuthentication.upgradeCustomerToBronze(username);
        } else if (numberOfReservations >= Thresholds.BRONZE_TO_SILVER.getThreshold() && !(this instanceof SilverCustomer) && !(this instanceof GoldCustomer)) {
            return CustomerAuthentication.upgradeCustomerToSilver(username);
        } else if (numberOfReservations >= Thresholds.SILVER_TO_GOLD.getThreshold() && !(this instanceof GoldCustomer)) {
            return CustomerAuthentication.upgradeCustomerToGold(username);
        }

        return this;
    }

    public Customer checkAndDowngrade(RentalHistory rentalHistory) {
        int numberOfReservations = rentalHistory.getNumberOfReservationsForCustomer(getUsername());
        String username = getUsername();

        if (numberOfReservations < Thresholds.SILVER_TO_GOLD.getThreshold() && this instanceof GoldCustomer) {
            return CustomerAuthentication.downgradeGoldToSilver(username);
        } else if (numberOfReservations < Thresholds.BRONZE_TO_SILVER.getThreshold() && this instanceof SilverCustomer) {
            return CustomerAuthentication.downgradeSilverToBronze(username);
        } else if (numberOfReservations < Thresholds.REGULAR_TO_BRONZE.getThreshold() && this instanceof BronzeCustomer) {
            return CustomerAuthentication.downgradeBronzeToRegular(username);
        }

        return this;
    }

    public double calculateRentalPrice(Car selectedCar, Date startDate, Date endDate, PricingAttributes pricingAttributes) {
        // Default pricing logic for regular customers
        double durationBasedPrice = PriceCalculation.calculateDurationBasedPrice(
                selectedCar.getPrice(), startDate, endDate, pricingAttributes);
        double additionalServicesPrice = PriceCalculation.calculateAdditionalServicesPrice(selectedCar.getAdditionalFeatures(),
                pricingAttributes);
        double finalPrice = PriceCalculation.calculateFinalPrice(durationBasedPrice, additionalServicesPrice);

        // Display the price window
        PriceCalculation.displayPriceWindow(selectedCar.getPrice(), durationBasedPrice, additionalServicesPrice, finalPrice, selectedCar.getAdditionalFeatures(), pricingAttributes);
        return finalPrice;
    }
}
