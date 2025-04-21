package com.example.demo.services;


import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatGptService {
    private final OpenAiService client;

    public ChatGptService(@Value("${openai.api-key}") String apiKey) {
        this.client = new OpenAiService(apiKey);
    }

    public String chat(List<ChatMessage> history) {
        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(history)
                .maxTokens(500)
                .temperature(0.7)
                .build();
        ChatCompletionResult result = client.createChatCompletion(req);
        return result.getChoices().get(0).getMessage().getContent().trim();
    }
}
