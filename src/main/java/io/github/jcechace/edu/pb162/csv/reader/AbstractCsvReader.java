package io.github.jcechace.edu.pb162.csv.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Abstract base for {@link CsvReader} implementations
 *
 * @param <T> type parameter of {@link #read()} result
 */
public abstract class AbstractCsvReader<T> implements CsvReader<T> {

    protected final BufferedReader reader;
    protected final String delimiter;


    /**
     * Creates {@link CsvReader} instance with given stream and delimiter
     *
     * @param is input stream of CSV data
     * @param delimiter delimiter
     * @param charset character encoding
     * @throws  UnsupportedEncodingException on invalid character encoding
     */
    public AbstractCsvReader(InputStream is, String delimiter, Charset charset) throws UnsupportedEncodingException {
        Objects.requireNonNull(is);
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(charset);

        this.delimiter = delimiter;
        this.reader = new BufferedReader(new InputStreamReader(is, charset.name()));
    }

    @Override
    public void forEach(Consumer<T> consumer) throws IOException {
        T line;
        while ((line = read()) != null ) {
            consumer.accept(line);
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    private List<String> parseLine(String line) {
        return Arrays.stream(line.split(delimiter))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    protected List<String> readLine() throws IOException {
        String line = reader.readLine();
        return (line == null) ? null :  parseLine(line);
    }
}
