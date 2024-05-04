package net.cechacek.edu.pb162.csv;

import java.util.Map;

/**
 * Domain convertor used for reading/writing CSV data with header
 * 
 * @param <D> domain type
 */
public interface HeadedValueConvertor<D> extends ValueConvertor<Map<String, String>, D> {
}
