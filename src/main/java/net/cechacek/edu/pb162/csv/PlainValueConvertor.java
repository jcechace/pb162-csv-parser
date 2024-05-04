package net.cechacek.edu.pb162.csv;

import java.util.List;

/**
 * Domain convertor used for reading/writing  plain CSV data
 *
 * @param <D> domain type
 */
public interface PlainValueConvertor<D> extends ValueConvertor<List<String>, D> {
}
