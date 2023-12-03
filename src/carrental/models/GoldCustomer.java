package carrental.models;

import java.util.Date;

import carrental.util.PriceCalculation;

public class GoldCustomer extends Customer {
    
    public GoldCustomer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    @Override
    public double calculateRentalPrice(Car selectedCar, Date startDate, Date endDate, PricingAttributes pricingAttributes) {
        // Default pricing logic for regular customers
        double durationBasedPrice = PriceCalculation.calculateDurationBasedPrice(
                selectedCar.getPrice(), startDate, endDate, pricingAttributes);
        double additionalServicesPrice = PriceCalculation.calculateAdditionalServicesPrice(selectedCar.getAdditionalFeatures(),
                pricingAttributes);
        double finalPrice = PriceCalculation.calculateFinalPrice(durationBasedPrice, additionalServicesPrice);
        double discountedFinalPrice = PriceCalculation.calculateDiscountedPrice(finalPrice, 50);

        // Display the price window
        PriceCalculation.displayPriceWindow(selectedCar.getPrice(), durationBasedPrice, additionalServicesPrice, discountedFinalPrice, selectedCar.getAdditionalFeatures(), pricingAttributes);
        return finalPrice;
    }
}
