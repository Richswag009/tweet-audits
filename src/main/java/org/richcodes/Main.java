package org.richcodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.richcodes.enums.ParserType;
import org.richcodes.model.Tweet;
import org.richcodes.model.TweetWrapper;
import org.richcodes.storage.TweetParser;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String EXTRACT_TWEETS_COMMAND = "extract-tweets";
    private static final String ANALYZE_TWEETS_COMMAND = "analyze-tweets";

    private Main() {
    }

    public static void main(String[] args) throws JsonProcessingException {
        if(args.length == 0){
            printUsage();
            return;
        }

        String command = args[0];
        if(!command.equals(EXTRACT_TWEETS_COMMAND ) && !command.equals(ANALYZE_TWEETS_COMMAND)){
            System.err.println("Error: Unknown command '" + command + "'");
            printUsage();
            System.exit(1);
        }


        switch (command) {
            case EXTRACT_TWEETS_COMMAND -> executeExtractTweets();
            case ANALYZE_TWEETS_COMMAND -> executeAnalyzeTweets();
        }


    }

    private static void executeAnalyzeTweets() {

    }
    
    private static void executeExtractTweets() throws JsonProcessingException {
        String path= "data/tweets/tweets.js";
        TweetParser tweetParser = new TweetParser(path,ParserType.CSV);
        tweetParser.parse();
    }

    private static void printUsage() {
        System.out.println("Usage: tweet-audit <command>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  extract-tweets  Extract tweets from Twitter archive");
        System.out.println("  analyze-tweets  Analyze tweets using Gemini AI");
    }

}