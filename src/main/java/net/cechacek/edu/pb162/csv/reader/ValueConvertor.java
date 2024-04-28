package net.cechacek.edu.pb162.csv.reader;

/**
 * Provides conversion of reader output type to domain model
 *
 * @param <T> reader output type
 * @param <D> domain type
 */
@FunctionalInterface
public interface ValueConvertor<T, D> {

    /**
     * Converts raw reader output into domain object
     *
     * @param value reader output
     * @return domain object instance
     */
    D convert(T value);
}
