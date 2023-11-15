package carrental.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import carrental.exceptions.AccountCreationException;
import carrental.models.User;

public class CustomerAuthentication {
    private static final Map<String, User> customerDatabase = new HashMap<>();
    private static final String CUSTOMER_DATABASE_FILE = "customer_database.ser";
    private static final Logger logger = Logger.getLogger(CustomerAuthentication.class.getName());

    // Private constructor to prevent instantiation
    private CustomerAuthentication() {}

    public static void createUser(User user) throws AccountCreationException {
        if (!user.isValidUser()) {
            throw new AccountCreationException("Invalid user. Account not created.");
        }

        customerDatabase.put(user.getUsername(), user);
        saveCustomerDatabaseToFile();
    }

    public static User authenticateUser(String username, String password, String email) {
        User user = customerDatabase.get(username);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            return user;
        }
        return null; // Authentication failed
    }

    public static void saveCustomerDatabaseToFile() {
        Serialization.serializeObject(customerDatabase, CUSTOMER_DATABASE_FILE);
    }

    public static void loadCustomerDatabaseFromFile() {
        try {
            Object loadedObject = Serialization.deserializeObject(CUSTOMER_DATABASE_FILE);
        
            if (loadedObject instanceof Map) {
                customerDatabase.clear();
                customerDatabase.putAll((Map<String, User>) loadedObject);
            } else {
                logger.severe("Loaded object is null or not an instance of Map");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading customer database from file", e);
        }
    }
}
