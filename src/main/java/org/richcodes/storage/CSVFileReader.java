package org.richcodes.storage;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.richcodes.model.Tweet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CSVFileReader {

    private final FileReader fileReader;
//    public List<Tweet> readAll()

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



    public static List<Tweet> readAll() throws Exception {
        CSVReader csvReader = new CSVReaderBuilder(fileReader)
                .withSkipLines(1)
                .build();
        List<String[]> rows = csvReader.readAll();

        System.out.println(rows.size());

        // map each String[] to a Tweet record
        return rows.stream()
                .map(row -> new Tweet(row[0], row[1], row[2]))
                .collect(Collectors.toList());
    }


}
