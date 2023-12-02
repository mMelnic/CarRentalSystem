package carrental.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.SwingUtilities;

import carrental.models.PricingAttributes;
import carrental.gui.PriceWindow;
import carrental.models.Car.AdditionalFeatures;

public class PricingCalculation {
    private PricingCalculation() {}

    public static double calculateDurationBasedPrice(double basePrice, Date startDate, Date endDate, PricingAttributes pricingAttribute) {
        // Calculate the duration in days
        LocalDate startLocalDate = startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
        int durationInDays = (int) ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
        boolean isPeakSeason = isPeakSeason(startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate(), pricingAttribute);

        double seasonalBasePrice = calculateSeasonalDemandBasedPrice(basePrice, isPeakSeason, pricingAttribute);
        // Calculate the total cost based on the duration
        double totalCost = seasonalBasePrice * durationInDays;

        // Apply discounts based on the duration
        if (durationInDays >= 7) {
            totalCost -= totalCost * pricingAttribute.getWeeklyDiscountRate();
        }

        if (durationInDays >= 30) {
            totalCost -= totalCost * pricingAttribute.getMonthlyDiscountRate();
        }

        return totalCost;
    }

    // Function to calculate rental price based on seasonal demand
    public static double calculateSeasonalDemandBasedPrice(double basePrice, boolean isPeakSeason, PricingAttributes pricingAttribute) {
        return isPeakSeason ? basePrice * pricingAttribute.getPeakSeasonRateMultiplier() : basePrice;
    }

    public static boolean isPeakSeason(LocalDate rentalDate, PricingAttributes pricingAttribute) {
        int rentalMonth = rentalDate.getMonthValue();
        return (rentalMonth >= pricingAttribute.getPeakSeasonStartMonth() && rentalMonth <= pricingAttribute.getPeakSeasonEndMonth());
    }

    public static double calculateDiscountedSeasonalDemandBasedPrice(double basePrice, boolean isPeakSeason, PricingAttributes pricingAttribute) {
        double seasonalBasePrice = isPeakSeason ? basePrice * pricingAttribute.getPeakSeasonRateMultiplier() : basePrice;
        return 0.5 * seasonalBasePrice;
    }

    public static double calculateDiscountedDurationBasedPrice(double basePrice, Date startDate, Date endDate, PricingAttributes pricingAttribute) {
        // Calculate the duration in days
        LocalDate startLocalDate = startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
        int durationInDays = (int) ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
        boolean isPeakSeason = isPeakSeason(startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate(), pricingAttribute);

        double seasonalBasePrice = calculateDiscountedSeasonalDemandBasedPrice(basePrice, isPeakSeason, pricingAttribute);
        // Calculate the total cost based on the duration
        double totalCost = seasonalBasePrice * durationInDays;

        // Apply discounts based on the duration
        if (durationInDays >= 7) {
            totalCost -= totalCost * pricingAttribute.getWeeklyDiscountRate();
            totalCost = calculateDiscountedPrice(totalCost, 50);
        }

        if (durationInDays >= 30) {
            totalCost -= totalCost * pricingAttribute.getMonthlyDiscountRate();
            totalCost = calculateDiscountedPrice(totalCost, 70);
        }

        return totalCost;
    }

    // Function to calculate rental price based on additional services
    public static double calculateAdditionalServicesPrice(Set<AdditionalFeatures> features, PricingAttributes pricingAttributes) {

        double additionalServicesPrice = 0.0;
        for (AdditionalFeatures feature : features) {
            switch (feature) {
                case GPS:
                    additionalServicesPrice += pricingAttributes.getGpsServiceCharge();
                    break;
                case CHILD_SEAT:
                    additionalServicesPrice += pricingAttributes.getChildSeatCharge();
                    break;
                case INSURANCE:
                    additionalServicesPrice += pricingAttributes.getInsuranceCharge();
                    break;
                case LEATHER_INTERIOR:
                    additionalServicesPrice += pricingAttributes.getLeatherInteriorCharge();
                    break;
                case SUNROOF:
                    additionalServicesPrice += pricingAttributes.getSunroofCharge();
                    break;
                case HYBRID_TECHNOLOGY:
                    additionalServicesPrice += pricingAttributes.getHybridTechnologyCharge();
                    break;
            }
        }

        return additionalServicesPrice;
    }



    public static double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        // Calculate the discounted price
        double discountMultiplier = 1 - (discountPercentage / 100);
        return originalPrice * discountMultiplier;
    }

    public static double calculateFinalPrice(double totalPrice, double additionalFeaturesPrice) {
        return totalPrice + additionalFeaturesPrice;
    }

    // Method to display the PriceWindow
    public static void displayPriceWindow(double basePrice, double durationBasedPrice, double additionalServicesPrice, double finalPrice,
                                          Set<AdditionalFeatures> additionalFeatures, PricingAttributes pricingAttributes) {
        SwingUtilities.invokeLater(() ->
                new PriceWindow(basePrice, durationBasedPrice, additionalServicesPrice, finalPrice, additionalFeatures, pricingAttributes)
        );
    }
}
