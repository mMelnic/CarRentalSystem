package carrental;

import javax.swing.SwingUtilities;

import carrental.gui.UserInterface;
import carrental.models.CarInventory;
import carrental.util.AdminAuthentication;
import carrental.util.CustomerAuthentication;

public class CarRentalApp {
    public static void main(String[] args) {
        CarInventory carInventory = new CarInventory();
        AdminAuthentication.loadAdminDatabaseFromFile();
        CustomerAuthentication.loadCustomerDatabaseFromFile();
        SwingUtilities.invokeLater(() -> new UserInterface(carInventory));
    }
}
