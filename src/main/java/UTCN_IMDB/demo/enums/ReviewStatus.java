package UTCN_IMDB.demo.enums;

public enum ReviewStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static ReviewStatus fromString(String status) {
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
            if (reviewStatus.name().equalsIgnoreCase(status)) {
                return reviewStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ReviewStatus.class.getCanonicalName() + "." + status);
    }
}
