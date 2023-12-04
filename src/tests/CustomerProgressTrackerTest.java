package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import carrental.util.CustomerProgressTracker;

public class CustomerProgressTrackerTest {

    @Test
    public void testUpdateAndGetProgress() {
        // Create an instance of CustomerProgressTracker
        CustomerProgressTracker progressTracker = new CustomerProgressTracker();

        // Update progress for a customer
        String customerUsername = "testUser";
        int numberOfReservations = 5;
        progressTracker.updateProgress(customerUsername, numberOfReservations);

        // Get and assert the progress for the customer
        int expectedProgress = progressTracker.calculateProgress(numberOfReservations);
        int actualProgress = progressTracker.getProgress(customerUsername);
        assertEquals(expectedProgress, actualProgress);
    }

    @Test
    public void testGetCurrentTier() {
        // Create an instance of CustomerProgressTracker
        CustomerProgressTracker progressTracker = new CustomerProgressTracker();

        // Test for Regular tier
        int regularReservations = 3;
        assertEquals("Regular", progressTracker.getCurrentTier(regularReservations));

        // Test for Bronze tier
        int bronzeReservations = 5;
        assertEquals("Bronze", progressTracker.getCurrentTier(bronzeReservations));

        // Test for Silver tier
        int silverReservations = 10;
        assertEquals("Silver", progressTracker.getCurrentTier(silverReservations));

        // Test for Gold tier
        int goldReservations = 20;
        assertEquals("Gold", progressTracker.getCurrentTier(goldReservations));
    }

    @Test
    public void testGetCurrentTierWithProgressTracker() {
        // Create an instance of CustomerProgressTracker
        CustomerProgressTracker progressTracker = new CustomerProgressTracker();

        // Test for Regular tier
        int regularReservations = 3;
        progressTracker.updateProgress("testUserA", regularReservations);
        assertEquals("Regular", progressTracker.getCurrentTier(regularReservations));

        // Test for Bronze tier
        int bronzeReservations = 9;
        progressTracker.updateProgress("testUserB", bronzeReservations);
        assertEquals("Bronze", progressTracker.getCurrentTier(bronzeReservations));

        // Test for Silver tier
        int silverReservations = 10;
        progressTracker.updateProgress("testUserC", silverReservations);
        assertEquals("Silver", progressTracker.getCurrentTier(silverReservations));

        // Test for Gold tier
        int goldReservations = 21;
        progressTracker.updateProgress("testUserD", goldReservations);
        assertEquals("Gold", progressTracker.getCurrentTier(goldReservations));
    }

    @Test
    public void testCalculateProgressAndTier() {
        // Create an instance of CustomerProgressTracker
        CustomerProgressTracker progressTracker = new CustomerProgressTracker();

        // Test for Regular tier and percentage 80
        int regularReservations = 4;
        int expectedRegularProgress = 80;
        assertEquals(expectedRegularProgress, progressTracker.calculateProgress(regularReservations));
        assertEquals("Regular", progressTracker.getCurrentTier(regularReservations));

        // Test for Bronze tier and percentage 40
        int bronzeReservations = 7;
        int expectedBronzeProgress = 40;
        assertEquals(expectedBronzeProgress, progressTracker.calculateProgress(bronzeReservations));
        assertEquals("Bronze", progressTracker.getCurrentTier(bronzeReservations));

        // Test for Silver tier and percentage 0
        int silverReservations = 10;
        int expectedSilverProgress = 0;
        assertEquals(expectedSilverProgress, progressTracker.calculateProgress(silverReservations));
        assertEquals("Silver", progressTracker.getCurrentTier(silverReservations));

        // Test for Gold tier and percentage 100
        int goldReservations = 22;
        int expectedGoldProgress = 100;
        assertEquals(expectedGoldProgress, progressTracker.calculateProgress(goldReservations));
        assertEquals("Gold", progressTracker.getCurrentTier(goldReservations));
    }
}

