package org.richcodes.application;

import org.richcodes.analyser.GeminiAnalyser;
import org.richcodes.analyser.RetryPolicy;
import org.richcodes.config.BatchProcessing;
import org.richcodes.config.Config;
import org.richcodes.enums.ParserType;
import org.richcodes.model.AnalysisResult;
import org.richcodes.model.CheckpointManager;
import org.richcodes.model.Tweet;
import org.richcodes.storage.CSVFileReader;
import org.richcodes.storage.CSVFileWriter;
import org.richcodes.storage.TweetParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TweetPipeline {

    private Config config;

    public TweetPipeline(Config config) {
        if(config == null) {
            throw new IllegalArgumentException("config is null");
        }
        this.config = config;
    }
    public void extract () throws Exception {
        String path= "data/tweets/tweets.js";
        TweetParser tweetParser = new TweetParser(path, ParserType.CSV);
        List<Tweet> tweets = tweetParser.parse();

        try (CSVFileWriter writer =
                     CSVFileWriter.create(config.transformedPath(), false)) {
            writer.writeList(tweets);
        }
    }

    public void analyze  () throws Exception {
        RetryPolicy retryPolicy = new RetryPolicy();
        GeminiAnalyser analyser = new GeminiAnalyser(retryPolicy);
        CSVFileReader.create(config.transformedPath());
        CheckpointManager checkpointManager = new CheckpointManager(config.checkpointPath());
        List<Tweet> tweets = CSVFileReader.readAll();
        BatchProcessing batchProcessing = new BatchProcessing(3);
        List<List<Tweet>> batches = batchProcessing.partition(tweets, 3);
        
        try (CSVFileWriter flaggedWriter = CSVFileWriter.create(config.flaggedPath(), true)) {
            for (List<Tweet> batch : batches) {
                System.out.println("Processing batch " + (batches.indexOf(batch) + 1) + " of " + batches.size());
                List<Future<AnalysisResult>> futures = new ArrayList<>();
                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    for (Tweet tweet : batch) {
                        if (!checkpointManager.contains(tweet.id())) {
                            futures.add(executor.submit(() -> {
                                AnalysisResult result = analyser.geminiAnalyser(tweet.text());
                                if (result.flagged()) {
                                    flaggedWriter.writeResult(tweet, result);
                                }
                                checkpointManager.save(tweet.id());
                                return result;
                            }));
                        }
                    }
                }

                for (Future<AnalysisResult> future : futures) {
                    try {
                        future.get();
                    } catch (Exception e) {
                        System.out.println("Task failed: " + e.getMessage());
                    }
                }
                System.out.println("Batch complete. Waiting 60 seconds...");
                Thread.sleep(60000);
            }
        }
        System.out.println("Total tweets processed: " + tweets.size());
        System.out.println("Check flagged.csv for tweets to delete.");
        System.out.println("All batches complete. Check flagged.csv for results.");
    }

}
