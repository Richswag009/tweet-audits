package org.richcodes.analyser;

import org.richcodes.model.AnalysisResult;

import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class RetryPolicy {
    private final int DEFAULT_MAX_RETRIES = 3;
    private final int DEFAULT_BASE_TIMEOUT = 37000; // milliseconds
    private final  int DEFAULT_BACKOFF_FACTOR = 2;
    private final int maxRetries;
    private final int baseTimeout;
    private final int backoffFactor;
    private static final Set<String> RETRYABLE_CODES = Set.of("429", "503", "504");

    public RetryPolicy() {
        this.maxRetries = DEFAULT_MAX_RETRIES;
        this.baseTimeout = DEFAULT_BASE_TIMEOUT;
        this.backoffFactor = DEFAULT_BACKOFF_FACTOR;
    }


    RetryPolicy(int maxRetries, int baseTimeout, int backoffFactor) {
        if(maxRetries < 1){
            throw new IllegalArgumentException("maxRetries must be greater than 0");
        }
        this.maxRetries=maxRetries;
        this.baseTimeout =baseTimeout;
        this.backoffFactor = backoffFactor;
    }



    public String retryPolicy(Supplier<String> action) throws Exception {
        int retries = 0;

        while (retries < maxRetries) {
            try {
                System.out.println("making request...");
                return action.get();

            }catch (Exception e) {
                if (!isRetryable(e.getMessage())) {
                    throw e;
                }
                System.out.println("Attempt " + (retries + 1) + " of " + maxRetries + " failed");
                retries++;

                if(retries < maxRetries) {
                    int backoff = calculateBackOffTime(retries);
                    System.out.println( "Retrying in " + backoff + " milliseconds" );
                    Thread.sleep(backoff);
                }else{
                    System.out.println("Max retries reached");
                    throw e;
                }
            }
        }

        return null;
    }


    private int calculateBackOffTime(int retries){
        return (int) (baseTimeout * Math.pow(backoffFactor,retries));
    }


    private boolean isRetryable(String message) {
        return RETRYABLE_CODES.stream()
                .anyMatch(message::startsWith);
    }
}
