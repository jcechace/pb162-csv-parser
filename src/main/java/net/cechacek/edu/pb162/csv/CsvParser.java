package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.csv.reader.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * CSV Parser able to either open a source of CSV data or to directly read all the data
 */
public interface CsvParser {

    /**
     * Default delimiter is a comma
     */
    char DEFAULT_DELIMITER = ',';

    /**
     * Default quoter is a double quote
     */
    char DEFAULT_QUOTER = '"';

    /**
     * Default character encoding
     */
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Opens {@link CsvReader} from given path to read data from
     *
     * @param path file path
     * @return instance of @{CsvReader} capable of reading plain CSV data
     * @throws IOException on any IO error
     */
    CsvReader<List<String>> open(Path path) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data from
     *
     * @param is source of data
     * @return instance of @{CsvReader} capable of reading plain CSV data
     * @throws IOException on any IO error
     */
    CsvReader<List<String>> open(InputStream is) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data with header from
     *
     * @param path file path
     * @return instance of @{CsvReader} capable of reading CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    CsvReader<Map<String, String>> openWithHeader(Path path) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data with header from
     *
     * @param is source of data
     * @return instance of @{CsvReader} capable of reading CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    CsvReader<Map<String, String>> openWithHeader(InputStream is) throws IOException;

    /**
     * Reads all CSV data from given path
     *
     * @param path file path
     * @return read CSV data
     * @throws IOException on any IO error
     */
    List<List<String>> readAll(Path path) throws IOException;

    /**
     * Reads all CSV data <strong>with header</strong> from given path
     *
     * @param path file path
     * @return read CSV data  <strong>with header</strong>
     * @throws IOException on any IO error
     */
    List<Map<String, String>> readAllWithHeader(Path path) throws IOException;

    /**
     * Gets delimiter used by this parser
     * @return delimiter
     */
    char getDelimiter();

    /**
     * Gets quoter character used by this parser
     * @return quoter
     */
    char getQuoter();

    /**
     * Gets charset used by this parser
     *
     * @return charset
     */
    Charset getCharset();
}
