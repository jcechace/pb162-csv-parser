[![License](http://img.shields.io/:license-apache%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.jcechace.edu/pb162-csv-parser?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/#nexus-search;gav~io.github.jcechace.edu~pb162-csv-parser~~~)
[![Javadoc](https://img.shields.io/badge/-javadoc-blue)](http://jcechace.github.io/pb162-csv-parser/apidocs/)

PB162 Simple CSV Reader
====================================

This library provides a very simple CSV parser capable of reading CSV data as java collections.
Simple CSV Reader does not perform any type conversions and all values are simply read as``String``.

## Installation
In order to use CSV Reader in your project, add the following dependency to your project's ``pom.xml``

```xml

<dependency>
    <groupId>net.cechacek.edu</groupId>
    <artifactId>pb162-csv-parser</artifactId>
    <version>0.2.0-final</version>
</dependency>
```

## Usage Example

Given a CSV data set
```csv
atomicNumber, symbol, name, standardState, groupBlock, yearDiscovered
1, H , Hydrogen,gas,nonmetal,1766
2, He , Helium,gas,noble gas,1868
```

Two types of parsing are supported, depending on whether we want to use the first line as a header.

For plain (headerless) data sets, each line will be converted into ``List<String>`` and thus the entire data set
can be converted into ``List<List<String>``. With the example above, the parser will yield
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

```java
Path path = Paths.get("test.csv");
CsvParser parser = CsvToolkit.parser();

// Direct usage
List<List<String>> data = parser.readAll(path);
List<Map<String, String>> dataHeaded = parser.readAllWithHeader(path);

System.out.println("Direct (eager)");
System.out.println(data);
System.out.println(dataHeaded);
System.out.println();

// Reader Usage: Consumer
System.out.println("Reader: Consumer");
try (var reader = parser.open(path)) {
    reader.forEach(System.out::println);
}

try (var reader = parser.openWithHeader(path)) {
    reader.forEach(System.out::println);
}
System.out.println();

// Reader Usage: iterative
System.out.println("Reader: Iterative");
try (var reader = parser.open(path)) {
    List<String> line;
    while ((line = reader.read()) != null) {
        System.out.println(line);
    }
}

try (var reader = parser.openWithHeader(path)) {
    Map<String, String> line;
    while ((line = reader.read()) != null) {
        System.out.println(line);
    }
}
```
