package carrental;

import javax.swing.SwingUtilities;

import carrental.gui.UserInterface;
import carrental.util.AdminAuthentication;
import carrental.util.CustomerAuthentication;

public class CarRentalApp {
    public static void main(String[] args) {
        AdminAuthentication.loadAdminDatabaseFromFile();
        CustomerAuthentication.loadCustomerDatabaseFromFile();
        SwingUtilities.invokeLater(UserInterface::new);
    }
}
