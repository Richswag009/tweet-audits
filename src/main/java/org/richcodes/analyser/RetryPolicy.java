package org.richcodes.analyser;

import org.richcodes.model.AnalysisResult;

import java.util.Random;
import java.util.function.Supplier;

public class RetryPolicy {
    private int MAX_RETRIES = 3;
    private int BASE_TIMEOUT = 1000; // milliseconds
    private int BACKOFF_FACTOR = 2;

    RetryPolicy(int maxRetries, int baseTimeout, int backoffFactor) {
        if(maxRetries < 1){
            throw new IllegalArgumentException("maxRetries must be greater than 0");
        }
        this.MAX_RETRIES=maxRetries;
        this.BASE_TIMEOUT =baseTimeout;
        this.BACKOFF_FACTOR = backoffFactor;
    }

    public RetryPolicy() {

    }

    public String retryPolicy(Supplier<String> action) throws Exception {
        int retries = 0;
        Random random = new Random();
        while (retries < MAX_RETRIES) {
            try {
                System.out.println("making request...");
                String response = action.get();

            }catch (Exception e) {
                System.out.println("Attempt " + retries + " of " + MAX_RETRIES + " failed" + e.getMessage());
                retries++;

                if(retries < MAX_RETRIES) {
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
        return (int) (BASE_TIMEOUT * Math.pow(BACKOFF_FACTOR,retries));
    }

}
