package org.richcodes.config;

public class Config {
    private final String archivePath;
    private final String transformedPath;
    private final String flaggedPath;
    private final String checkpointPath;
    private final int batchSize;
    private final long batchDelayMs;
    private final String username;
    private final String geminiApiKey;
    private final String geminiModel;
    private final String baseUrl;
    private final Criteria criteria;


    public Config(Builder builder) {
        this.archivePath = builder.archivePath;
        this.transformedPath = builder.transformedPath;
        this.flaggedPath = builder.flaggedPath;
        this.checkpointPath = builder.checkpointPath;
        this.batchSize = builder.batchSize;
        this.batchDelayMs = builder.batchDelayMs;
        this.username = builder.username;
        this.geminiApiKey = builder.geminiApiKey;
        this.geminiModel = builder.geminiModel;
        this.baseUrl = builder.baseTwitterUrl;
        this.criteria= builder.criteria;

    }


    public static Builder builder() {
        return new Builder();
    }

    public String archivePath() {
        return archivePath;
    }

    public String transformedPath() {
        return transformedPath;
    }

    public String flaggedPath() {
        return flaggedPath;
    }

    public String checkpointPath() {
        return checkpointPath;
    }

    public int batchSize() {
        return batchSize;
    }

    public long batchDelayMs() {
        return batchDelayMs;
    }

    public String username() {
        return username;
    }

    public String geminiApiKey() {
        return geminiApiKey;
    }

    public String geminiModel() {
        return geminiModel;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
    public Criteria criteria() {
        return criteria;
    }

    public String tweetUrl(String tweetId) {
        return baseUrl + "/" + username + "/status/" + tweetId;
    }

    public static class Builder {
        private String archivePath = "data/tweets/tweets.js";
        private String transformedPath = "data/tweets/transformed/tweets.csv";
        private String flaggedPath = "data/tweets/processed/flagged.csv";
        private String checkpointPath = "data/checkpoint.txt";
        private String baseTwitterUrl = "https://x.com";
        private String username = "user";
        private String geminiApiKey = "";
        private String geminiModel = "gemini-2.5-flash";
        public int batchSize = 3;
        private final long batchDelayMs = 60000;
        public  Criteria criteria = Criteria.defaultCriteria();


        public Builder archivePath(String path) {
             this.archivePath = path;
             return this;
        }

        public Builder transformedPath(String path) {
            this.transformedPath = path;
            return  this;
        }

        public Builder flaggedPath(String path) {
            this.flaggedPath = path;
            return this;
        }

        public Builder checkpointPath(String path) {
            this.checkpointPath = path;
            return this;
        }

        public Builder baseTwitterUrl(String url) {
            this.baseTwitterUrl = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder geminiApiKey(String apiKey) {
            this.geminiApiKey = apiKey;
            return this;
        }

        public Builder geminiModel(String model) {
            this.geminiModel = model;
            return this;
        }

        public Builder batchSize(int size) {
            this.batchSize = size;
            return  this;
        }

        public Builder getBatchDelayMs() {
            return this;
        }

        public Builder criteria(Criteria criteria) {
            this.criteria = criteria;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
