package org.richcodes.model;

public record Tweet(
        String id,
        String fullText,
        String createdAt,
        int favoriteCount,
        int retweetCount,
        String lang
) {
}
