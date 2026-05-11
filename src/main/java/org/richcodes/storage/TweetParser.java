package org.richcodes.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.richcodes.enums.ParserType;
import org.richcodes.model.Tweet;
import org.richcodes.model.TweetWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TweetParser {


    private Path path ;
    private ParserType parserType;
    private final ObjectMapper mapper = new ObjectMapper();

    public TweetParser(String path, ParserType parserType) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be null or blank");
        }
        if (parserType == null) {
            throw new IllegalArgumentException("ParserType cannot be null");
        }
        this.path = Paths.get(path);
        this.parserType = parserType;
    }

    public String readContent() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(this.path.toString());

            if (inputStream == null) {
                throw new RuntimeException("File not found");
            }
            String content = new String(inputStream.readAllBytes());

            String marker = "window.YTD.tweets.part0 =";

            int start = content.indexOf(marker);
            if (start == -1) {
                throw new RuntimeException("Tweet data marker not found");
            }

            return content.substring(start + marker.length());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<Tweet>  parse () throws JsonProcessingException {
        return switch (parserType) {
            case CSV -> parseCSV();
            case JSON -> parseJson();
        };
    }

    private List<Tweet> parseCSV() throws JsonProcessingException {
        String content = this.readContent();
        List<TweetWrapper> wrappers = mapper.readValue(
                content,
                new TypeReference<List<TweetWrapper>>() {}
        );
        return wrappers.stream()
                .map(TweetWrapper::tweet)
                .collect(Collectors.toList());
    }


    private List<Tweet> parseJson() throws JsonProcessingException {
        String content = this.readContent();
        List<TweetWrapper> wrappers = mapper.readValue(
                content,
                new TypeReference<List<TweetWrapper>>() {}
        );
        return wrappers.stream()
                .map(TweetWrapper::tweet)
                .collect(Collectors.toList());
    }




}
