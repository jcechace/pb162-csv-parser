package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.csv.reader.CsvReader;
import net.cechacek.edu.pb162.csv.reader.HeadedCsvReader;
import net.cechacek.edu.pb162.csv.reader.PlainCsvReader;
import net.cechacek.edu.pb162.csv.writer.CsvWriter;
import net.cechacek.edu.pb162.csv.writer.HeadedCsvWriter;
import net.cechacek.edu.pb162.csv.writer.PlainCsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * CSV Parser able to either open a source of CSV data or to directly read all the data
 */
public interface CsvToolkit {

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
     * Default file open options used for writing
     */
    Set<OpenOption> DEFAULT_OPEN_OPTIONS_WRITE = Set.of(CREATE, TRUNCATE_EXISTING, WRITE);


    /**
     * Opens {@link CsvReader} from given path to read data from
     *
     * @param path file path
     * @return instance of {@link CsvReader} capable of reading plain CSV data
     * @throws IOException on any IO error
     */
    PlainCsvReader read(Path path) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data from
     *
     * @param is source of data
     * @return instance of {@link CsvReader} capable of reading plain CSV data
     * @throws IOException on any IO error
     */
    PlainCsvReader read(InputStream is) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data with header from
     *
     * @param path file path
     * @return instance of {@link CsvReader} capable of reading CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    HeadedCsvReader readWithHeader(Path path) throws IOException;

    /**
     * Opens {@link CsvReader} from given {@link InputStream} to read CSV data with header from
     *
     * @param is source of data
     * @return instance of {@link CsvReader} capable of reading CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    HeadedCsvReader readWithHeader(InputStream is) throws IOException;


    /**
     * Opens {@link CsvWriter} from given path to write data to
     *
     * @param path    file path
     * @param options options specifying how the file is open
     * @return instance of {@link CsvWriter}} capable of writing plain CSV data
     * @throws IOException on any IO error
     */
    PlainCsvWriter write(Path path, OpenOption... options) throws IOException;

    /**
     * Opens {@link CsvWriter} from given {@link OutputStream} to write CSV data to
     *
     * @param os target of data
     * @return instance of {@link CsvWriter} capable of writing plain CSV data
     * @throws IOException on any IO error
     */
    PlainCsvWriter write(OutputStream os) throws IOException;

    /**
     * Opens {@link CsvWriter} from given {@link OutputStream} to write CSV data with header to
     *
     * @param path    file path
     * @param header  csv header
     * @param options options specifying how the file is open,
     *                if empty {@link CsvToolkit#DEFAULT_OPEN_OPTIONS_WRITE} are used
     * @return instance of {@link CsvWriter} capable of writing CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    HeadedCsvWriter writeWithHeader(Path path, List<String> header, OpenOption... options) throws IOException;

    /**
     * Opens {@link CsvWriter} from given {@link OutputStream} to write CSV data with header to
     *
     * @param os target of data
     * @param header  csv header
     * @param writeHeader if true, header will be written before first write
     * @return instance of {@link CsvReader} capable of reading CSV data <strong>with header</strong>
     * @throws IOException on any IO error
     */
    HeadedCsvWriter writeWithHeader(OutputStream os, List<String> header, boolean writeHeader) throws IOException;


    /**
     * Gets delimiter used by this parser
     *
     * @return delimiter
     */
    char getDelimiter();

    /**
     * Gets quoter character used by this parser
     *
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
