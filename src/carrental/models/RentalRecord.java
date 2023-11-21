package carrental.models;

import java.util.Date;

public class RentalRecord {
    private Car rentedCar;
    private Customer rentingCustomer;
    private double totalPrice;
    private Date transactionDate;

    public RentalRecord(Car rentedCar, Customer rentingCustomer, double totalPrice) {
        this.rentedCar = rentedCar;
        this.rentingCustomer = rentingCustomer;
        this.totalPrice = totalPrice;
        this.transactionDate = new Date(); // Current date
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
}

