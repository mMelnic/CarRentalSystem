package carrental.util;

import java.util.HashMap;
import java.util.Map;

public class CustomerProgressTracker {
    private static final int REGULAR_TO_BRONZE_THRESHOLD = 5; // Adjust as needed
    private static final int BRONZE_TO_SILVER_THRESHOLD = 10; // Adjust as needed
    private static final int SILVER_TO_GOLD_THRESHOLD = 15; // Adjust as needed

    // Map to store the progress for each customer
    private Map<String, Integer> progressMap;

    public CustomerProgressTracker() {
        this.progressMap = new HashMap<>();
    }

    public void updateProgress(String customerUsername, int numberOfReservations) {
        // Calculate progress based on the thresholds
        int progress = calculateProgress(numberOfReservations);

        // Update the progress map
        progressMap.put(customerUsername, progress);
    }

    public int getProgress(String customerUsername) {
        // Retrieve the progress for a specific customer
        return progressMap.getOrDefault(customerUsername, 0);
    }

    public String getCurrentTier(int numberOfReservations) {
        // Determine the current tier based on the number of reservations
        if (numberOfReservations >= SILVER_TO_GOLD_THRESHOLD) {
            return "Gold";
        } else if (numberOfReservations >= BRONZE_TO_SILVER_THRESHOLD) {
            return "Silver";
        } else if (numberOfReservations >= REGULAR_TO_BRONZE_THRESHOLD) {
            return "Bronze";
        } else {
            return "Regular";
        }
    }

    // Calculate progress based on the thresholds
    private int calculateProgress(int numberOfReservations) {
        if (numberOfReservations >= SILVER_TO_GOLD_THRESHOLD) {
            return 100; // Full progress for Gold
        } else if (numberOfReservations >= BRONZE_TO_SILVER_THRESHOLD) {
            return (numberOfReservations - BRONZE_TO_SILVER_THRESHOLD) * 100 / (SILVER_TO_GOLD_THRESHOLD - BRONZE_TO_SILVER_THRESHOLD);
        } else if (numberOfReservations >= REGULAR_TO_BRONZE_THRESHOLD) {
            return (numberOfReservations - REGULAR_TO_BRONZE_THRESHOLD) * 100 / (BRONZE_TO_SILVER_THRESHOLD - REGULAR_TO_BRONZE_THRESHOLD);
        } else {
            return numberOfReservations * 100 / REGULAR_TO_BRONZE_THRESHOLD;
        }
    }
}

