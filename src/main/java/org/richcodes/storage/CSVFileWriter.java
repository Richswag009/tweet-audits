package org.richcodes.storage;

import org.richcodes.model.AnalysisResult;
import org.richcodes.model.Tweet;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVFileWriter implements AutoCloseable{

    private static CSVWriter writer = null;
    private Path path;
    private static boolean skipHeader = false;


    private CSVFileWriter(Path path, boolean shouldAppend) throws IOException {
        Path parent = path.getParent();
        if(parent != null){
            Files.createDirectories(parent);
        }
        boolean exists = Files.exists(path);
        skipHeader = shouldAppend && exists;
        writer = new CSVWriter(
                new FileWriter(path.toFile(), shouldAppend)
        );
    }

    public static CSVFileWriter create(String path,boolean shouldAppend) throws IOException {
        if(path == null || path.isBlank()) {
            throw new IllegalArgumentException("path cannot be null or blank");
        }
        return new CSVFileWriter(Paths.get(path),shouldAppend);
    }

    public  void writeList(List<Tweet> tweets) throws IOException {
        if (!skipHeader) {

            writer.writeNext(new String[]{
                    "id",
                    "text",
                    "createdAt"
            });
        }
        for (Tweet tweet : tweets) {
            writer.writeNext(new String[]{
                    tweet.id(),
                    tweet.text(),
                    tweet.createdAt()
            });
        }
    }

    public  void writeResult(Tweet tweet,AnalysisResult result) throws IOException {
        if (!skipHeader) {
            writer.writeNext(new String[]{
                    "id",
                    "reason",
                    "flagged",
            });
        }
        writer.writeNext(new String[]{
                tweet.id(),
                result.reason(),
                String.valueOf(result.flagged())
        });
    }

    @Override
    public String toString() {
        return "CSVWriter[path=" + this.path + "]";
    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }
}
