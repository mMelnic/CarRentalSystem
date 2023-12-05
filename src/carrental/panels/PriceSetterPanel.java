package carrental.panels;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import carrental.models.PricingAttributes;

public class PriceSetterPanel extends JPanel {
    private PricingAttributes pricingAttributes;

    private JTextField weeklyDiscountRateField;
    private JTextField monthlyDiscountRateField;
    private JTextField peakSeasonRateMultiplierField;
    private JTextField peakSeasonStartMonthField;
    private JTextField peakSeasonEndMonthField;
    private JTextField gpsServiceChargeField;
    private JTextField insuranceChargeField;
    private JTextField childSeatChargeField;
    private JTextField leatherInteriorChargeField;
    private JTextField sunroofChargeField;
    private JTextField hybridTechnologyChargeField;

    public PriceSetterPanel(PricingAttributes pricingAttributes) {
        this.pricingAttributes = pricingAttributes;

        setLayout(new GridLayout(12, 1));
        setBorder(new EmptyBorder(30, 50, 290, 140));

        add(new JLabel("Weekly Discount Rate(0-1):"));
        weeklyDiscountRateField = new JTextField();
        add(weeklyDiscountRateField);

        add(new JLabel("Monthly Discount Rate(0-1):"));
        monthlyDiscountRateField = new JTextField();
        add(monthlyDiscountRateField);

        add(new JLabel("Peak Season Rate Multiplier:"));
        peakSeasonRateMultiplierField = new JTextField();
        add(peakSeasonRateMultiplierField);

        add(new JLabel("Peak Season Start Month:"));
        peakSeasonStartMonthField = new JTextField();
        add(peakSeasonStartMonthField);

        add(new JLabel("Peak Season End Month:"));
        peakSeasonEndMonthField = new JTextField();
        add(peakSeasonEndMonthField);

        add(new JLabel("GPS Service Charge:"));
        gpsServiceChargeField = new JTextField();
        add(gpsServiceChargeField);

        add(new JLabel("Insurance Charge:"));
        insuranceChargeField = new JTextField();
        add(insuranceChargeField);

        add(new JLabel("Child Seat Charge:"));
        childSeatChargeField = new JTextField();
        add(childSeatChargeField);

        add(new JLabel("Leather Interior Charge:"));
        leatherInteriorChargeField = new JTextField();
        add(leatherInteriorChargeField);

        add(new JLabel("Sunroof Charge:"));
        sunroofChargeField = new JTextField();
        add(sunroofChargeField);

        add(new JLabel("Hybrid Technology Charge:"));
        hybridTechnologyChargeField = new JTextField();
        add(hybridTechnologyChargeField);
        add(new JLabel());

        if (pricingAttributes != null) {
            populateFields();
        }

        JButton setValuesButton = new JButton("Set Values");
        setValuesButton.addActionListener(e -> {
            if (validateFields()) {
                setPricingAttributes();
                pricingAttributes.serializePricingAttributes("pricingAttributes.ser");
                JOptionPane.showMessageDialog(null, "Values set successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields with correct values.");
            }
        });
        add(setValuesButton);
    }

    private void setPricingAttributes() {
        // Check if pricingAttributes is null and instantiate it if necessary
        if (pricingAttributes == null) {
            pricingAttributes = new PricingAttributes();
        }

        // Setting the attributes
        pricingAttributes.setWeeklyDiscountRate(parseDouble(weeklyDiscountRateField.getText()));
        pricingAttributes.setMonthlyDiscountRate(parseDouble(monthlyDiscountRateField.getText()));
        pricingAttributes.setPeakSeasonRateMultiplier(parseDouble(peakSeasonRateMultiplierField.getText()));
        pricingAttributes.setPeakSeasonStartMonth(parseInt(peakSeasonStartMonthField.getText()));
        pricingAttributes.setPeakSeasonEndMonth(parseInt(peakSeasonEndMonthField.getText()));
        pricingAttributes.setGpsServiceCharge(parseDouble(gpsServiceChargeField.getText()));
        pricingAttributes.setInsuranceCharge(parseDouble(insuranceChargeField.getText()));
        pricingAttributes.setChildSeatCharge(parseDouble(childSeatChargeField.getText()));
        pricingAttributes.setLeatherInteriorCharge(parseDouble(leatherInteriorChargeField.getText()));
        pricingAttributes.setSunroofCharge(parseDouble(sunroofChargeField.getText()));
        pricingAttributes.setHybridTechnologyCharge(parseDouble(hybridTechnologyChargeField.getText()));
    }

    private void populateFields() {
        weeklyDiscountRateField.setText(String.valueOf(pricingAttributes.getWeeklyDiscountRate()));
        monthlyDiscountRateField.setText(String.valueOf(pricingAttributes.getMonthlyDiscountRate()));
        peakSeasonRateMultiplierField.setText(String.valueOf(pricingAttributes.getPeakSeasonRateMultiplier()));
        peakSeasonStartMonthField.setText(String.valueOf(pricingAttributes.getPeakSeasonStartMonth()));
        peakSeasonEndMonthField.setText(String.valueOf(pricingAttributes.getPeakSeasonEndMonth()));
        gpsServiceChargeField.setText(String.valueOf(pricingAttributes.getGpsServiceCharge()));
        insuranceChargeField.setText(String.valueOf(pricingAttributes.getInsuranceCharge()));
        childSeatChargeField.setText(String.valueOf(pricingAttributes.getChildSeatCharge()));
        leatherInteriorChargeField.setText(String.valueOf(pricingAttributes.getLeatherInteriorCharge()));
        sunroofChargeField.setText(String.valueOf(pricingAttributes.getSunroofCharge()));
        hybridTechnologyChargeField.setText(String.valueOf(pricingAttributes.getHybridTechnologyCharge()));
    }

    private boolean validateFields() {
        try {
            // Parse discount rates from text fields
            double weeklyDiscountRate = Double.parseDouble(weeklyDiscountRateField.getText());
            double monthlyDiscountRate = Double.parseDouble(monthlyDiscountRateField.getText());

            // Check if discount rates are between 0 and 1
            boolean areDiscountRatesValid = (weeklyDiscountRate >= 0 && weeklyDiscountRate <= 1) &&
                    (monthlyDiscountRate >= 0 && monthlyDiscountRate <= 1);

            // Check other non-empty fields
            if (!areDiscountRatesValid) {
                // Display an error message to the user
                showMessageDialog("Discount rates must be between 0 and 1.");
                return false;
            }

            return !peakSeasonRateMultiplierField.getText().isEmpty() &&
                    !peakSeasonStartMonthField.getText().isEmpty() &&
                    !peakSeasonEndMonthField.getText().isEmpty() &&
                    !gpsServiceChargeField.getText().isEmpty() &&
                    !insuranceChargeField.getText().isEmpty() &&
                    !childSeatChargeField.getText().isEmpty() &&
                    !leatherInteriorChargeField.getText().isEmpty() &&
                    !sunroofChargeField.getText().isEmpty() &&
                    !hybridTechnologyChargeField.getText().isEmpty();
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails (e.g., non-numeric input)
            showMessageDialog("Invalid input. Please enter numeric values for discount rates.");
            return false;
        }
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            // Handle parsing error, e.g., show an error message
            return 0.0;
        }
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Handle parsing error, e.g., show an error message
            return 0;
        }
    }
}
