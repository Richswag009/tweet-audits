package org.richcodes.analyser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import org.richcodes.model.AnalysisResult;

public class GeminiAnalyser {


    private final Client client;
    private final GeminiService geminiService;
    private final RetryPolicy retryPolicy;

    public GeminiAnalyser( RetryPolicy retryPolicy ) {
        this.retryPolicy=retryPolicy;
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY not set");
        }
        this.client = Client.builder().apiKey(apiKey).build();
        this.geminiService = new GeminiService(client);
    }


    public String geminiAnalyser( String tweetText) throws Exception {
        String prompt = buildResponse(tweetText);
        return retryPolicy.retryPolicy(()-> {
            try {
                return String.valueOf(cleanResponse(geminiService.generateContent(prompt)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }



    private AnalysisResult cleanResponse(String response) throws JsonProcessingException {
        String clean = response
                .replace("```json", "")
                .replace("```", "")
                .trim();

        ObjectMapper mapper = new ObjectMapper();
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
