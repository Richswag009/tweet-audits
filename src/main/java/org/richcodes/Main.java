package org.richcodes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.genai.Client;
import org.richcodes.analyser.GeminiAnalyser;
import org.richcodes.analyser.GeminiClient;
import org.richcodes.analyser.GeminiService;
import org.richcodes.analyser.RetryPolicy;
import org.richcodes.enums.ParserType;
import org.richcodes.model.AnalysisResult;
import org.richcodes.model.Tweet;
import org.richcodes.model.TweetWrapper;
import org.richcodes.storage.CSVWriter;
import org.richcodes.storage.TweetParser;

import java.io.IOException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String EXTRACT_TWEETS_COMMAND = "extract-tweets";
    private static final String ANALYZE_TWEETS_COMMAND = "analyze-tweets";
    private static final CSVWriter csvWriter = null;
    private Main() {
    }

    public static void main(String[] args) throws Exception {
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

    private static void executeAnalyzeTweets() throws Exception {

        RetryPolicy retryPolicy = new RetryPolicy();
        GeminiAnalyser analyser = new GeminiAnalyser(retryPolicy);
        String tweetText = """
                   See eh, sometimes people get into relationships for reasons other than sex abeg.
                    The soul lifting conversations,
                    warm hugs, good food, companionship, those little little things dey priceless abeg.
                     Still bring the sex sha, just in case.
                   """;
        String tweetText1 = """
                This week, I’ve found myself spending more time on @Substack, and I genuinely love it.
                It feels quiet there,thoughtful. Unrushed.
                It’s not loud like Instagram.
                It’s not performative like Twitter.
                It feels like sitting with someone’s mind instead of watching them perform.,Fri Feb 20 09:44:17 +0000 2026
                """;
        String result = analyser.geminiAnalyser(tweetText);
        String result1 = analyser.geminiAnalyser(tweetText1);
        System.out.println(result);
        System.out.println(result1);
    }



    private static void executeExtractTweets() throws Exception {
        String path= "data/tweets/tweets.js";
        TweetParser tweetParser = new TweetParser(path,ParserType.CSV);
        tweetParser.parse();
        CSVWriter.create("data/tweets/transformed/tweets.csv",false);
        CSVWriter.writeList(tweetParser.parse());
        executeAnalyzeTweets();


    }

    private static void printUsage() {
        System.out.println("Usage: tweet-audit <command>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  extract-tweets  Extract tweets from Twitter archive");
        System.out.println("  analyze-tweets  Analyze tweets using Gemini AI");
    }

}