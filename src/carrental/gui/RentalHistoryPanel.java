package carrental.gui;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import carrental.models.Car;
import carrental.models.RentalHistory;
import carrental.models.RentalRecord;

public class RentalHistoryPanel extends JPanel {
    private JTextArea textArea;

    public RentalHistoryPanel(RentalHistory rentalHistory) {
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        printRentalHistoryForOneCustomer(rentalHistory);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void printRentalHistoryForOneCustomer(RentalHistory rentalHistory) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
        Map<String, List<RentalRecord>> customerRentalMap = rentalHistory.getCustomerRentalMap();
    
        if (!customerRentalMap.isEmpty()) {
            // Assuming there is only one customer in the map
            Map.Entry<String, List<RentalRecord>> entry = customerRentalMap.entrySet().iterator().next();
            List<RentalRecord> rentalRecords = entry.getValue();
    
            textArea.append("Rental history:\n");
            for (RentalRecord rentalRecord : rentalRecords) {
                Car rentedCar = rentalRecord.getRentedCar();
                if (rentalRecord.getCancelled()) {
                    textArea.append("   Car: " + rentedCar.getManufacturer() +
                            " " + rentedCar.getModel() +
                            ", Base Price: " + rentedCar.getPrice() +
                            ", Date: " + dateFormat.format(rentalRecord.getTransactionDate()) +
                            ", Total Price: " + rentalRecord.getTotalPrice() +
                            ", Status: Cancelled\n");
                } else {
                    textArea.append("   Car: " + rentedCar.getManufacturer() +
                            " " + rentedCar.getModel() +
                            ", Base Price: " + rentedCar.getPrice() +
                            ", Date: " + dateFormat.format(rentalRecord.getTransactionDate()) +
                            ", Total Price: " + rentalRecord.getTotalPrice() + "\n");
                }
            }
        } else {
            textArea.append("No rental history found for the customer.\n");
        }
    }

}