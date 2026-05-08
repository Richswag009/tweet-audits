package org.richcodes.storage;

import org.richcodes.enums.ParserType;
import org.richcodes.model.Tweet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TweetParser {


    private Path path ;
    private ParserType parserType;

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

    public List<Tweet> parse(){
        return new ArrayList<>();
    }
}
