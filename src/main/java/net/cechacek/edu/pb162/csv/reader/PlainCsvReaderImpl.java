package net.cechacek.edu.pb162.csv.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Implementation of {@link PlainCsvReader}
 */
public class PlainCsvReaderImpl
        extends AbstractCsvReader<List<String>>
        implements PlainCsvReader {

    /**
     * Creates {@link CsvReader} instance with given stream and delimiter
     *
     * @param is        input stream of CSV data
     * @param delimiter delimiter
     * @param quoter    element quotation character
     * @param charset   character encoding
     */
    public PlainCsvReaderImpl(InputStream is, char delimiter, char quoter, Charset charset) {
        super(is, delimiter, quoter, charset);
    }

    @Override
    public List<String> read() throws IOException {
        return readLine();
    }
}
