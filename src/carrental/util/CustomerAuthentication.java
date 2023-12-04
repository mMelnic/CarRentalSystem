package carrental.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import carrental.exceptions.AccountCreationException;
import carrental.models.BronzeCustomer;
import carrental.models.Customer;
import carrental.models.GoldCustomer;
import carrental.models.SilverCustomer;

public class CustomerAuthentication {
    private static Map<String, Customer> customerDatabase = new HashMap<>();
    private static final String CUSTOMER_DATABASE_FILE = "customer_database.ser";
    private static final Logger logger = Logger.getLogger(CustomerAuthentication.class.getName());

    // Private constructor to prevent instantiation
    private CustomerAuthentication() {}

    public static void createUser(Customer customer) throws AccountCreationException {
        if (!customer.isValidUser()) {
            throw new AccountCreationException("Invalid user. Account not created.");
        }

        customerDatabase.put(customer.getUsername(), customer);
        saveCustomerDatabaseToFile();
    }

    // todo generic method for authentication of admin and customer:
    public static Customer authenticateUser(String username, String password, String email) {
        Customer user = customerDatabase.get(username);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            return user;
        }
        return null; // Authentication failed
    }

    public static void removeUser(String username) {
        customerDatabase.remove(username);
        saveCustomerDatabaseToFile();
    }

    public static void updateUser(String username, Customer updatedCustomer) {
        // Check if the username exists in the database before updating
        if (customerDatabase.containsKey(username)) {
            // Update the customer information with the values from the updatedCustomer
            customerDatabase.put(username, updatedCustomer);
            saveCustomerDatabaseToFile();
        } else {
            logger.warning("Username not found. Cannot update user.");
        }
    }
    
    // todo generic method for upgrade and downgrade
    public static Customer upgradeCustomerToBronze(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer != null && !(customer instanceof BronzeCustomer)) {
            BronzeCustomer bronzeCustomer = new BronzeCustomer(
                customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail()
            );
            updateUser(username, bronzeCustomer);
            return bronzeCustomer;
        }
        return customer;
    }

    public static Customer upgradeCustomerToSilver(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer != null && !(customer instanceof SilverCustomer)) {
            SilverCustomer silverCustomer = new SilverCustomer(
                customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail()
            );
            updateUser(username, silverCustomer);
            return silverCustomer;
        }

        return customer;
    }

    public static Customer upgradeCustomerToGold(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer != null && !(customer instanceof GoldCustomer)) {
            GoldCustomer goldCustomer = new GoldCustomer(
                customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail()
            );
            updateUser(username, goldCustomer);
            return goldCustomer;
        }

        return customer;
    }

    public static Customer downgradeGoldToSilver(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer instanceof GoldCustomer) {
            SilverCustomer silverCustomer = new SilverCustomer(
                    customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail());
            updateUser(username, silverCustomer);
            return silverCustomer;
        }

        return customer;
    }

    public static Customer downgradeSilverToBronze(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer instanceof SilverCustomer) {
            BronzeCustomer bronzeCustomer = new BronzeCustomer(
                    customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail());
            updateUser(username, bronzeCustomer);
            return bronzeCustomer;
        }

        return customer;
    }

    public static Customer downgradeBronzeToRegular(String username) {
        Customer customer = customerDatabase.get(username);
        if (customer instanceof BronzeCustomer) {
            Customer regularCustomer = new Customer(
                    customer.getUsername(), customer.getPassword(), customer.getFullName(), customer.getEmail());
            updateUser(username, regularCustomer);
            return regularCustomer;
        }
        return customer;
    }

    public static void saveCustomerDatabaseToFile() {
        Serialization.serializeObject(customerDatabase, CUSTOMER_DATABASE_FILE);
    }

    public static void loadCustomerDatabaseFromFile() {
        Serialization.loadDatabaseFromFile(CUSTOMER_DATABASE_FILE, customerDatabase);
    }
}
