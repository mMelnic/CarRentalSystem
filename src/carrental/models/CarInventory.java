package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import carrental.util.Serialization;

public class CarInventory implements Serializable {
    private Map<String, Car> carMap = new HashMap<>();
    private List<Car> carList = new ArrayList<>();
    public static final long serialVersionUID = 3274432500605891315L;

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
                return (CarInventory) obj;   //TODO use deserialization method from Serialization class
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

    public CarInventory getAvailableCarsInventoryToday() {
        CarInventory availableCarsInventory = new CarInventory();

        LocalDate today = LocalDate.now();

        for (Car car : carList) {
            if (!isCarRentedToday(car, today)) {
                availableCarsInventory.addCar(car);
            }
        }

        return availableCarsInventory;
    }

    public CarInventory getUnavailableCarsInventoryToday() {
        CarInventory unavailableCarsInventory = new CarInventory();

        LocalDate today = LocalDate.now();

        for (Car car : carList) {
            if (isCarRentedToday(car, today)) {
                unavailableCarsInventory.addCar(car);
            }
        }

        return unavailableCarsInventory;
    }


    private boolean isCarRentedToday(Car car, LocalDate today) {
        for (RentalInterval interval : car.getRentalIntervals()) {
            LocalDate startDate = interval.getStartDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
            LocalDate endDate = interval.getEndDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();

            if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
                return true; // Car is rented today
            }
        }
        return false; // Car is not rented today
    }

    public CarInventory searchWithMultipleCriteria(String manufacturer, String model, Car.ComfortLevel comfortLevel,
            Set<Car.AdditionalFeatures> features, Date startDate, Date endDate) {

        CarInventory filteredCarsInventory = new CarInventory();

        carList.stream()
                .filter(car -> (manufacturer == null || manufacturer.isEmpty() || car.getManufacturer().equalsIgnoreCase(manufacturer))
                        && (model == null || model.isEmpty() || car.getModel().equalsIgnoreCase(model))
                        && (comfortLevel == null || car.getComfortLevel() == comfortLevel)
                        && (features == null || features.isEmpty() || car.getAdditionalFeatures().containsAll(features))
                        && !isCarRentedInInterval(car, startDate, endDate))
                .forEach(filteredCarsInventory::addCar);

        return filteredCarsInventory;
    }
    private boolean isCarRentedInInterval(Car car, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false; // If either startDate or endDate is null, the car is not rented within the specified interval
        }

        LocalDate localStartDate = dateToLocalDate(startDate);
        LocalDate localEndDate = dateToLocalDate(endDate);

        for (RentalInterval interval : car.getRentalIntervals()) {
            LocalDate intervalStartDate = interval.getStartDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
            LocalDate intervalEndDate = interval.getEndDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();

            if (!localEndDate.isBefore(intervalStartDate) && !localStartDate.isAfter(intervalEndDate)) {
                return true; // Car is rented within the specified interval
            }
        }
        return false; // Car is not rented within the specified interval
    }

    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
    }

    public boolean rentCar(Car selectedCar, Date startDate, Date endDate) {
        if (selectedCar != null && !selectedCar.hasOverlap(startDate, endDate)) {
    
            // Updating the carList as well
            int index = carList.indexOf(selectedCar);
            if (index != -1) {
                carList.set(index, selectedCar);
                carMap.put(selectedCar.getRegistrationInfo(), selectedCar);
                return true; // Operation successful
            }
        }
        return false; // Operation failed    
    }
    
    public void returnCarToday(Car car) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

        for (RentalInterval interval : car.getRentalIntervals()) {
            LocalDate intervalStartDate = interval.getStartDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
            LocalDate intervalEndDate = interval.getEndDate().toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();

            // Check if today's date is within the rental interval
            if (!today.isBefore(intervalStartDate) && !today.isAfter(intervalEndDate)) {
                // Modify the end date to yesterday's date
                interval.setEndDate(Date.from(yesterday.atStartOfDay(Calendar.getInstance().getTimeZone().toZoneId()).toInstant()));
                return; // End the loop after modifying the interval
            }
        }
    }

    public boolean modifyReservation(String registrationInfo, UUID rentId, Date newStartDate, Date newEndDate) {
        Car car = carMap.get(registrationInfo);
        if (car != null) {
            return car.modifyReservation(rentId, newStartDate, newEndDate);
        }
        return false;
    }
}
