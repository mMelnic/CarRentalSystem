package carrental.util;

public enum Thresholds {
    REGULAR_TO_BRONZE(5),
    BRONZE_TO_SILVER(10),
    SILVER_TO_GOLD(20);

    private final int threshold;

    Thresholds(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }
}
