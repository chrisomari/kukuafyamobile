package com.example.kukuafya;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String API_URL = "https://chicken-health-api.onrender.com/analyze";

    // Response model class
    public static class AnalysisResult {
        public boolean isFeces;
        public String healthStatus;
        public String confidence;
        public String description;
        public String recommendation;
        public String error;

        public boolean hasError() {
            return error != null && !error.isEmpty();
        }
    }

    // Interface for callback
    public interface AnalysisCallback {
        void onSuccess(AnalysisResult result);
        void onFailure(String errorMessage);
    }

    // Analyze image from URI
    public static void analyzeImage(final Context context, final Uri imageUri, final AnalysisCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a temporary file from the URI
                    File imageFile = createTempFileFromUri(context, imageUri);
                    if (imageFile == null) {
                        throw new IOException("Failed to create temporary file from URI");
                    }

                    Log.d(TAG, "Created temp file: " + imageFile.getAbsolutePath() + ", size: " + imageFile.length() + " bytes");

                    // Create multipart request
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .build();

                    // Create request body
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", "image.jpg",
                                    RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                            .build();

                    // Build request
                    Request request = new Request.Builder()
                            .url(API_URL)
                            .post(requestBody)
                            .build();

                    Log.d(TAG, "Sending multipart request to: " + API_URL);
                    Response response = client.newCall(request).execute();

                    final String responseString;
                    if (response.body() != null) {
                        responseString = response.body().string();
                    } else {
                        responseString = "No response body";
                    }

                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response body: " + (responseString.length() > 100 ?
                            responseString.substring(0, 100) + "..." : responseString));

                    // Clean up temp file
                    imageFile.delete();

                    if (response.isSuccessful()) {
                        // Parse response
                        final AnalysisResult result = parseResponse(responseString);

                        // Return result on main thread
                        if (context instanceof android.app.Activity) {
                            ((android.app.Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(result);
                                }
                            });
                        }
                    } else {
                        // Try to parse error message from response
                        String errorDetail = "";
                        try {
                            JSONObject errorJson = new JSONObject(responseString);
                            if (errorJson.has("error")) {
                                errorDetail = errorJson.getString("error");
                            } else if (errorJson.has("message")) {
                                errorDetail = errorJson.getString("message");
                            }
                        } catch (JSONException e) {
                            errorDetail = responseString;
                        }

                        final String errorMessage = "Server error " + response.code() + ": " + errorDetail;
                        Log.e(TAG, errorMessage);

                        if (context instanceof android.app.Activity) {
                            ((android.app.Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(errorMessage);
                                }
                            });
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception: " + e.getMessage());

                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("Error: " + e.getMessage());
                            }
                        });
                    }
                }
            }
        }).start();
    }

    // Create a temporary file from URI
    private static File createTempFileFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            return null;
        }

        File outputFile = File.createTempFile("image", ".jpg", context.getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return outputFile;
    }

    // Parse JSON response
    private static AnalysisResult parseResponse(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        AnalysisResult result = new AnalysisResult();

        if (json.has("error")) {
            result.error = json.getString("error");
            return result;
        }

        if (json.has("isFeces")) result.isFeces = json.getBoolean("isFeces");
        if (json.has("healthStatus")) result.healthStatus = json.getString("healthStatus");
        if (json.has("confidence")) result.confidence = json.getString("confidence");
        if (json.has("description")) result.description = json.getString("description");
        if (json.has("recommendation")) result.recommendation = json.getString("recommendation");

        return result;
    }

    // Method to try different API formats for debugging
    public static void testApiFormats(final Context context, final Uri imageUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Test multipart form data
                    testMultipartFormData(context, imageUri);

                    // Test direct file upload
                    testDirectFileUpload(context, imageUri);

                    // Test URL-encoded form
                    testUrlEncodedForm(context, imageUri);

                } catch (Exception e) {
                    Log.e(TAG, "Test failed: " + e.getMessage(), e);
                }
            }
        }).start();
    }

    private static void testMultipartFormData(Context context, Uri imageUri) {
        try {
            Log.d(TAG, "Testing multipart form data upload");

            File imageFile = createTempFileFromUri(context, imageUri);
            if (imageFile == null) {
                Log.e(TAG, "Failed to create temp file");
                return;
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Test with field name "image"
            RequestBody requestBody1 = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                    .build();

            Request request1 = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody1)
                    .build();

            Response response1 = client.newCall(request1).execute();
            String responseBody1 = response1.body() != null ? response1.body().string() : "No response body";

            Log.d(TAG, "Multipart 'image' response: " + response1.code() + " - " + responseBody1);

            // Test with field name "file"
            RequestBody requestBody2 = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "image.jpg",
                            RequestBody.create(imageFile, MediaType.parse("image/jpeg")))
                    .build();

            Request request2 = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody2)
                    .build();

            Response response2 = client.newCall(request2).execute();
            String responseBody2 = response2.body() != null ? response2.body().string() : "No response body";

            Log.d(TAG, "Multipart 'file' response: " + response2.code() + " - " + responseBody2);

            imageFile.delete();

        } catch (Exception e) {
            Log.e(TAG, "Multipart test failed: " + e.getMessage(), e);
        }
    }

    private static void testDirectFileUpload(Context context, Uri imageUri) {
        try {
            Log.d(TAG, "Testing direct file upload");

            File imageFile = createTempFileFromUri(context, imageUri);
            if (imageFile == null) {
                Log.e(TAG, "Failed to create temp file");
                return;
            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body() != null ? response.body().string() : "No response body";

            Log.d(TAG, "Direct file upload response: " + response.code() + " - " + responseBody);

            imageFile.delete();

        } catch (Exception e) {
            Log.e(TAG, "Direct file upload test failed: " + e.getMessage(), e);
        }
    }

    private static void testUrlEncodedForm(Context context, Uri imageUri) {
        try {
            Log.d(TAG, "Testing URL-encoded form");

            // Load bitmap from URI
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            // Convert to base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image_data", base64Image)
                    .build();

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body() != null ? response.body().string() : "No response body";

            Log.d(TAG, "URL-encoded form response: " + response.code() + " - " + responseBody);

        } catch (Exception e) {
            Log.e(TAG, "URL-encoded form test failed: " + e.getMessage(), e);
        }
    }
}