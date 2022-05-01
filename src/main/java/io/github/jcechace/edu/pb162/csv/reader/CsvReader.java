package io.github.jcechace.edu.pb162.csv.reader;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Interface for classes providing the capability of reading CSV data
 *
 * @param <T> type parameter of {@link #read()} result
 */
public interface CsvReader<T> extends Closeable {

    /**
     * Reads single line of CSV data
     * Trims leading and trailing whitespaces around row entries
     *
     * @return process line of CSV
     * @throws IOException on any IO error
     */
    T read() throws IOException;

    /**
     * Consumes each line of CSV data
     *
     * @param consumer consumer called with each line of CSV
     * @throws IOException on any IO error
     */
    void forEach(Consumer<T> consumer) throws IOException;

    /**
     * Tells if the ready is ready to be read
     *
     * @throws IOException on any IO error
     * @return true if ready
     */
    boolean ready() throws IOException;
}
