package carrental.util;

import java.time.LocalDate;
import java.util.Set;

import carrental.models.Car.AdditionalFeatures;

public class PricingCalculation {
    private static final double DEFAULT_DAILY_RATE = 50.0;
    private static final double DEFAULT_WEEKLY_RATE = 300.0;
    private static final double DEFAULT_MONTHLY_RATE = 1200.0;

    private static final double DEFAULT_ECONOMY_RATE = 40.0;
    private static final double DEFAULT_STANDARD_RATE = 60.0;
    private static final double DEFAULT_SUV_RATE = 80.0;
    private static final double DEFAULT_LUXURY_RATE = 120.0;

    private static final double PEAK_SEASON_RATE_MULTIPLIER = 1.2;
    private static final int PEAK_SEASON_START_MONTH = 6; // June
    private static final int PEAK_SEASON_END_MONTH = 8;   // August

    private static final double DEFAULT_GPS_SERVICE_CHARGE = 10.0;
    private static final double DEFAULT_INSURANCE_CHARGE = 20.0;
    private static final double DEFAULT_CHILD_SEAT_CHARGE = 15.0;
    private static final double DEFAULT_LEATHER_INTERIOR_CHARGE = 25.0;
    private static final double DEFAULT_SUNROOF_CHARGE = 20.0;
    private static final double DEFAULT_HYBRID_TECHNOLOGY_CHARGE = 30.0;

    // Function to calculate rental price based on duration
    public double calculateDurationBasedPriceWithBase(double basePrice, int days, int weeks, int months, double dailyRate, double weeklyRate, double monthlyRate) {
        dailyRate = (dailyRate > 0) ? dailyRate : DEFAULT_DAILY_RATE;
        weeklyRate = (weeklyRate > 0) ? weeklyRate : DEFAULT_WEEKLY_RATE;
        monthlyRate = (monthlyRate > 0) ? monthlyRate : DEFAULT_MONTHLY_RATE;
    
        double totalCost = (days * dailyRate) + (weeks * weeklyRate) + (months * monthlyRate);
    
        // Reduce the total cost based on the duration
        if (days >= 7) {
            totalCost -= basePrice * 0.1; // 10% discount for a week or more
        }
    
        if (weeks >= 4) {
            totalCost -= basePrice * 0.2; // 20% discount for a month or more
        }
    
        return totalCost;
    }
    // Function to calculate rental price based on car class
    public double calculateCarComfortLevelBasedPrice(String carClass, double economyRate, double standardRate, double suvRate, double luxuryRate) {
        economyRate = (economyRate > 0) ? economyRate : DEFAULT_ECONOMY_RATE;
        standardRate = (standardRate > 0) ? standardRate : DEFAULT_STANDARD_RATE;
        suvRate = (suvRate > 0) ? suvRate : DEFAULT_SUV_RATE;
        luxuryRate = (luxuryRate > 0) ? luxuryRate : DEFAULT_LUXURY_RATE;

        switch (carClass.toUpperCase()) {
            case "ECONOMY":
                return economyRate;
            case "COMPACT":
                return standardRate;
            case "SUV":
                return suvRate;
            case "LUXURY":
                return luxuryRate;
            default:
                return 0.0; // Invalid car class
        }
    }

    // Function to calculate rental price based on seasonal demand
    public double calculateSeasonalDemandBasedPrice(double basePrice, boolean isPeakSeason, double peakSeasonRateMultiplier) {
        peakSeasonRateMultiplier = (peakSeasonRateMultiplier > 1.0) ? peakSeasonRateMultiplier : PEAK_SEASON_RATE_MULTIPLIER;
        return isPeakSeason ? basePrice * peakSeasonRateMultiplier : basePrice;
    }

    public boolean isPeakSeason(LocalDate rentalDate) {
        int rentalMonth = rentalDate.getMonthValue();
        return (rentalMonth >= PEAK_SEASON_START_MONTH && rentalMonth <= PEAK_SEASON_END_MONTH);
    }

    // Function to calculate rental price based on additional services
    public double calculateAdditionalServicesPrice(Set<AdditionalFeatures> features, double gpsServiceCharge, double insuranceCharge, double childSeatCharge,
                                                   double leatherInteriorCharge, double sunroofCharge, double hybridTechnologyCharge) {
        gpsServiceCharge = (gpsServiceCharge > 0) ? gpsServiceCharge : DEFAULT_GPS_SERVICE_CHARGE;
        insuranceCharge = (insuranceCharge > 0) ? insuranceCharge : DEFAULT_INSURANCE_CHARGE;
        childSeatCharge = (childSeatCharge > 0) ? childSeatCharge : DEFAULT_CHILD_SEAT_CHARGE; // No default charge for child seat
        leatherInteriorCharge = (leatherInteriorCharge > 0) ? leatherInteriorCharge : DEFAULT_LEATHER_INTERIOR_CHARGE; // No default charge for leather interior
        sunroofCharge = (sunroofCharge > 0) ? sunroofCharge : DEFAULT_SUNROOF_CHARGE; // No default charge for sunroof
        hybridTechnologyCharge = (hybridTechnologyCharge > 0) ? hybridTechnologyCharge : DEFAULT_HYBRID_TECHNOLOGY_CHARGE; // No default charge for hybrid technology

        double additionalServicesPrice = 0.0;
        for (AdditionalFeatures feature : features) {
            switch (feature) {
                case GPS:
                    additionalServicesPrice += gpsServiceCharge;
                    break;
                case CHILD_SEAT:
                    additionalServicesPrice += childSeatCharge;
                    break;
                case INSURANCE:
                    additionalServicesPrice += insuranceCharge;
                    break;
                case LEATHER_INTERIOR:
                    additionalServicesPrice += leatherInteriorCharge;
                    break;
                case SUNROOF:
                    additionalServicesPrice += sunroofCharge;
                    break;
                case HYBRID_TECHNOLOGY:
                    additionalServicesPrice += hybridTechnologyCharge;
                    break;
            }
        }

        return additionalServicesPrice;
    }
}
