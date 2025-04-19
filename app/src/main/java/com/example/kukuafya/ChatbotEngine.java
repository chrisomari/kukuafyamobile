package com.example.kukuafya;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatbotEngine {

    private Context context;
    private Executor executor;
    private Handler mainHandler;
    private PoultryKnowledgeBase knowledgeBase;

    public interface ResponseCallback {
        void onResponse(String response);
    }

    public ChatbotEngine(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.knowledgeBase = new PoultryKnowledgeBase();
    }

    public void getResponse(String userMessage, ResponseCallback callback) {
        executor.execute(() -> {
            // Process the user message and generate a response
            String response = processUserMessage(userMessage);

            // Post the response back to the main thread
            mainHandler.post(() -> {
                callback.onResponse(response);
            });
        });
    }

    private String processUserMessage(String userMessage) {
        // Convert to lowercase for easier matching
        String lowerCaseMessage = userMessage.toLowerCase();

        // Check for greetings
        if (containsAny(lowerCaseMessage, "hello", "hi", "hey", "jambo", "habari")) {
            return "Hello! How can I help you with your poultry farming today?";
        }

        // Check for breed information request
        if (containsAny(lowerCaseMessage, "breed", "breeds", "types of chicken")) {
            return knowledgeBase.getBreedInformation();
        }

        // Check for housing information request
        if (containsAny(lowerCaseMessage, "house", "housing", "coop", "shelter")) {
            return knowledgeBase.getHousingInformation();
        }

        // Check for feeding information request
        if (containsAny(lowerCaseMessage, "feed", "feeding", "food", "nutrition")) {
            return knowledgeBase.getFeedingInformation();
        }

        // Check for disease information request
        if (containsAny(lowerCaseMessage, "disease", "illness", "sick", "health", "medication", "treatment")) {
            return knowledgeBase.getDiseaseInformation();
        }

        // Check for business information request
        if (containsAny(lowerCaseMessage, "business", "profit", "market", "sell", "income")) {
            return knowledgeBase.getBusinessInformation();
        }

        // Check for biosecurity information request
        if (containsAny(lowerCaseMessage, "biosecurity", "protection", "safety")) {
            return knowledgeBase.getBiosecurityInformation();
        }

        // Check for suggestion request
        if (containsAny(lowerCaseMessage, "suggest", "tip", "advice", "help")) {
            return knowledgeBase.getRandomTip();
        }

        // Default response if no specific topic is matched
        return "I'm here to help with all aspects of poultry farming! You can ask me about chicken breeds, housing, feeding, disease management, or business aspects of poultry farming. What would you like to know more about?";
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