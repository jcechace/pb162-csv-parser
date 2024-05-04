package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.csv.reader.HeadedCsvReader;
import net.cechacek.edu.pb162.csv.reader.HeadedCsvReaderImpl;
import net.cechacek.edu.pb162.csv.reader.PlainCsvReader;
import net.cechacek.edu.pb162.csv.reader.PlainCsvReaderImpl;
import net.cechacek.edu.pb162.csv.writer.HeadedCsvWriter;
import net.cechacek.edu.pb162.csv.writer.HeadedCsvWriterImpl;
import net.cechacek.edu.pb162.csv.writer.PlainCsvWriter;
import net.cechacek.edu.pb162.csv.writer.PlainCsvWriterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Reading and parsing of CSV data
 */
public final class DefaultToolkit implements CsvToolkit {

    private final char delimiter;
    private final char quoter;
    private final Charset charset;


    /**
     * Creates instance with default configuration
     *
     * @return new instance
     */
    public static CsvToolkit create(){
        return create(DEFAULT_DELIMITER, DEFAULT_QUOTER, DEFAULT_CHARSET);
    }

    /**
     * Creates instance with given delimiter
     *
     * @param delimiter delimiter used to separate values
     * @param quoter    element quotation character
     * @param charset   character encoding
     * @return new instance
     */
    public static CsvToolkit create(char delimiter, char quoter, Charset charset) {
        return new DefaultToolkit(delimiter, quoter, charset);
    }

    /**
     * See {@link DefaultToolkit#create(char, char, Charset)}
     */
    private DefaultToolkit(char delimiter, char quoter, Charset charset) {
        this.delimiter = delimiter;
        this.quoter = quoter;
        this.charset = charset;
    }

    @Override
    public PlainCsvReader read(Path path) throws IOException {
        return read(Files.newInputStream(path));
    }

    @Override
    public PlainCsvReader read(InputStream is) {
        return new PlainCsvReaderImpl(is, delimiter, quoter, charset);
    }

    @Override
    public HeadedCsvReader readWithHeader(Path path) throws IOException {
        return readWithHeader(Files.newInputStream(path));
    }

    @Override
    public HeadedCsvReader readWithHeader(InputStream os) throws IOException {
        return new HeadedCsvReaderImpl(os, delimiter, quoter, charset);
    }

    @Override
    public PlainCsvWriter write(Path path, OpenOption... options) throws IOException {
        var optionSet = (options.length > 0) ? Set.of(options) : DEFAULT_OPEN_OPTIONS_WRITE;
        var optionArray = optionSet.toArray(OpenOption[]::new);
        return write(Files.newOutputStream(path, optionArray));
    }

    @Override
    public PlainCsvWriter write(OutputStream os) {
        return new PlainCsvWriterImpl(os, delimiter, quoter, charset);
    }

    @Override
    public HeadedCsvWriter writeWithHeader(Path path, List<String> header, OpenOption... options) throws IOException {
        var optionSet = (options.length > 0) ? Set.of(options) : DEFAULT_OPEN_OPTIONS_WRITE;

        var writeHeader = Files.notExists(path)
                || Files.exists(path) && optionSet.contains(TRUNCATE_EXISTING);

        var optionArray = optionSet.toArray(OpenOption[]::new);
        return writeWithHeader(Files.newOutputStream(path, optionArray), header, writeHeader);
    }

    @Override
    public HeadedCsvWriter writeWithHeader(OutputStream os, List<String> header, boolean writeHeader) {
        return new HeadedCsvWriterImpl(os, delimiter, quoter, charset, header, writeHeader);
    }

    @Override
    public char getDelimiter() {
        return delimiter;
    }

    @Override
    public char getQuoter() {
        return quoter;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }
}
