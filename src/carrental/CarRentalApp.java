package carrental;

import javax.swing.SwingUtilities;

import carrental.gui.UserInterface;
import carrental.models.CarInventory;
import carrental.models.PricingAttributes;
import carrental.models.RentalHistory;
import carrental.util.AdminAuthentication;
import carrental.util.CustomerAuthentication;
import carrental.util.SerializationUtil;

public class CarRentalApp {
    public static void main(String[] args) {
        CarInventory loadedCarInventory = SerializationUtil.deserializeObject("carInventory.ser", CarInventory.class);
        RentalHistory entireRentalHistory = SerializationUtil.deserializeObject("rental_history.ser", RentalHistory.class);
        AdminAuthentication.loadAdminDatabaseFromFile();
        CustomerAuthentication.loadCustomerDatabaseFromFile();
        PricingAttributes charges = PricingAttributes.deserializePricingAttributes("pricingAttributes.ser");
        SwingUtilities.invokeLater(() -> new UserInterface(loadedCarInventory, entireRentalHistory, charges));
    }
}
