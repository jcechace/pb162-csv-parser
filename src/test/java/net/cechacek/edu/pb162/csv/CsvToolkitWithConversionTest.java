package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.TestUtils;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(SoftAssertionsExtension.class)
public class CsvToolkitWithConversionTest {

    record Element(Integer id, String symbol, String name){};

    static class ElementConverter implements PlainValueConvertor<Element> {
        @Override
        public Element toDomain(List<String> data) {
            return new Element(
                    Integer.valueOf(data.get(0)),
                    data.get(1),
                    data.get(2)
            );
        }

        @Override
        public List<String> toData(Element domain) {
            return List.of(domain.id.toString(), domain.symbol, domain.name);
        }
    }

    static class ElementHeadedConvertor implements HeadedValueConvertor<Element> {
        @Override
        public Element toDomain(Map<String, String> data) {
            return new Element(
                    Integer.parseInt(data.get("atomicNumber")),
                    data.get("symbol"),
                    data.get("name")
            );
        }

        @Override
        public Map<String, String> toData(Element domain) {
            return Map.of(
                    "atomicNumber", domain.id.toString(),
                    "symbol", domain.symbol,
                    "name", domain.name
            );
        }
    }

    @InjectSoftAssertions
    private SoftAssertions softly;

    private CsvToolkit csv;


    @BeforeEach
    void setUp() {
        csv = DefaultToolkit.create(',', '"', StandardCharsets.UTF_8);
    }


    @Test
    @DisplayName("Creates, writes and reads plain CSV data")
    public void createAndWritePlainCsv(@TempDir Path path) throws Exception {
        var file = path.resolve("test.csv");
        var converter = new ElementConverter();

        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium")
        );

        try (var writer = csv.write(file)) {
            for (int i = 0; i < 3; i++) {
                writer.write(converter, expected.get(i));
            }
        }

        try (var reader = csv.read(file)) {
            for (int i = 0; i < 3; i++) {
                softly.assertThat(reader.read(converter)).isEqualTo(expected.get(i));
            }
            softly.assertThat(reader.read()).isNull();
        }
    }

    @Test
    @DisplayName("Loads plain CSV data")
    public void plainCsv() throws Exception {
        var expected = List.of(
                new Element(1, "H", "Hydrogen"),
                new Element(2, "He", "Helium"),
                new Element(3, "Li", "Lithium")
        );

        try (var reader = csv.read(TestUtils.resourcePath("/plain.csv"))) {
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
        try (var reader = csv.read(TestUtils.resourcePath("/plain.csv"))) {
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

        try (var reader = csv.readWithHeader(TestUtils.resourcePath("/header.csv"))) {
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
        try (var reader = csv.readWithHeader(TestUtils.resourcePath("/header.csv"))) {
            reader.forEach(new ElementHeadedConvertor(), actual::add);
            softly.assertThat(reader.read()).isNull();
        }
        softly.assertThat(actual).containsExactlyElementsOf(expected);
    }

}
