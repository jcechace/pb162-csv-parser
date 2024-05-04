package net.cechacek.edu.pb162.csv.writer;

import net.cechacek.edu.pb162.csv.Messages;
import net.cechacek.edu.pb162.csv.reader.CsvReader;
import net.cechacek.edu.pb162.csv.ValueConvertor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract base for {@link CsvReader} implementations
 *
 * @param <T> type parameter of {@link #write(Object)} result
 */
public abstract class AbstractCsvWriter<T> implements CsvWriter<T> {

    /**
     * Value delimiter character
     */
    protected final char delimiter;

    /**
     * Value quoter character
     */
    protected final char quoter;

    /**
     * File writer
     */
    protected final BufferedWriter writer;


    /**
     * Creates {@link CsvWriter} instance with given stream and delimiter
     *
     * @param os        output stream of CSV data
     * @param delimiter delimiter
     * @param quoter    element quotation character
     * @param charset   character encoding
     */
    public AbstractCsvWriter(OutputStream os, char delimiter, char quoter, Charset charset) {
        Objects.requireNonNull(os);
        Objects.requireNonNull(charset);

        this.delimiter = delimiter;
        this.quoter = quoter;
        this.writer = new BufferedWriter(new OutputStreamWriter(os, charset));
    }

    @Override
    public void write(T data) throws IOException {
        Objects.requireNonNull(data);
        var line = asLine(data);
        writeLine(line);
    }

    @Override
    public <D> void write(ValueConvertor<T, D> convertor, D domain) throws IOException {
        var data = convertor.toData(domain);
        write(data);
    }

    @Override
    public void writeEach(Iterable<T> data) throws IOException {
        for (T line : data) {
            this.write(line);
        }
    }

    @Override
    public <D> void writeEach(ValueConvertor<T, D> convertor, Iterable<D> domains) throws IOException {
        for (D domain : domains) {
            var data = convertor.toData(domain);
            this.write(data);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    /**
     * Writes a single line of CSV
     * @param line line to write
     * @throws IOException on any io error
     */
    protected void writeLine(String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    /**
     * Quotes line item if needed (that is when it contains the delimiter character)
     *
     * @param item line item
     * @throws IllegalArgumentException if item contains quoter character
     * @return original or quoted line item
     */
    protected String quoteIfNeeded(String item) {
        if (item.indexOf(delimiter) == -1) {
            return item;
        }
        if (item.indexOf(quoter) != -1) {
            throw new IllegalArgumentException(Messages.INVALID_CHARACTER);
        }
        return quoter + item + quoter;
    }

    /**
     * Converts CSV data into single {@link String}
     * @param data CSV data
     * @return CSV data as String
     */
    protected String asLine(List<String> data) {
        return data.stream()
                .map(this::quoteIfNeeded)
                .collect(Collectors.joining(String.valueOf(delimiter)));
    }


    /**
     * Converts CSV data into single {@link String}
     * @param data CSV data
     * @return CSV data as String
     */
    protected abstract String asLine(T data);

}
