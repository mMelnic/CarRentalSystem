package carrental.models;


public class Administrator extends User{
    // todo maybe move administrator related functionality from carInventory here
    public Administrator(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }
    
    public void addCar(Car newCar, CarInventory carInventory) {
        carInventory.addCar(newCar);
    }

    public void modifyCar(Car car, CarInventory carInventory) {
        carInventory.modifyCar(car);
    }

    public void removeCar(String registrationInfo, CarInventory carInventory) {
        carInventory.removeCar(registrationInfo);
    }
}
