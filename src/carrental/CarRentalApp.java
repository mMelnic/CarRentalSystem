package carrental;

import javax.swing.SwingUtilities;

import carrental.gui.UserInterface;
import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.util.AdminAuthentication;
import carrental.util.CustomerAuthentication;

public class CarRentalApp {
    public static void main(String[] args) {
        CarInventory loadedCarInventory = CarInventory.deserializeCarInventory("carInventory.ser");
        RentalHistory entireRentalHistory = RentalHistory.loadFromFile("rental_history.ser");
        AdminAuthentication.loadAdminDatabaseFromFile();
        CustomerAuthentication.loadCustomerDatabaseFromFile();
        PricingAttributes charges = PricingAttributes.deserializePricingAttributes("pricingAttributes.ser");
        SwingUtilities.invokeLater(() -> new UserInterface(loadedCarInventory, entireRentalHistory, charges));
    }
}
