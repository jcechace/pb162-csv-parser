package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.TestUtils;
import net.cechacek.edu.pb162.csv.reader.ValueConvertor;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.description;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(SoftAssertionsExtension.class)
public class CsvParserWithConversionTest {

    record Element(int id, String symbol, String name){};

    static class ElementConverter implements ValueConvertor<List<String>, Element> {
        @Override
        public Element convert(List<String> value) {
            return new Element(
                    Integer.parseInt(value.get(0)),
                    value.get(1),
                    value.get(2)
            );
        }
    }

    static class ElementHeadedConvertor implements ValueConvertor<Map<String, String>, Element> {
        @Override
        public Element convert(Map<String, String> value) {
            return new Element(
                    Integer.parseInt(value.get("atomicNumber")),
                    value.get("symbol"),
                    value.get("name")
            );
        }
    }

    @InjectSoftAssertions
    private SoftAssertions softly;

    private CsvParser parser;


    @BeforeEach
    void setUp() {
        parser = CsvToolkit.parser(',', '"', StandardCharsets.UTF_8);
    }


    @Test
    @DisplayName("Loads plain CSV data")
    public void plainCsv() throws Exception {
        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium")
        );

        try (var reader = parser.open(TestUtils.resourcePath("/plain.csv"))) {
            for (int i = 0; i < 3; i++) {
                softly.assertThat(reader.read(new ElementConverter())).isEqualTo(expected.get(i));
            }
            softly.assertThat(reader.read()).isNull();
        }
    }


    @Test
    @DisplayName("Loads plain CSV data via consumer")
    public void plainCsvConsumer() throws Exception {
        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium")
        );

        List<Element> actual = new ArrayList<>();
        try (var reader = parser.open(TestUtils.resourcePath("/plain.csv"))) {
            reader.forEach(new ElementConverter(), actual::add);
            softly.assertThat(reader.read()).isNull();
        }
        softly.assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    @DisplayName("Loads CSV data with header")
    public void headedCsv() throws Exception {
        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium"),
                new Element(4, "Be", "Beryllium")
        );

        try (var reader = parser.openWithHeader(TestUtils.resourcePath("/header.csv"))) {
            for (int i = 0; i < 4; i++) {
                softly.assertThat(reader.read(new ElementHeadedConvertor())).isEqualTo(expected.get(i));
            }
            softly.assertThat(reader.read()).isNull();
        }
    }

    @Test
    @DisplayName("Loads CSV data with header via consumer")
    public void headedCsvConsumer() throws Exception {
        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium"),
                new Element(4, "Be", "Beryllium")
        );

        var actual = new ArrayList<Element>();
        try (var reader = parser.openWithHeader(TestUtils.resourcePath("/header.csv"))) {
            reader.forEach(new ElementHeadedConvertor(), actual::add);
            softly.assertThat(reader.read()).isNull();
        }
        softly.assertThat(actual).containsExactlyElementsOf(expected);
    }

}
