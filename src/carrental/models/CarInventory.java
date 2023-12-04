package carrental.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import carrental.util.SerializationUtil;

public class CarInventory implements Serializable {
    private Map<String, Car> carMap = new HashMap<>();
    private List<Car> carList = new ArrayList<>();
    public static final long serialVersionUID = 3274432500605891315L;
    
    public Map<String, Car> getCarMap() {
        return carMap;
    }

    public List<Car> getCarList() {
        return carList;
    }

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

    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(Calendar.getInstance().getTimeZone().toZoneId()).toLocalDate();
    }

    private boolean isDateWithinInterval(LocalDate date, RentalInterval interval) {
        LocalDate startDate = dateToLocalDate(interval.getStartDate());
        LocalDate endDate = dateToLocalDate(interval.getEndDate());
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isCarRentedToday(Car car, LocalDate today) {
        for (RentalInterval interval : car.getRentalIntervals()) {
            if (isDateWithinInterval(today, interval)) {
                return true; // Car is rented today
            }
        }
        return false; // Car is not rented today
    }

    private CarInventory getFilteredCarsInventory(Predicate<Car> condition) {
        CarInventory filteredCarsInventory = new CarInventory();

        for (Car car : carList) {
            if (condition.test(car)) {
                filteredCarsInventory.addCar(car);
            }
        }

        return filteredCarsInventory;
    }

    public CarInventory getAvailableCarsInventoryToday() {
        Predicate<Car> isAvailableToday = car -> !isCarRentedToday(car, LocalDate.now());
        return getFilteredCarsInventory(isAvailableToday);
    }

    public CarInventory getUnavailableCarsInventoryToday() {
        Predicate<Car> isUnavailableToday = car -> isCarRentedToday(car, LocalDate.now());
        return getFilteredCarsInventory(isUnavailableToday);
    }

    public boolean rentCar(Car selectedCar, Date startDate, Date endDate) {
        if (selectedCar != null && !selectedCar.hasOverlapWithExistingIntervals(startDate, endDate)) {
    
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
    
    private void modifyIntervalEndDate(RentalInterval interval, LocalDate newEndDate) {
        interval.setEndDate(Date.from(newEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    
    public void returnCarToday(Car car) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);

        for (RentalInterval interval : car.getRentalIntervals()) {
            if (isDateWithinInterval(today, interval)) {
                modifyIntervalEndDate(interval, yesterday);
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
            LocalDate intervalStartDate = dateToLocalDate(interval.getStartDate());
            LocalDate intervalEndDate = dateToLocalDate(interval.getEndDate());

            if (!localEndDate.isBefore(intervalStartDate) && !localStartDate.isAfter(intervalEndDate)) {
                return true; // Car is rented within the specified interval
            }
        }
        return false; // Car is not rented within the specified interval
    }

    public void serializeCarInventory(String filePath) {
        SerializationUtil.serializeObject(this, filePath); //TODO is it worth to get rid of this method?
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
}
