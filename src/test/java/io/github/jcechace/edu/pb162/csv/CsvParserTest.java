package io.github.jcechace.edu.pb162.csv;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static io.github.jcechace.edu.pb162.TestUtils.resourcePath;

@ExtendWith(SoftAssertionsExtension.class)
public class CsvParserTest {

    @InjectSoftAssertions
    private SoftAssertions softly;

    private CsvParser parser;

    @BeforeEach
    void setUp() {
        parser = CsvToolkit.parser(",", StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Loads plain CSV data")
    public void plainCsv() throws Exception {
        List<List<String>> data = parser.readAll(resourcePath("/plain.csv"));
        softly.assertThat(data).containsExactlyElementsOf(List.of(
                List.of("1", "H", "Hydrogen", "gas", "nonmetal", "1766"),
                List.of("2", "He", "Helium", "gas", "noble gas", "1868"),
                List.of("3", "Li", "Lithium", "solid", "alkali metal", "1817")
        ));
    }

    @Test
    @DisplayName("Loads plain CSV data with different row lengths")
    public void plainCsvDifferentRowLength() throws Exception {
        List<List<String>> data = parser.readAll(resourcePath("/plain_broken.csv"));
        softly.assertThat(data).containsExactlyElementsOf(List.of(
                List.of("1", "H", "Hydrogen", "gas", "nonmetal", "1766"),
                List.of("2", "He", "Helium", "gas", "noble gas", "1868", "broken")
        ));
    }

    @Test
    @DisplayName("Loads CSV data with header as plain")
    public void headedCsvAsPlain() throws Exception {
        List<List<String>> data = parser.readAll(resourcePath("/header.csv"));
        softly.assertThat(data).containsExactlyElementsOf(List.of(
                List.of("atomicNumber", "symbol", "name", "standardState", "groupBlock", "yearDiscovered"),
                List.of("1", "H", "Hydrogen", "gas", "nonmetal", "1766"),
                List.of("2", "He", "Helium", "gas", "noble gas", "1868"),
                List.of("3", "Li", "Lithium", "solid", "alkali metal", "1817"),
                List.of("4", "Be", "Beryllium", "solid", "alkaline earth metal", "1798")
        ));
    }

    @Test
    @DisplayName("Loads CSV data with header")
    public void headedCsv() throws Exception {
        List<Map<String, String>> data = parser.readAllWithHeader(resourcePath("/header.csv"));
        softly.assertThat(data).containsExactlyElementsOf(List.of(
                Map.of(
                        "atomicNumber", "1",
                        "symbol", "H",
                        "name", "Hydrogen",
                        "standardState", "gas",
                        "groupBlock", "nonmetal",
                        "yearDiscovered", "1766"
                ),
                Map.of(
                        "atomicNumber", "2",
                        "symbol", "He",
                        "name", "Helium",
                        "standardState", "gas",
                        "groupBlock", "noble gas",
                        "yearDiscovered", "1868"
                ),
                Map.of(
                        "atomicNumber", "3",
                        "symbol", "Li",
                        "name", "Lithium",
                        "standardState", "solid",
                        "groupBlock", "alkali metal",
                        "yearDiscovered", "1817"
                ),
                Map.of(
                        "atomicNumber", "4",
                        "symbol", "Be", "name",
                        "Beryllium", "standardState",
                        "solid", "groupBlock",
                        "alkaline earth metal",
                        "yearDiscovered",
                        "1798"
                )
        ));
    }

    @Test
    @DisplayName("Errors with more elements when loading CSV data with header")
    public void headedCsvHigherRowLength() throws Exception {
        Path path = resourcePath("/header_broken.csv");
        softly.assertThatThrownBy(() -> parser.readAllWithHeader(path))
                .isInstanceOf(IOException.class)
                .hasMessageContaining(Messages.INVALID_FORMAT);
    }

    @Test
    @DisplayName("Errors with less elements when loading CSV data with header")
    public void headedCsvLowerRowLength() throws Exception {
        Path path = resourcePath("/header_broken2.csv");
        softly.assertThatThrownBy(() -> parser.readAllWithHeader(path))
                .isInstanceOf(IOException.class)
                .hasMessageContaining(Messages.INVALID_FORMAT);
    }
}
