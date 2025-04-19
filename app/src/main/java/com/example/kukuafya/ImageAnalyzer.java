package com.example.kukuafya;

import android.graphics.Bitmap;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageAnalyzer {
    private static final String API_URL = "https://chicken-health-api.onrender.com";

    public interface AnalysisCallback {
        void onAnalysisComplete(String result);
        void onError(String error);
    }

    public void analyze(Bitmap image, AnalysisCallback callback) {
        new Thread(() -> {
            try {
                // Convert bitmap to byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Create request body
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                                "image",
                                "feces.jpg",
                                RequestBody.create(byteArray, MediaType.parse("image/jpeg"))
                        )
                        .build();

                // Create request
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .build();

                // Execute request with timeout settings
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseBody = response.body().string();
                    String formattedResponse = formatResponse(responseBody);
                    callback.onAnalysisComplete(formattedResponse);
                }

            } catch (Exception e) {
                callback.onError("Analysis failed: " + e.getMessage());
            }
        }).start();
    }

    private String formatResponse(String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            StringBuilder result = new StringBuilder();

            if (response.has("healthStatus")) {
                result.append("Health Status: ")
                        .append(response.getString("healthStatus"))
                        .append("\n\n");
            }

            if (response.has("confidence")) {
                result.append("Confidence: ")
                        .append(response.getString("confidence"))
                        .append("\n\n");
            }

            if (response.has("recommendation")) {
                result.append("Recommendation: ")
                        .append(response.getString("recommendation"));
            }

            return result.toString();
        } catch (JSONException e) {
            return "Error: Could not match close image, try another image"; //"Error: Could not parse server response"
        }
    }
}