package org.richcodes.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Criteria(
        List<String> forbiddenWords,
        List<String> topicsToExclude,
        List<String> toneRequirements,
        String additionalInstructions) {

    public Criteria{
        forbiddenWords= safeList(forbiddenWords);
        topicsToExclude = safeList(topicsToExclude);
        toneRequirements = safeList(toneRequirements);
        additionalInstructions = additionalInstructions == null ? "" : additionalInstructions;
    }

    private static List<String> safeList(List<String> list) {
        return List.copyOf(Objects.requireNonNullElse(list, List.of()));
    }

    public static Criteria defaultCriteria() {
        return new Criteria(List.of("death"), List.of(
                "Political opinions","Personal attacks or insults","sports related banters"
        ), List.of("Respectful communications"), null);
    }

}
