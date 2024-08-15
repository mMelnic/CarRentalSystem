package carrental.gui;

import javax.swing.*;

import carrental.util.CustomerProgressTracker;

import java.awt.*;

public class CustomerProgressBarPanel extends JPanel {
    private transient CustomerProgressTracker progressTracker;
    private String customerUsername;
    private int numberOfReservations;

    private JProgressBar progressBar;

    public CustomerProgressBarPanel(CustomerProgressTracker progressTracker, String customerUsername, int numberOfReservations) {
        this.progressTracker = progressTracker;
        this.customerUsername = customerUsername;
        this.numberOfReservations = numberOfReservations;

        initializeComponents();
        updateProgressBar();
    }

    public void setNumberOfReservations(int numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 20));

        add(progressBar, BorderLayout.CENTER);
    }

    void updateProgressBar() {
        int progress = progressTracker.getProgress(customerUsername);
        progressBar.setValue(progress);

        String tier = progressTracker.getCurrentTier(numberOfReservations);
        progressBar.setString("Tier: " + tier + " - Progress: " + progress + "%");
    }

}
