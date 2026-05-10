package org.richcodes.storage;

import org.richcodes.model.Tweet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVWriter implements AutoCloseable{

    private static BufferedWriter writer = null;
    private Path path;
    private static boolean skipHeader = false;


    private CSVWriter(Path path, boolean shouldAppend) throws IOException {
        Path parent = path.getParent();
        if(parent != null){
            Files.createDirectories(parent);
        }
        boolean exists = Files.exists(path);
        skipHeader = shouldAppend && exists;
        writer = new BufferedWriter(new FileWriter(path.toFile()));

        System.out.println("getting parent " + parent);
    }


    public static CSVWriter create(String path,boolean shouldAppend) throws IOException {
        if(path == null || path.isBlank()) {
            throw new IllegalArgumentException("path cannot be null or blank");
        }
        return new CSVWriter(Paths.get(path),shouldAppend);
    }

    public static  void  writeList(List<Tweet> tweets) throws IOException {

        if(!skipHeader){
            System.out.println("writing header to CSV");
            writer.write("id,text,createdAt");
            writer.newLine();
        }
        System.out.println("writing tweets to CSV");
        for(Tweet tweet : tweets){
            writer.write(tweet.id()+","+tweet.text()+","+tweet.createdAt());
            writer.newLine();
        }
        System.out.printf("Extracted %d tweets to %s%n", tweets.size(), "tweets.csv");

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
