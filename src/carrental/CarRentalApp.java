package carrental;

import javax.swing.SwingUtilities;

import carrental.gui.UserInterface;
import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.util.AdminAuthentication;
import carrental.util.CustomerAuthentication;
import carrental.util.Serialization;

public class CarRentalApp {
    public static void main(String[] args) {
        CarInventory loadedCarInventory = Serialization.deserializeObject("carInventory.ser", CarInventory.class);
        RentalHistory entireRentalHistory = Serialization.deserializeObject("rental_history.ser", RentalHistory.class);
        AdminAuthentication.loadAdminDatabaseFromFile();
        CustomerAuthentication.loadCustomerDatabaseFromFile();
        PricingAttributes charges = PricingAttributes.deserializePricingAttributes("pricingAttributes.ser");
        SwingUtilities.invokeLater(() -> new UserInterface(loadedCarInventory, entireRentalHistory, charges));
    }
}
