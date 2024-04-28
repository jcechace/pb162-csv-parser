package net.cechacek.edu.pb162.csv.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract base for {@link CsvReader} implementations
 *
 * @param <T> type parameter of {@link #read()} result
 */
public abstract class AbstractCsvReader<T> implements CsvReader<T> {

    /**
     * Value delimiter character
     */
    protected final char delimiter;

    /**
     * Value quoter character
     */
    protected final char quoter;

    private final LineParser parser;
    protected final BufferedReader reader;


    /**
     * Creates {@link CsvReader} instance with given stream and delimiter
     *
     * @param is        input stream of CSV data
     * @param delimiter delimiter
     * @param quoter    element quotation character
     * @param charset   character encoding
     */
    public AbstractCsvReader(InputStream is, char delimiter, char quoter, Charset charset) {
        Objects.requireNonNull(is);
        Objects.requireNonNull(charset);

        this.delimiter = delimiter;
        this.quoter = quoter;
        this.parser = new LineParser(delimiter, quoter);
        this.reader = new BufferedReader(new InputStreamReader(is, charset));
    }

    @Override
    public void forEach(Consumer<T> consumer) throws IOException {
        T line;
        while ((line = read()) != null) {
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
        return parser.parse(line);
    }

    protected List<String> readLine() throws IOException {
        String line = reader.readLine();
        return (line == null) ? null : parseLine(line);
    }

    /**
     * Parser used for parsing each CSV line
     */
    public static class LineParser {
        private final char delimiter;
        private final char quoter;

        /**
         * Creates line parser
         *
         * @param delimiter delimiter character
         * @param quoter quoter character
         */
        public LineParser(char delimiter, char quoter) {
            this.delimiter = delimiter;
            this.quoter = quoter;
        }

        /**
         * Parses CSV line
         *
         * @param input line to parse
         * @return list of values
         */
        public List<String> parse(String input) {
            var tokens = new ArrayList<String>();
            int start = 0;
            boolean quoted = false;

            for (var pos = 0; pos < input.length(); pos++) {
                var current = input.charAt(pos);
                if (current == quoter) {
                    quoted = !quoted;
                    continue;
                }

                if (!quoted && current == delimiter) {
                    var token = token(input, start, pos);
                    tokens.add(token);
                    start = pos + 1;
                }
            }

            var lastToken = token(input, start, input.length());
            if (lastToken.equals(",")) {
                tokens.add("");
            } else {
                tokens.add(lastToken);
            }

            return tokens;
        }
        private String token(String input, int from, int to) {
            var token = input.substring(from, to).strip();

            if (token.charAt(0) == quoter) {
                token = token.substring(1, token.length() - 1);
            }
            return token;
        }

    }
}
