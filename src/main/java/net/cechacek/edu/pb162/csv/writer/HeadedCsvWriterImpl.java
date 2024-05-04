package net.cechacek.edu.pb162.csv.writer;

import net.cechacek.edu.pb162.csv.Messages;
import net.cechacek.edu.pb162.csv.reader.CsvReader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Implementation of {@link HeadedCsvWriter}
 */
public class HeadedCsvWriterImpl
        extends AbstractCsvWriter<Map<String, String>>
        implements HeadedCsvWriter {

    private boolean expectHeaderWrite = true;
    private List<String> header;

    /**
     * Creates {@link CsvReader} instance with given stream and delimiter
     *
     * @param os          output stream of CSV data
     * @param delimiter   delimiter
     * @param quoter      element quotation character
     * @param charset     character encoding
     * @param header      header
     * @param writeHeader true if header is expected to be written
     */
    public HeadedCsvWriterImpl(OutputStream os, char delimiter, char quoter, Charset charset, List<String> header, boolean writeHeader) {
        super(os, delimiter, quoter, charset);
        this.header = new ArrayList<>(header);
        this.expectHeaderWrite = writeHeader;
    }

    @Override
    public void write(Map<String, String> data) throws IOException {
        if (expectHeaderWrite) {
            writeHeader(data);
        }
        super.write(data);
    }

    @Override
    protected String asLine(Map<String, String> data) {
        return header.stream()
                .map(key -> dataForHeaderKey(key, data))
                .map(this::quoteIfNeeded)
                .collect(Collectors.joining(String.valueOf(delimiter)));
    }

    private void writeHeader(Map<String, String> data) throws IOException {
        if (!expectHeaderWrite) {
            return;
        }
        this.header = new ArrayList<>(data.keySet());
        var headerLine = super.asLine(header);
        writeLine(headerLine);
        expectHeaderWrite = false;
    }

    private String dataForHeaderKey(String key, Map<String, String> data) {
        var item = data.get(key);
        if (item == null) {
            throw new IllegalArgumentException(Messages.MISSING_HEADER_KEY);
        }
        return item;
    }
}
