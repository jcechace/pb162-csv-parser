package net.cechacek.edu.pb162.csv;

/**
 * Provides conversion between CSV data type and domain type
 *
 * @param <T> CSV data type
 * @param <D> domain type
 */
public interface ValueConvertor<T, D> {

    /**
     * Converts raw CSV data type into domain object
     *
     * @param data csv data
     * @return domain object
     */
    D toDomain(T data);

    /**
     * Converts domain object to CSV data
     *
     * @param domain  domain object
     * @return csv data
     */
    T toData(D domain);
}
