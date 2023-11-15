package carrental.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import carrental.exceptions.AccountCreationException;
import carrental.models.User;

public class AdminAuthentication {
    private static final Map<String, User> adminDatabase = new HashMap<>();
    private static final String ADMIN_DATABASE_FILE = "admin_database.ser";
    private static final Logger logger = Logger.getLogger(AdminAuthentication.class.getName());


    private AdminAuthentication() {}

    public static void createUser(User user) throws AccountCreationException {
        if (!user.isValidUser()) {
            throw new AccountCreationException("Invalid user. Account not created.");
        }

        adminDatabase.put(user.getUsername(), user);
        saveAdminDatabaseToFile();
    }

    public static User authenticateUser(String username, String password, String email) {
        User user = adminDatabase.get(username);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            return user;
        }
        return null; // Authentication failed
    }

    public static void saveAdminDatabaseToFile() {
        Serialization.serializeObject(adminDatabase, ADMIN_DATABASE_FILE);
    }

    public static void loadAdminDatabaseFromFile() {
        try {
            Object loadedObject = Serialization.deserializeObject(ADMIN_DATABASE_FILE);

            if (loadedObject instanceof Map) {
                adminDatabase.clear();
                adminDatabase.putAll((Map<String, User>) loadedObject);
            } else {
                logger.warning("Loaded object is not an instance of Map");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading admin database from file", e);
        }
    }
}
