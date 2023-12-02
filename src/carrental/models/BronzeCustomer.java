package carrental.models;

import java.util.Date;

import carrental.util.PricingCalculation;

public class BronzeCustomer extends Customer {
    private static final long serialVersionUID = -7706992655032230086L;
    
    public BronzeCustomer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }
    
    @Override
    public double calculateRentalPrice(Car selectedCar, Date startDate, Date endDate, PricingAttributes pricingAttributes) {
        // Default pricing logic for regular customers
        double durationBasedPrice = PricingCalculation.calculateDurationBasedPrice(
                selectedCar.getPrice(), startDate, endDate, pricingAttributes);
        double additionalServicesPrice = PricingCalculation.calculateAdditionalServicesPrice(selectedCar.getAdditionalFeatures(),
                pricingAttributes);

        double discountedAdditionalServicesPrice = PricingCalculation.calculateDiscountedPrice(additionalServicesPrice,
                60);
        double finalPrice = PricingCalculation.calculateFinalPrice(durationBasedPrice, discountedAdditionalServicesPrice);

        // Display the price window
        PricingCalculation.displayPriceWindow(selectedCar.getPrice(), durationBasedPrice, discountedAdditionalServicesPrice, finalPrice, selectedCar.getAdditionalFeatures(), pricingAttributes);
        return finalPrice;
    }
}
