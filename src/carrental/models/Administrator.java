package carrental.models;


public class Administrator extends User{
    public Administrator(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }
    
    public void addCar(Car newCar, CarInventory carInventory) {
        // Administrator-specific logic for adding a car
        carInventory.addCar(newCar);
    }

    public void modifyCar(Car car, CarInventory carInventory) {
        // Administrator-specific logic for modifying a car
        carInventory.modifyCar(car);
    }

    public void removeCar(String registrationInfo, CarInventory carInventory) {
        // Administrator-specific logic for removing a car
        carInventory.removeCar(registrationInfo);
    }
}
