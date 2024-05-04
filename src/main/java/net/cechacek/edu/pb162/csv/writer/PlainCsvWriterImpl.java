package net.cechacek.edu.pb162.csv.writer;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Implementation of {@link PlainCsvWriter}
 */
public class PlainCsvWriterImpl
        extends AbstractCsvWriter<List<String>>
        implements PlainCsvWriter {

    /**
     * Creates {@link CsvWriter} instance with given stream and delimiter
     *
     * @param os        output stream of CSV data
     * @param delimiter delimiter
     * @param quoter    element quotation character
     * @param charset   character encoding
     */
    public PlainCsvWriterImpl(OutputStream os, char delimiter, char quoter, Charset charset) {
        super(os, delimiter, quoter, charset);
    }

    @Override
    protected String asLine(List<String> data) {
        return super.asLine(data);
    }

}