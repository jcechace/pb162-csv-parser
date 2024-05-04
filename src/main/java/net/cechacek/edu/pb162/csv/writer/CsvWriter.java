package net.cechacek.edu.pb162.csv.writer;

import net.cechacek.edu.pb162.csv.ValueConvertor;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for classes providing the capability of reading CSV data
 *
 * @param <T> type parameter of {@link #write(Object)}
 */
public interface CsvWriter<T> extends Closeable {
    /**
     * Reads single line of CSV data
     * Trims leading and trailing whitespaces around row entries
     *
     * @param value line of CSV data
     * @throws IOException on any IO error
     */
    void write(T value) throws IOException;

    /**
     * Same as {@link #write(Object)} proceeded by conversion from domain type
     *
     * @param convertor domain convertor
     * @param value domain object
     * @param <D> domain type
     */
    <D> void write(ValueConvertor<T, D> convertor, D value) throws IOException;

    /**
     * Consumes each line of CSV data
     *
     * @param items line of CSV
     * @throws IOException on any IO error
     */
    void writeEach(Iterable<T> items) throws IOException;

    /**
     * Same as {@link #write(Object)} but for converted values
     *
     * @param convertor domain convertor
     * @param items domain objects to write
     * @param <D> domain type
     */
    <D> void writeEach(ValueConvertor<T, D> convertor, Iterable<D> items) throws IOException;

}
