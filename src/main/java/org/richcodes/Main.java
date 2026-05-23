package org.richcodes;

import org.richcodes.application.TweetPipeline;
import org.richcodes.config.Config;
import org.richcodes.storage.ConfigLoader;


public class Main {

    private static final String EXTRACT_TWEETS_COMMAND = "extract-tweets";
    private static final String ANALYZE_TWEETS_COMMAND = "analyze-tweets";

    private Main() {
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            printUsage();
            return;
        }
        String command = args[0];
        int batchSize = parseBatchSize(args);

        if(!command.equals(EXTRACT_TWEETS_COMMAND ) && !command.equals(ANALYZE_TWEETS_COMMAND)){
            System.err.println("Error: Unknown command '" + command + "'");
            printUsage();
            System.exit(1);
        }

        try {
            ConfigLoader configLoader = new ConfigLoader();
            Config config = configLoader.load();
            TweetPipeline tweetPipeline=  new TweetPipeline(config);

            switch (command) {
                case EXTRACT_TWEETS_COMMAND -> executeExtractTweets(tweetPipeline);
                case ANALYZE_TWEETS_COMMAND -> executeAnalyzeTweets(tweetPipeline);
            };

        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void executeAnalyzeTweets(TweetPipeline tweetPipeline) throws Exception {
        System.out.println("Executing analyze-tweets ....");
        tweetPipeline.analyze();
        System.out.println("analyze-tweets done.");
    }

    private static void executeExtractTweets(TweetPipeline tweetPipeline) throws Exception {
        tweetPipeline.extract();
    }

    private static void printUsage() {
        System.out.println("Usage: tweet-audit <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  extract-tweets                    Extract tweets from Twitter archive");
        System.out.println("  analyze-tweets                    Analyze tweets using Gemini AI");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --batch-size=<n>                  Number of tweets per batch (default: 3)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  tweet-audit analyze-tweets");
        System.out.println("  tweet-audit analyze-tweets --batch-size=5");
    }

    private static int parseBatchSize(String[] args) {
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--batch-size=")) {
                return Integer.parseInt(args[i].split("=")[1]);
            }
        }
        return 3;
    }

}