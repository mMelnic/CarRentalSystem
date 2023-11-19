package carrental.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import carrental.models.Car.AdditionalFeatures;

public class CarInventory {
    private Map<String, Car> carMap = new HashMap<>();
    private List<Car> carList = new ArrayList<>();
    private Set<Car> carSetSortedByPrice = new TreeSet<>(Comparator.comparingDouble(Car::getPrice));

    public void addCar(Car car) {
        carMap.put(car.getRegistrationInfo(), car);
        carList.add(car);
        carSetSortedByPrice.add(car);
    }

    public void modifyCar(Car modifiedCar) {
        // Check if the car with the specified registration info exists
        if (carMap.containsKey(modifiedCar.getRegistrationInfo())) {
            // Remove the existing car from the set
            carSetSortedByPrice.remove(carMap.get(modifiedCar.getRegistrationInfo()));

            // Update the car in the map and list
            carMap.put(modifiedCar.getRegistrationInfo(), modifiedCar);
            carList.set(carList.indexOf(carMap.get(modifiedCar.getRegistrationInfo())), modifiedCar);

            // Add the modified car back to the set
            carSetSortedByPrice.add(modifiedCar);
        } else {
            System.out.println("Car not found for modification: " + modifiedCar.getRegistrationInfo());
        }
    }

    public void removeCar(String registrationInfo) {
        // Check if the car with the specified registration info exists
        if (carMap.containsKey(registrationInfo)) {
            // Remove the existing car from the set
            carSetSortedByPrice.remove(carMap.get(registrationInfo));

            // Remove the car from the map and list
            carList.remove(carMap.get(registrationInfo));
            carMap.remove(registrationInfo);
        } else {
            System.out.println("Car not found for removal: " + registrationInfo);
        }
    }

    public List<Car> filterCarsByModel(String model) {
        List<Car> filteredCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getModel().equalsIgnoreCase(model)) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }

    public Set<Car> sortCarsByPrice() {
        return carSetSortedByPrice;
    }

    public Map<String, Car> getCarMap() {
        return carMap;
    }
}
