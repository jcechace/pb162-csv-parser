package net.cechacek.edu.pb162.csv;

import net.cechacek.edu.pb162.TestUtils;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SoftAssertionsExtension.class)
public class CsvToolkitEncodingTest {

    @InjectSoftAssertions
    private SoftAssertions softly;

    private CsvToolkit csv;

    @BeforeEach
    void setUp() {
        csv = DefaultToolkit.create(',', '"', Charset.forName("ISO-8859-2"));
    }

    @Test
    @DisplayName("Loads plain CSV data with Latin 2 encoding")
    public void plainCsvWithReader() throws Exception {
        var expected = List.of(
                List.of("příliš", "žluťoučký", "kůň"),
                List.of("úpěl", "ďábelské", "ódy")
        );

        List<List<String>> actual = new ArrayList<>();
        try (var reader = csv.read((TestUtils.resourcePath("/plain_iso-8859-2.csv")))) {
            reader.forEach(actual::add);
            softly.assertThat(reader.read()).isNull();
        }
        softly.assertThat(actual).containsExactlyElementsOf(expected);
    }
}
