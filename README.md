[![License](http://img.shields.io/:license-apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central Version](https://img.shields.io/maven-central/v/net.cechacek.edu/pb162-csv-parser)](https://central.sonatype.com/artifact/net.cechacek.edu/pb162-csv-parser)
[![Javadoc](https://img.shields.io/badge/-javadoc-blue)](http://jcechace.github.io/pb162-csv-parser/apidocs/)

PB162 Simple CSV Toolkit
====================================

This library provides simple way of reading CSV data as java collections. As of 0.4.0-final writing is also supported.

## Installation

In order to use CSV Toolkit in your project, add the following dependency to your project's ``pom.xml``

```xml

<dependency>
    <groupId>net.cechacek.edu</groupId>
    <artifactId>pb162-csv-parser</artifactId>
    <version>0.3.0-final</version>
</dependency>
```

## Usage Example

Given a CSV data set

```csv
atomicNumber, symbol, name, standardState, groupBlock, yearDiscovered
1, H , Hydrogen,gas,nonmetal,1766
2, He , Helium,gas,noble gas,1868
```

Two types of data structures are supported, depending on whether we want to use the first line as a header.

For plain (headerless) data sets, each line will be converted into ``List<String>`` and thus the entire data set
can be converted into ``List<List<String>``. With the example above, the reader will yield

```
[
    [atomicNumber, symbol, name, standardState, groupBlock, yearDiscovered],
    [1, H , Hydrogen,gas,nonmetal,1766],
    [2, He , Helium,gas,noble gas,1868],
]
```

For data sets with a header, each line will be converted into ``Map<String, String>`` with
column names used as keys. Thus, the entire data set can be converted into ``List<Map<String, String>>``.
With the example above, the parser will yield

```
[
    {
        atomicNumber: 1, 
        symbol: H, 
        name: Hydrogen, 
        standardState: gas, 
        groupBlock: nonmetal, 
        yearDiscovered: 1766
    }.
    {
        atomicNumber: 2,
        symbol: He, 
        name: Helium, 
        standardState: gas, 
        groupBlock: noble gas,
        yearDiscovered: 1868
    }
]
```

### Code Samples

Reading raw CSV data

```java



public static void main(String[] args) {
    Path path = Paths.get("test.csv");
    CsvToolkit toolkit = DefaultToolkit.create();

    // Reader Usage: Consumer
    System.out.println("Reader: Consumer");
    try (var reader = toolkit.read(path)) {
        reader.forEach(System.out::println);
    }

    // with header
    try (var reader = toolkit.readWithHeader(path)) {
        reader.forEach(System.out::println);
    }


    // Reader Usage: iterative
    System.out.println("Reader: Iterative");
    try (var reader = toolkit.read(path)) {
        while (reader.ready()) {
            var line = reader.read();
            System.out.println(line);
        }
    }

    // With Header
    try (var reader = toolkit.readWithHeader(path)) {
        while (reader.ready()) {
            var line = reader.read();
            System.out.println(line);
        }
    }
}
```

Reading CSV data with conversion

```java
static class ElementHeadedConvertor implements ValueConvertor<Map<String, String>, Element> {
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

public static void main(String[] args) {
    Path path = Paths.get("test.csv");
    CsvToolkit toolkit = DefaultToolkit.create();
    ElementHeadedConvertor elementConvertor = new ElementHeadedConvertor();

    // Reader Usage: Consumer
    try (var reader = toolkit.readWithHeader(path)) {
        reader.forEach(elementConvertor, System.out::println);
    }

    // Reader Usage: iterative
    try (var reader = toolkit.readWithHeader(path)) {
        while (reader.ready()) {
            var line = reader.read(elementConvertor);
            System.out.println(line);
        }
    }
}
```

Writing CSV data with conversion
```java

public static void main(String[] args) {
    List<Element> elements = getElements();
    List<String> header = List.of("atomicNumber", "symbol", "name");
    Path path = Paths.get("test.csv");
    CsvToolkit toolkit = DefaultToolkit.create();
    ElementHeadedConvertor elementConvertor = new ElementHeadedConvertor();

    // Reader Usage: Consumer
    try (var writer = toolkit.writeWithHeader(path, header)) {
        writer.writeEach(elementConvertor, elements);
    }


    // Reader Usage: iterative
    try (var writer = toolkit.writeWithHeader(path, header)) {
        elements.forEach(element -> writer.write(elementConvertor, element));
    }
}
```

Refer to tests for more examples. 