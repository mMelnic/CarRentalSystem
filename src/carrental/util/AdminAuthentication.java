package carrental.util;

import java.util.HashMap;
import java.util.Map;

import carrental.exceptions.AccountCreationException;
import carrental.models.Administrator;

public class AdminAuthentication {
    private static Map<String, Administrator> adminDatabase = new HashMap<>();
    private static final String ADMIN_DATABASE_FILE = "admin_database.ser";

    private AdminAuthentication() {}

    public static void createUser(Administrator admin) throws AccountCreationException {
        if (!admin.isValidUser()) {
            throw new AccountCreationException("Invalid user. Account not created.");
        }

        String username = admin.getUsername();

        if (adminDatabase.containsKey(username)) {
            throw new AccountCreationException(
                    "Username '" + username + "' is already taken. Please choose a different username.");
        }

        adminDatabase.put(username, admin);
        saveAdminDatabaseToFile();
    }

    public static Administrator authenticateUser(String username, String password, String email) {
        Administrator user = adminDatabase.get(username);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            return user;
        }
        return null; // Authentication failed
    }

    public static void saveAdminDatabaseToFile() {
        SerializationUtil.serializeObject(adminDatabase, ADMIN_DATABASE_FILE);
    }

    public static void loadAdminDatabaseFromFile() {
        SerializationUtil.loadDatabaseFromFile(ADMIN_DATABASE_FILE, adminDatabase);
    }
}
