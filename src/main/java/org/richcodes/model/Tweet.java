package org.richcodes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Tweet(
        @JsonProperty("id_str")
        String id,
        @JsonProperty("full_text")
        String text,
        @JsonProperty("created_at")
        String createdAt


){}
