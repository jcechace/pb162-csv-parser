package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.csv.reader.HeadedCsvReader;
import net.cechacek.edu.pb162.csv.reader.PlainCsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reading and parsing of CSV data
 */
public class CsvParserImpl implements CsvParser {


    private final char delimiter;
    private final char quoter;
    private final Charset charset;

    /**
     * Creates instance with {@link CsvParser#DEFAULT_DELIMITER}
     */
    public CsvParserImpl() {
        this(DEFAULT_DELIMITER, DEFAULT_QUOTER, DEFAULT_CHARSET);
    }

    /**
     * Creates instance with given delimiter
     *
     * @param delimiter delimiter used to separate values
     * @param quoter element quotation character
     * @param charset character encoding
     */
    public CsvParserImpl(char delimiter,char quoter, Charset charset) {
        this.delimiter = delimiter;
        this.quoter = quoter;
        this.charset = charset;
    }

    @Override
    public PlainCsvReader open(Path path) throws IOException {
       return open(Files.newInputStream(path));
    }

    @Override
    public PlainCsvReader open(InputStream is) {
        return new PlainCsvReader(is, delimiter, quoter, charset);
    }

    @Override
    public HeadedCsvReader openWithHeader(Path path) throws IOException {
        return openWithHeader(Files.newInputStream(path));
    }

    @Override
    public HeadedCsvReader openWithHeader(InputStream is) throws IOException {
        return new HeadedCsvReader(is, delimiter, quoter, charset);
    }

    @Override
    public List<List<String>> readAll(Path path) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try(PlainCsvReader reader = open(path)) {
            reader.forEach(data::add);
        }
        return data;
    }

    @Override
    public List<Map<String, String>> readAllWithHeader(Path path) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try(HeadedCsvReader reader = openWithHeader(path)) {
            reader.forEach(data::add);
        }
        return data;
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
