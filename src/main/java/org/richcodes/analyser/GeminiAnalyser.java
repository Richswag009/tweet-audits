package org.richcodes.analyser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import org.richcodes.model.AnalysisResult;


public class GeminiAnalyser {

    private Client client;
    private final GeminiService geminiService;
    private final RetryPolicy retryPolicy;
    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiAnalyser( RetryPolicy retryPolicy ) {

        String apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GOOGLE_API_KEY not set");
        }
        this.client = Client.builder().apiKey(apiKey).build();
        this.geminiService = new GeminiService(client);
        this.retryPolicy=retryPolicy;
    }

    public AnalysisResult geminiAnalyser(String tweetText) throws Exception {
        String prompt = buildResponse(tweetText);
        return retryPolicy.retryPolicy(() ->
                cleanResponse(geminiService.generateContent(prompt))
        );
    }

    private AnalysisResult cleanResponse(String response) throws JsonProcessingException {
        String clean = response
                .replace("```json", "")
                .replace("```", "")
                .trim();
        return mapper.readValue(clean, AnalysisResult.class);
    }

    private String buildResponse (String tweetText){
        String content = """
            You are reviewing tweets for a professional's digital cleanup.
       
            Analyze this tweet and decide if it should be deleted based on:
            - Unprofessional language
            - Controversial opinions
            - Content that could harm professional reputation
       
            Tweet: "%s"
      
            Respond only in JSON with two fields:
            flagged (boolean) and reason (string).
        """;
       return String.format(content, tweetText);
    }

}
