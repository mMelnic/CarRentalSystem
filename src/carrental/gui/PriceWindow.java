package carrental.gui;

import javax.swing.*;

import carrental.models.PricingAttributes;
import carrental.models.Car.AdditionalFeatures;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;

public class PriceWindow extends JFrame {

    public PriceWindow(double basePrice, double durationBasedPrice, double additionalServicesPrice, double finalPrice, Set<AdditionalFeatures> additionalFeatures, PricingAttributes pricingAttributes) {
        setTitle("Receipt");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a panel with a vertical layout
        JPanel panel = new JPanel(new BorderLayout());
        JPanel invoicePanel = new JPanel();
        invoicePanel.setLayout(new BoxLayout(invoicePanel, BoxLayout.Y_AXIS));

        // Add components to the invoice panel
        addLabel(invoicePanel, "Base Price: " + formatPrice(basePrice) + " Ft/day");
        addLabel(invoicePanel, "Duration Based Price: " + formatPrice(durationBasedPrice) + " Ft");

        if (additionalServicesPrice > 0) {
            addLabel(invoicePanel, "Additional Services Breakdown:");
            addAdditionalServicesLabels(invoicePanel, additionalFeatures, pricingAttributes);
            addLabel(invoicePanel, "-------------------------------");
            addLabel(invoicePanel, "Additional Services Price: " + formatPrice(additionalServicesPrice) + " Ft");
        }

        addLabel(invoicePanel, "-------------------------------");
        addLabel(invoicePanel, "Total Price: " + formatPrice(finalPrice) + " Ft");

        // Add the invoice panel to the main panel
        panel.add(invoicePanel, BorderLayout.CENTER);

        // Add a "Print Invoice" button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up the frame
        getContentPane().add(panel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(label);
    }

    private void addAdditionalServicesLabels(JPanel panel, Set<AdditionalFeatures> additionalFeatures, PricingAttributes pricingAttributes) {
        for (AdditionalFeatures feature : additionalFeatures) {
            double serviceCharge = getServiceCharge(feature, pricingAttributes);
            addLabel(panel, feature + ": " + formatPrice(serviceCharge) + " Ft");
        }
    }

    private double getServiceCharge(AdditionalFeatures feature, PricingAttributes pricingAttributes) {
        switch (feature) {
            case GPS:
                return pricingAttributes.getGpsServiceCharge();
            case CHILD_SEAT:
                return pricingAttributes.getChildSeatCharge();
            case INSURANCE:
                return pricingAttributes.getInsuranceCharge();
            case LEATHER_INTERIOR:
                return pricingAttributes.getLeatherInteriorCharge();
            case SUNROOF:
                return pricingAttributes.getSunroofCharge();
            case HYBRID_TECHNOLOGY:
                return pricingAttributes.getHybridTechnologyCharge();
            default:
                return 0.0;
        }
    }

    private String formatPrice(double price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(price);
    }
}

