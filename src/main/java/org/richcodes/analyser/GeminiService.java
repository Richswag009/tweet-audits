package org.richcodes.analyser;

import com.google.genai.Client;
import com.google.genai.errors.ClientException;
import com.google.genai.types.GenerateContentResponse;

public class GeminiService implements GeminiClient {

    private final Client client;

    public GeminiService(Client client) {
        this.client = client;
    }

    @Override
    public String generateContent(String content) throws Exception {
        if(content == null || content.isBlank()) {
            throw new Exception("content is empty");
        }

        try{
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            content,
                            null
                    );
            return response.text();
        } catch (Exception e) {
            System.out.println("error generating content: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
