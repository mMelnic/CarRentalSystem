package carrental.models;

import java.util.Date;

import carrental.util.PriceCalculation;

public class GoldCustomer extends Customer {
    private static final long serialVersionUID = 3L;

    public GoldCustomer(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    @Override
    public double calculateRentalPrice(Car selectedCar, Date startDate, Date endDate,
            PricingAttributes pricingAttributes) {
        // Default pricing logic for regular customers
        double durationBasedPrice = PriceCalculation.calculateDiscountedDurationBasedPrice(
                selectedCar.getPrice(), startDate, endDate, pricingAttributes);
        double additionalServicesPrice = PriceCalculation.calculateAdditionalServicesPrice(
                selectedCar.getAdditionalFeatures(),
                pricingAttributes);

        double discountedAdditionalServicesPrice = PriceCalculation.calculateDiscountedPrice(additionalServicesPrice,
                70);
        double finalPrice = PriceCalculation.calculateFinalPrice(durationBasedPrice, discountedAdditionalServicesPrice);

        // Display the price window
        PriceCalculation.displayPriceWindow(selectedCar.getPrice(), durationBasedPrice,
                discountedAdditionalServicesPrice, finalPrice, selectedCar.getAdditionalFeatures(), pricingAttributes);
        return finalPrice;
    }
}
