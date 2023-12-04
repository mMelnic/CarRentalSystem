package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import carrental.models.Car;
import carrental.models.Car.AdditionalFeatures;
import carrental.models.Car.ComfortLevel;
import carrental.models.GoldCustomer;
import carrental.models.PricingAttributes;
import carrental.models.SilverCustomer;
import carrental.util.PriceCalculation;

public class PriceCalculationTest {
    private static Date convertToLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Test
    public void testIsPeakSeason() {
        LocalDate peakSeasonDate = LocalDate.of(2023, 6, 15);
        PricingAttributes charges = new PricingAttributes(1, 1, 1, 6, 8, 1, 1, 1, 1, 1, 1);

        assertTrue(PriceCalculation.isPeakSeason(peakSeasonDate, charges));
    }

    @Test
    public void testCalculateSeasonalDemandBasedPrice() {
        double basePrice = 100.0;
        boolean isPeakSeason = true;
        PricingAttributes charges = new PricingAttributes(1, 1, 2.5, 1, 1, 1, 1, 1, 1, 1, 1);

        double result = PriceCalculation.calculateSeasonalDemandBasedPrice(basePrice, isPeakSeason, charges);

        assertEquals(250.0, result, 0);
    }

    @Test
    public void testCalculateDiscountedSeasonalDemandBasedPrice() {
        double basePrice = 100.0;
        boolean isPeakSeason = true;
        PricingAttributes charges = new PricingAttributes(1, 1, 2.5, 1, 1, 1, 1, 1, 1, 1, 1);

        double result = PriceCalculation.calculateDiscountedSeasonalDemandBasedPrice(basePrice, isPeakSeason, charges);

        assertEquals(125.0, result, 0);
    }

    @Test
    public void testCalculateDiscountedPrice() {
        double originalPrice = 100.0;
        double discountPercentage = 20.0;

        double result = PriceCalculation.calculateDiscountedPrice(originalPrice, discountPercentage);

        assertEquals(80.0, result, 0);
    }

    @Test
    public void testCalculateAdditionalServicesPrice() {
        Set<AdditionalFeatures> features = new HashSet<>();
        features.add(AdditionalFeatures.GPS);
        features.add(AdditionalFeatures.CHILD_SEAT);
        PricingAttributes serviceCharges = new PricingAttributes(1, 1, 1, 1, 1, 100, 200, 150, 175, 190, 250);

        double result = PriceCalculation.calculateAdditionalServicesPrice(features, serviceCharges);

        assertEquals(250, result, 0);
    }

    @Test
    public void testCalculateRentalPriceForSilverCustomer() {
        // User data for testing
        String username = "testUser";
        String password = "testPassword";
        String fullName = "Test User";
        String email = "test@example.com";

        // Additional features for the selected car
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(AdditionalFeatures.GPS);
        additionalFeatures.add(AdditionalFeatures.LEATHER_INTERIOR);
        additionalFeatures.add(AdditionalFeatures.INSURANCE);

        Car selectedCar = new Car("Toyota", "Prius", "ABC123", "black", 2016, 2000, ComfortLevel.BASIC, additionalFeatures);
        Date startDate = convertToLocalDateToDate(LocalDate.of(2023, 1, 1)); // Replace with your desired start date
        Date endDate = convertToLocalDateToDate(LocalDate.of(2023, 1, 7));   // Replace with your desired end date
        PricingAttributes pricingAttributes = new PricingAttributes(0.15, 0.25, 2, 1, 2, 1000, 5000, 2000, 2500, 2500, 3000);

        // Creating a GoldCustomer instance
        SilverCustomer silverCustomer = new SilverCustomer(username, password, fullName, email);

        // Calculate the expected result based on the pricing logic
        double expectedFinalPrice = 16150;

        double actualFinalPrice = silverCustomer.calculateRentalPrice(selectedCar, startDate, endDate, pricingAttributes);

        assertEquals(expectedFinalPrice, actualFinalPrice, 0.1);
    }

    @Test
    public void testCalculateRentalPriceForGoldCustomer() {
        // User data for testing
        String username = "testUser";
        String password = "testPassword";
        String fullName = "Test User";
        String email = "test@example.com";

        // Additional features for the selected car
        Set<AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(AdditionalFeatures.GPS);
        additionalFeatures.add(AdditionalFeatures.LEATHER_INTERIOR);
        additionalFeatures.add(AdditionalFeatures.INSURANCE);

        Car selectedCar = new Car("Toyota", "Prius", "ABC123", "black", 2016, 2000, ComfortLevel.BASIC, additionalFeatures);
        Date startDate = convertToLocalDateToDate(LocalDate.of(2023, 1, 1)); // Replace with your desired start date
        Date endDate = convertToLocalDateToDate(LocalDate.of(2023, 1, 7));   // Replace with your desired end date
        PricingAttributes pricingAttributes = new PricingAttributes(0.15, 0.25, 2, 1, 2, 1000, 5000, 2000, 2500, 2500, 3000);

        // Creating a SilverCustomer instance
        GoldCustomer goldCustomer = new GoldCustomer(username, password, fullName, email);

        // Calculate the expected result based on the pricing logic
        double expectedFinalPrice = 8500;

        double actualFinalPrice = goldCustomer.calculateRentalPrice(selectedCar, startDate, endDate, pricingAttributes);

        // Assert the result
        assertEquals(expectedFinalPrice, actualFinalPrice, 0.1);
    }

}
