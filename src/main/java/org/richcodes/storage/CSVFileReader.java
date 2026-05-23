package org.richcodes.storage;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.richcodes.model.Tweet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CSVFileReader {

    private static FileReader fileReader;

    public CSVFileReader(Path path) throws IOException {
        Path parent = path.getParent();
        if(parent != null){
            Files.createDirectories(parent);
        }
        boolean exists = Files.exists(path);
        if(!exists){
            throw new IOException("File not found");
        }
        fileReader = new FileReader(path.toFile());
    }

    public static CSVFileReader create(String path) throws IOException {
        if(path == null || path.isBlank()) {
            throw new IllegalArgumentException("path cannot be null or blank");
        }
        return new CSVFileReader(Paths.get(path));
    }

    public static List<Tweet> readAll() throws Exception {
        CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(1)
                .build();
        List<String[]> rows = csvReader.readAll();
        return rows.stream()
                .filter(row -> row.length >= 2)
                .map(row -> new Tweet(
                        row[0],
                        row[1],
                        row.length > 2 ? row[2] : "N/A"
                ))
                .collect(Collectors.toList());
    }

}
