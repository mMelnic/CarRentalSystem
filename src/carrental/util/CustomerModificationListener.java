package carrental.util;

import carrental.models.Customer;

public interface CustomerModificationListener {
    void onCustomerModified(Customer modifiedCustomer);
}

