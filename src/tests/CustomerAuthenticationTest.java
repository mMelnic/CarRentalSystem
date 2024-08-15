package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import carrental.exceptions.AccountCreationException;
import carrental.models.Customer;
import carrental.models.GoldCustomer;
import carrental.util.CustomerAuthentication;

public class CustomerAuthenticationTest {

    @Test
    public void testCreateValidCustomer() throws AccountCreationException {
        Customer customer = new Customer("john_doe", "password", "John Doe", "john@example.com");
        CustomerAuthentication.createUser(customer);
        assertEquals(customer, CustomerAuthentication.authenticateUser("john_doe", "password", "john@example.com"));
    }

    @Test
    public void testCreateInvalidCustomer() {
        assertThrows(AccountCreationException.class, () -> {
            Customer customer = new Customer("aa", "password1", "Invalid User 3", "invalid@example.com");
            CustomerAuthentication.createUser(customer);
        });
    }

    @Test
    public void testUpgradeToGold() {
        Customer customer = new Customer("gold_user", "password2", "Gold User", "gold@example.com");
        try {
            CustomerAuthentication.createUser(customer);
        } catch (AccountCreationException e) {
            e.printStackTrace();
        }

        Customer upgradedCustomer = CustomerAuthentication.upgradeCustomerToGold("gold_user");
        assertTrue(upgradedCustomer instanceof GoldCustomer);
    }
}
