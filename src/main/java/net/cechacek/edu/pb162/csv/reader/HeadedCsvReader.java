package net.cechacek.edu.pb162.csv.reader;

import net.cechacek.edu.pb162.csv.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

/**
 * {@link CsvReader} implementation for CSV data with header
 */
public class HeadedCsvReader extends AbstractCsvReader<Map<String, String>> {
    private List<String> header;

    /**
     * Creates {@link CsvReader} instance with given stream and delimiter
     *
     * @param is input stream of CSV data
     * @param delimiter delimiter
     * @param quoter element quotation character
     * @param charset character encoding
     * @throws IOException on any IO error
     */
    public HeadedCsvReader(InputStream is, char delimiter, char quoter, Charset charset) throws IOException {
        super(is, delimiter, quoter, charset);
        readHead();
    }

    @Override
    public Map<String, String> read() throws IOException {
        List<String> line = readLine();
        return (line == null) ? null : zipWithHeader(line);
    }

    private void readHead() throws IOException {
        if ((header = readLine()) == null) {
            throw new IOException(Messages.INVALID_FORMAT);
        }
    }

    private Map<String, String> zipWithHeader(List<String> line) throws IOException {
        if (line.size() != header.size()) {
            throw new IOException(Messages.INVALID_FORMAT);
        }

        return IntStream.range(0, header.size())
                .boxed()
                .collect(toMap(header::get, line::get, (x, y) -> y, LinkedHashMap::new));
    }
}
