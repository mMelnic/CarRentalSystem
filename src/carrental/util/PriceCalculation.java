package carrental.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.swing.SwingUtilities;

import carrental.models.PricingAttributes;
import carrental.gui.PriceWindow;
import carrental.models.Car.AdditionalFeatures;

public class PriceCalculation {
    private PriceCalculation() {}

    public static boolean isPeakSeason(LocalDate rentalDate, PricingAttributes charges) {
        int rentalMonth = rentalDate.getMonthValue();
        return (rentalMonth >= charges.getPeakSeasonStartMonth() && rentalMonth <= charges.getPeakSeasonEndMonth());
    }

    // Function to calculate rental price based on seasonal demand
    public static double calculateSeasonalDemandBasedPrice(double basePrice, boolean isPeakSeason, PricingAttributes charges) {
        return isPeakSeason ? basePrice * charges.getPeakSeasonRateMultiplier() : basePrice;
    }

    public static double calculateDurationBasedPrice(double basePrice, Date startDate, Date endDate, PricingAttributes charges) {
        // Calculate the duration in days
        int durationInDays = calculateDurationInDays(startDate, endDate);
        boolean isPeakSeason = isPeakSeason(startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate(), charges);

        double seasonalBasePrice = calculateSeasonalDemandBasedPrice(basePrice, isPeakSeason, charges);
        // Calculate the total cost based on the duration
        double totalCost = seasonalBasePrice * durationInDays;

        // Discounts based on the duration
        if (durationInDays >= 7) {
            totalCost -= totalCost * charges.getWeeklyDiscountRate();
        }

        if (durationInDays >= 30) {
            totalCost -= totalCost * charges.getMonthlyDiscountRate();
        }

        return totalCost;
    }

    public static double calculateDiscountedSeasonalDemandBasedPrice(double basePrice, boolean isPeakSeason, PricingAttributes charges) {
        double seasonalBasePrice = isPeakSeason ? basePrice * charges.getPeakSeasonRateMultiplier() : basePrice;
        return 0.5 * seasonalBasePrice;
    }

    public static double calculateDiscountedDurationBasedPrice(double basePrice, Date startDate, Date endDate, PricingAttributes charges) {
        // Calculate the duration in days
        int durationInDays = calculateDurationInDays(startDate, endDate);
        boolean isPeakSeason = isPeakSeason(startDate.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate(), charges);
        double seasonalBasePrice = calculateDiscountedSeasonalDemandBasedPrice(basePrice, isPeakSeason, charges);
        // Calculate the total cost based on the duration
        double totalCost = seasonalBasePrice * durationInDays;

        // Apply discounts based on the duration
        if (durationInDays >= 7) {
            totalCost -= totalCost * charges.getWeeklyDiscountRate();
            totalCost = calculateDiscountedPrice(totalCost, 50);
        }

        if (durationInDays >= 30) {
            totalCost -= totalCost * charges.getMonthlyDiscountRate();
            totalCost = calculateDiscountedPrice(totalCost, 70);
        }

        return totalCost;
    }

    public static double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        // Calculate the discounted price
        double discountMultiplier = 1 - (discountPercentage / 100);
        return originalPrice * discountMultiplier;
    }

    private static int calculateDurationInDays(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
    }

    // Function to calculate rental price based on additional services
    public static double calculateAdditionalServicesPrice(Set<AdditionalFeatures> features, PricingAttributes serviceCharges) {

        double additionalServicesPrice = 0.0;
        for (AdditionalFeatures feature : features) {
            switch (feature) {
                case GPS:
                    additionalServicesPrice += serviceCharges.getGpsServiceCharge();
                    break;
                case CHILD_SEAT:
                    additionalServicesPrice += serviceCharges.getChildSeatCharge();
                    break;
                case INSURANCE:
                    additionalServicesPrice += serviceCharges.getInsuranceCharge();
                    break;
                case LEATHER_INTERIOR:
                    additionalServicesPrice += serviceCharges.getLeatherInteriorCharge();
                    break;
                case SUNROOF:
                    additionalServicesPrice += serviceCharges.getSunroofCharge();
                    break;
                case HYBRID_TECHNOLOGY:
                    additionalServicesPrice += serviceCharges.getHybridTechnologyCharge();
                    break;
            }
        }

        return additionalServicesPrice;
    }

    public static double calculateFinalPrice(double totalPrice, double additionalFeaturesPrice) {
        return totalPrice + additionalFeaturesPrice;
    }

    // Method to display the PriceWindow
    public static void displayPriceWindow(double basePrice, double durationBasedPrice, double additionalServicesPrice, double finalPrice,
                                          Set<AdditionalFeatures> additionalFeatures, PricingAttributes charges) {
        SwingUtilities.invokeLater(() ->
                new PriceWindow(basePrice, durationBasedPrice, additionalServicesPrice, finalPrice, additionalFeatures, charges)
        );
    }
}
