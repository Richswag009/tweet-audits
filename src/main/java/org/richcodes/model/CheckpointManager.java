package org.richcodes.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CheckpointManager {

    private final Path checkPointPath;
//    private final Set<String> processedIds;
    private Set<String> processedIds = ConcurrentHashMap.newKeySet();

    public CheckpointManager(String path) throws IOException {
        this.checkPointPath = Paths.get(path);
        this.processedIds = load();
    }


    private HashSet<String> load() throws IOException {
        Path parent = checkPointPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(checkPointPath)) {
            Files.createFile(checkPointPath);
            return new HashSet<>();
        }
        return new HashSet<>(Files.readAllLines(checkPointPath));
    }

    public boolean contains(String tweetId){
       return processedIds.contains(tweetId);
    }


    public void save(String tweetId) throws IOException {
        Files.writeString(checkPointPath,tweetId + "\n", StandardOpenOption.APPEND);
        processedIds.add(tweetId);
    }


}
