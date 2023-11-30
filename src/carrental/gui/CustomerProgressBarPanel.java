package carrental.gui;

import javax.swing.*;

import carrental.util.CustomerProgressTracker;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerProgressBarPanel extends JPanel {
    private CustomerProgressTracker progressTracker;
    private String customerUsername;
    private int numberOfReservations;

    private JProgressBar progressBar;

    public CustomerProgressBarPanel(CustomerProgressTracker progressTracker, String customerUsername, int numberOfReservations) {
        this.progressTracker = progressTracker;
        this.customerUsername = customerUsername;
        this.numberOfReservations = numberOfReservations;

        initializeComponents();
        updateProgressBar();
        startAutoUpdate(); // Optionally start a timer for automatic progress updates
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 20));

        add(progressBar, BorderLayout.CENTER);
    }

    private void updateProgressBar() {
        int progress = progressTracker.getProgress(customerUsername);
        progressBar.setValue(progress);

        String tier = progressTracker.getCurrentTier(numberOfReservations);
        progressBar.setString("Tier: " + tier + " - Progress: " + progress + "%");
    }

    private void startAutoUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the progress bar periodically
                updateProgressBar();
            }
        }, 0, 1000); // Update every 1000 milliseconds (1 second)
    }

}

