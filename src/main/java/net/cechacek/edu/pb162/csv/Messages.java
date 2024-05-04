package net.cechacek.edu.pb162.csv;

/**
 * Error messages
 */
public final class Messages {

    private Messages() {
        // intentionally private to prevent instantiation
    }

    /**
     * Error message for invalid CSV format
     */
    public static final String INVALID_FORMAT = "Invalid file format";

    /**
     * Error message for invalid character in CSV item when writing
     */
    public static final String INVALID_CHARACTER = "Item contains invalid character";

    /**
     * Error message signaling missing header write when expected
     */
    public static final String MISSING_HEADER = "Missing header";

    /**
     * Error message signaling that data are missing expected header key
     */
    public static final String MISSING_HEADER_KEY = "Missing header key in data";

}
