package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import carrental.models.Car;
import carrental.models.CarInventory;

public class CarInventoryTest {

    @Test
    public void testAddCar() {
        CarInventory carInventory = new CarInventory();

        Set<Car.AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(Car.AdditionalFeatures.GPS);
        additionalFeatures.add(Car.AdditionalFeatures.LEATHER_INTERIOR);
        additionalFeatures.add(Car.AdditionalFeatures.INSURANCE);

        Car car = new Car("Toyota", "Camry", "ABC123", "black", 2022, 1000, Car.ComfortLevel.BASIC, additionalFeatures);

        carInventory.addCar(car);

        assertTrue(carInventory.getCarMap().containsKey("ABC123"));
        assertTrue(carInventory.getCarList().contains(car));
    }

    @Test
    public void testModifyCar() {
        CarInventory carInventory = new CarInventory();

        Set<Car.AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(Car.AdditionalFeatures.GPS);

        Car originalCar = new Car("Toyota", "Camry", "ABC123", "black", 2022, 1000, Car.ComfortLevel.BASIC, additionalFeatures);
        carInventory.addCar(originalCar);

        Set<Car.AdditionalFeatures> modifiedFeatures = new HashSet<>();
        modifiedFeatures.add(Car.AdditionalFeatures.LEATHER_INTERIOR);

        Car modifiedCar = new Car("Toyota", "Camry", "ABC123", "blue", 2022, 1200, Car.ComfortLevel.LUXURY, modifiedFeatures);
        carInventory.modifyCar(modifiedCar);

        assertEquals("blue", carInventory.getCarMap().get("ABC123").getColor());
        assertEquals("blue", carInventory.getCarList().get(0).getColor());
        assertEquals(1200, carInventory.getCarMap().get("ABC123").getPrice(), 0);
    }

    @Test
    public void testRemoveCar() {
        CarInventory carInventory = new CarInventory();

        Set<Car.AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(Car.AdditionalFeatures.GPS);

        Car car = new Car("Toyota", "Camry", "ABC123", "black", 2022, 1000, Car.ComfortLevel.BASIC, additionalFeatures);
        carInventory.addCar(car);

        carInventory.removeCar("ABC123");

        assertFalse(carInventory.getCarMap().containsKey("ABC123"));
        assertFalse(carInventory.getCarList().contains(car));
    }

    @Test
    public void testSearchWithMultipleCriteria() {
        CarInventory carInventory = new CarInventory();

        Set<Car.AdditionalFeatures> additionalFeatures = new HashSet<>();
        additionalFeatures.add(Car.AdditionalFeatures.GPS);

        Car luxuryCar = new Car("Toyota", "Camry", "ABC123", "black", 2022, 1000, Car.ComfortLevel.LUXURY, additionalFeatures);
        Car standardCar = new Car("Honda", "Civic", "XYZ789", "white", 2022, 1200, Car.ComfortLevel.STANDARD, additionalFeatures);

        carInventory.addCar(luxuryCar);
        carInventory.addCar(standardCar);

        Set<Car.AdditionalFeatures> searchFeatures = new HashSet<>();
        searchFeatures.add(Car.AdditionalFeatures.GPS);

        CarInventory result = carInventory.searchWithMultipleCriteria("Toyota", null, Car.ComfortLevel.LUXURY, searchFeatures, null, null);

        assertTrue(result.getCarMap().containsKey("ABC123"));
        assertFalse(result.getCarMap().containsKey("XYZ789"));
    }
}
