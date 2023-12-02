package carrental.models;

import java.util.Date;

import carrental.util.PricingCalculation;

public class GoldCustomer extends Customer {
    
    public GoldCustomer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    @Override
    public double calculateRentalPrice(Car selectedCar, Date startDate, Date endDate, PricingAttributes pricingAttributes) {
        // Default pricing logic for regular customers
        double durationBasedPrice = PricingCalculation.calculateDurationBasedPrice(
                selectedCar.getPrice(), startDate, endDate, pricingAttributes);
        double additionalServicesPrice = PricingCalculation.calculateAdditionalServicesPrice(selectedCar.getAdditionalFeatures(),
                pricingAttributes);
        double finalPrice = PricingCalculation.calculateFinalPrice(durationBasedPrice, additionalServicesPrice);
        double discountedFinalPrice = PricingCalculation.calculateDiscountedPrice(finalPrice, 50);

        // Display the price window
        PricingCalculation.displayPriceWindow(selectedCar.getPrice(), durationBasedPrice, additionalServicesPrice, discountedFinalPrice, selectedCar.getAdditionalFeatures(), pricingAttributes);
        return finalPrice;
    }
}
