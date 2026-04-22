package com.example.service;

import java.util.Locale;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import com.example.entity.TodoCategory;

@Service
public class TodoCategoryInferenceService {

    private final ChatClient chatClient;

    public TodoCategoryInferenceService(ObjectProvider<ChatClient.Builder> chatClientBuilderProvider) {
        ChatClient.Builder chatClientBuilder = chatClientBuilderProvider.getIfAvailable();
        this.chatClient = (chatClientBuilder != null) ? chatClientBuilder.build() : null;
    }

    public TodoCategory inferCategory(String title, String description) {
        if (chatClient == null) {
            return inferByKeyword(title, description);
        }

        String prompt = """
                You are a todo categorization assistant.
                Classify the todo into exactly one category from this list:
                WORK, PERSONAL, ENTERTAINMENT, HEALTH, OTHER.

                Respond with only the category name.

                Title: %s
                Description: %s
                """.formatted(safeText(title), safeText(description));

        try {
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
            TodoCategory parsedCategory = parseCategory(response);
            if (parsedCategory != null) {
                return parsedCategory;
            }
        } catch (RuntimeException ignored) {
            // Fall back when OpenAI is not configured or external call fails.
        }

        return inferByKeyword(title, description);
    }

    private TodoCategory inferByKeyword(String title, String description) {
        String text = (safeText(title) + " " + safeText(description)).toLowerCase(Locale.ROOT);

        if (containsAny(text, "meeting", "deadline", "office", "project", "client", "report", "work")) {
            return TodoCategory.WORK;
        }
        if (containsAny(text, "gym", "workout", "exercise", "doctor", "meditation", "health", "diet")) {
            return TodoCategory.HEALTH;
        }
        if (containsAny(text, "movie", "music", "game", "concert", "netflix", "entertainment")) {
            return TodoCategory.ENTERTAINMENT;
        }
        if (containsAny(text, "family", "friend", "home", "personal", "birthday", "call mom", "call dad")) {
            return TodoCategory.PERSONAL;
        }

        return TodoCategory.OTHER;
    }

    private TodoCategory parseCategory(String response) {
        if (response == null || response.isBlank()) {
            return null;
        }

        String normalized = response.trim().toUpperCase(Locale.ROOT);
        normalized = normalized.replaceAll("[^A-Z_]", "");
        if (normalized.isBlank()) {
            return null;
        }

        try {
            return TodoCategory.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String safeText(String text) {
        return (text == null) ? "" : text;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}