package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import carrental.util.Serialization;

public class CarInventory implements Serializable {
    private Map<String, Car> carMap = new HashMap<>();
    private List<Car> carList = new ArrayList<>();

    public void addCar(Car car) {
        carMap.put(car.getRegistrationInfo(), car);
        carList.add(car);
    }

    public void modifyCar(Car modifiedCar) {
        // Check if the car with the specified registration info exists
        if (carMap.containsKey(modifiedCar.getRegistrationInfo())) {
            // Update the car in the map and list
            carMap.put(modifiedCar.getRegistrationInfo(), modifiedCar);
            carList.set(carList.indexOf(carMap.get(modifiedCar.getRegistrationInfo())), modifiedCar);

        } else {
            System.out.println("Car not found for modification: " + modifiedCar.getRegistrationInfo());
        }
    }

    public void removeCar(String registrationInfo) {
        // Check if the car with the specified registration info exists
        if (carMap.containsKey(registrationInfo)) {
            // Remove the car from the map and list
            carList.remove(carMap.get(registrationInfo));
            carMap.remove(registrationInfo);
        } else {
            System.out.println("Car not found for removal: " + registrationInfo);
        }
    }

    public Map<String, Car> getCarMap() {
        return carMap;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void serializeCarInventory(String filePath) {
        Serialization.serializeObject(this, filePath);
    }

    public static CarInventory deserializeCarInventory(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = inputStream.readObject();
            if (obj instanceof CarInventory) {
                return (CarInventory) obj;
            } else {
                System.out.println("Invalid file content. Unable to deserialize CarInventory.");
                return null;
            }
        } catch (FileNotFoundException e) {
            // Handle the case where the file does not exist
            System.out.println("File not found. Creating a new CarInventory.");
            return new CarInventory(); // Or create a new CarInventory instance
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CarInventory getRentedCars() {
        CarInventory rentedCarsInventory = new CarInventory();
        for (Car car : carList) {
            if (car.getRentedStatus()) {
                rentedCarsInventory.addCar(car);
            }
        }
        return rentedCarsInventory;
    }

    public CarInventory getUnrentedCars() {
        CarInventory unrentedCarsInventory = new CarInventory();
        for (Car car : carList) {
            if (!car.getRentedStatus()) {
                unrentedCarsInventory.addCar(car);
            }
        }
        return unrentedCarsInventory;
    }

    public CarInventory searchWithMultipleCriteria(String manufacturer, String model, Car.ComfortLevel comfortLevel, Set<Car.AdditionalFeatures> features) {
        CarInventory filteredCarsInventory = new CarInventory();
    
        carList.stream()
                .filter(car -> (manufacturer == null || manufacturer.isEmpty() || car.getManufacturer().equalsIgnoreCase(manufacturer))
                        && (model == null || model.isEmpty() || car.getModel().equalsIgnoreCase(model))
                        && (comfortLevel == null || car.getComfortLevel() == comfortLevel)
                        && (features == null || features.isEmpty() || car.getAdditionalFeatures().containsAll(features)))
                .forEach(filteredCarsInventory::addCar);
    
        return filteredCarsInventory;
    }

    public boolean rentCar(Car selectedCar) {
        if (selectedCar != null && !selectedCar.getRentedStatus()) {
            selectedCar.setRentedStatus(true);
    
            // Updating the carList as well
            int index = carList.indexOf(selectedCar);
            if (index != -1) {
                carList.set(index, selectedCar);
                return true; // Operation successful
            }
        }
        return false; // Operation failed    
    }    
    
    public void returnCar(Car car) {
        if (car != null && car.getRentedStatus()) {
            // Update the rental status to false
            car.setRentedStatus(false);

            // Update the car in the map
            carMap.put(car.getRegistrationInfo(), car);

            int index = carList.indexOf(car);
            if (index != -1) {
                carList.set(index, car);
            }
        }
        // If the car is not rented or not found, no action is taken
    }
}
