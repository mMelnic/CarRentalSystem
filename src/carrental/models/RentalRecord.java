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
    private double lateFee;
    private Date returnDate;
    private static final long serialVersionUID = 6521755512973389801L;

    public RentalRecord(Car rentedCar, Customer rentingCustomer, double totalPrice, Date returnDate) {
        this.rentedCar = rentedCar;
        this.rentingCustomer = rentingCustomer;
        this.totalPrice = totalPrice;
        this.transactionDate = new Date(); // Current date
        this.rentId = UUID.randomUUID();
        this.returnDate = returnDate;
        lateFee = 0;
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

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

}

