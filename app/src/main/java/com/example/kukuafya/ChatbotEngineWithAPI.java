package com.example.kukuafya;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotEngineWithAPI {
    private static final String TAG = "PoultryChatbot";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String BASE_URL = "https://poultry-chatbot-1.onrender.com";

    private final OkHttpClient client;
    private final Executor executor;
    private final Handler mainHandler;

    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String errorMessage);
    }

    public ChatbotEngineWithAPI(Context context) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getResponse(String userMessage, ResponseCallback callback) {
        executor.execute(() -> {
            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("question", userMessage); // Match your backend's expected field

                Request request = new Request.Builder()
                        .url(BASE_URL + "/api/chat")
                        .post(RequestBody.create(requestBody.toString(), JSON))
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                executeRequestWithRetry(request, callback, 0);
            } catch (JSONException e) {
                notifyError(callback, "Failed to create request: " + e.getMessage());
            }
        });
    }

    private void executeRequestWithRetry(Request request, ResponseCallback callback, int attempt) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (attempt < 2) { // Retry twice
                    mainHandler.postDelayed(() ->
                                    executeRequestWithRetry(request, callback, attempt + 1),
                            1000 // 1 second delay between retries
                    );
                } else {
                    notifyError(callback, "Network error. Please check your connection");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        notifyError(callback, "Server error: " + response.code());
                        return;
                    }

                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String botResponse = jsonResponse.optString("response",
                            "Sorry, I couldn't process your request");

                    notifySuccess(botResponse, callback);
                } catch (JSONException e) {
                    notifyError(callback, "Invalid response format");
                } finally {
                    response.close();
                }
            }
        });
    }

    private void notifySuccess(String response, ResponseCallback callback) {
        mainHandler.post(() -> {
            try {
                callback.onResponse(response);
            } catch (Exception e) {
                Log.e(TAG, "Callback error", e);
            }
        });
    }

    private void notifyError(ResponseCallback callback, String error) {
        Log.e(TAG, error);
        mainHandler.post(() -> {
            try {
                callback.onError(error);
                callback.onResponse("Sorry, I couldn't process your request");
            } catch (Exception e) {
                Log.e(TAG, "Error callback failed", e);
            }
        });
    }

    // Optional: Health check endpoint
    public void checkServerHealth(ResponseCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/ping") // Ensure your backend has this endpoint
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyError(callback, "Connection test failed");
            }

            @Override
            public void onResponse(Call call, Response response) {
                notifySuccess(response.isSuccessful() ? "Server is online" : "Server error", callback);
                response.close();
            }
        });
    }
}