package org.richcodes.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.richcodes.config.Config;
import org.richcodes.config.Criteria;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ConfigLoader {

    private static final String configFile = "config.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Config load() {
        return load(configFile);
    }

    public Config load(String configFilePath) {
        Config.Builder builder = Config.builder();
        loadFromEnvironment(builder);
        Criteria criteria = loadCriteria(configFilePath);
        if (criteria != null) {
            builder.criteria(criteria);
        }
        return builder.build();
    }

    public Criteria loadCriteria(String filePath) {
        try{
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return null;
            }
            return objectMapper.readValue(path.toFile(),Criteria.class);
        }catch (Exception e){
            return null;
        }
    }

    private void loadFromEnvironment(Config.Builder builder) {
        getEnv("X_USERNAME").ifPresent(builder::username);
        getEnv("GEMINI_API_KEY").ifPresent(builder::geminiApiKey);
        getEnv("GEMINI_MODEL").ifPresent(builder::geminiModel);
        getEnv("BATCH_SIZE").ifPresent(value -> builder.batchSize(Integer.parseInt(value)));
        getEnv("TWEETS_ARCHIVE_PATH").ifPresent(builder::archivePath);
        getEnv("TRANSFORMED_TWEETS_PATH").ifPresent(builder::transformedPath);
        getEnv("CHECKPOINT_PATH").ifPresent(builder::checkpointPath);
        getEnv("PROCESSED_RESULTS_PATH").ifPresent(builder::flaggedPath);
    }

    private Optional<String> getEnv(String name){
        String value = System.getenv(name);
        if(value == null || value.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(value);
    }

}
