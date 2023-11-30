package carrental.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class RentalRecord implements Serializable{
    private Car rentedCar;
    private Customer rentingCustomer;
    private double totalPrice;
    private Date transactionDate;
    private UUID rentId;
    private boolean isCancelled;
    private static final long serialVersionUID = 6521755512973389801L;

    public RentalRecord(Car rentedCar, Customer rentingCustomer, double totalPrice) {
        this.rentedCar = rentedCar;
        this.rentingCustomer = rentingCustomer;
        this.totalPrice = totalPrice;
        this.transactionDate = new Date(); // Current date
        this.rentId = UUID.randomUUID();
        isCancelled = false;
    }

    // Getters and setters for the fields

    public Car getRentedCar() {
        return rentedCar;
    }

    public void setRentedCar(Car rentedCar) {
        this.rentedCar = rentedCar;
    }

    public Customer getRentingCustomer() {
        return rentingCustomer;
    }

    public void setRentingCustomer(Customer rentingCustomer) {
        this.rentingCustomer = rentingCustomer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public UUID getRentId() {
        return rentId;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean getCancelled() {
        return isCancelled;
    }

    public boolean isModificationAllowed() {
        // Check if the modification is allowed
        if (rentedCar != null) {
            return rentedCar.isModificationAllowed(rentId);
        }
        return false; // Modification not allowed if rentedCar is null
    }

}

