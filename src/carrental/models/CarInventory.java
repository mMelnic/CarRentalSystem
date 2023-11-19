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

    public List<Car> filterCarsByModel(String model) {
        List<Car> filteredCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getModel().equalsIgnoreCase(model)) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
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
}
