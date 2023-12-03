package carrental.util;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class Serialization {
    private static final Logger logger = Logger.getLogger(Serialization.class.getName());

    // Private constructor to prevent instantiation
    private Serialization() {}

    public static void serializeObject(Object obj, String filePath) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserializeObject(String filePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return inputStream.readObject();
        } catch (EOFException eofException) {
            logger.warning("Reached end of file unexpectedly: " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserializeObject(String filePath, Class<T> myClass) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = inputStream.readObject();
            if (myClass.isInstance(obj)) {
                return myClass.cast(obj);
            } else {
                System.out.println("Invalid file content. Unable to deserialize " + myClass.getSimpleName());
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new " + myClass.getSimpleName() + ".");
            try {
                return myClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
