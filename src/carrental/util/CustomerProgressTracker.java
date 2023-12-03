package carrental.util;

import java.util.HashMap;
import java.util.Map;

public class CustomerProgressTracker {

    // Map to store the progress for each customer
    private Map<String, Integer> progressMap;

    public CustomerProgressTracker() {
        this.progressMap = new HashMap<>();
    }

    public int getProgress(String customerUsername) {
        // Retrieve the progress for a specific customer
        return progressMap.getOrDefault(customerUsername, 0);
    }

    public void updateProgress(String customerUsername, int numberOfReservations) {
        // Calculate progress based on the thresholds
        int progress = calculateProgress(numberOfReservations);

        // Update the progress map
        progressMap.put(customerUsername, progress);
    }

    public String getCurrentTier(int numberOfReservations) {
        // Determine the current tier based on the number of reservations
        if (numberOfReservations >= Thresholds.SILVER_TO_GOLD.getThreshold()) {
            return "Gold";
        } else if (numberOfReservations >= Thresholds.BRONZE_TO_SILVER.getThreshold()) {
            return "Silver";
        } else if (numberOfReservations >= Thresholds.REGULAR_TO_BRONZE.getThreshold()) {
            return "Bronze";
        } else {
            return "Regular";
        }
    }

    // Calculate progress based on the thresholds
    private int calculateProgress(int numberOfReservations) {
        if (numberOfReservations >= Thresholds.SILVER_TO_GOLD.getThreshold()) {
            return 100; // Full progress for Gold
        } else if (numberOfReservations >= Thresholds.BRONZE_TO_SILVER.getThreshold()) {
            return (numberOfReservations - Thresholds.BRONZE_TO_SILVER.getThreshold()) * 100 /
                   (Thresholds.SILVER_TO_GOLD.getThreshold() - Thresholds.BRONZE_TO_SILVER.getThreshold());
        } else if (numberOfReservations >= Thresholds.REGULAR_TO_BRONZE.getThreshold()) {
            return (numberOfReservations - Thresholds.REGULAR_TO_BRONZE.getThreshold()) * 100 /
                   (Thresholds.BRONZE_TO_SILVER.getThreshold() - Thresholds.REGULAR_TO_BRONZE.getThreshold());
        } else {
            return numberOfReservations * 100 / Thresholds.REGULAR_TO_BRONZE.getThreshold();
        }
    }
}

