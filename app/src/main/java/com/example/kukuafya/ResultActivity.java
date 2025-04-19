package com.example.kukuafya;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    private ImageView imageView;
    private TextView statusTextView;
    private TextView confidenceTextView;
    private TextView recommendationTextView;
    private TextView errorTextView;
    private Button newAnalysisButton;
    private Button testApiButton;
    private ProgressBar progressBar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        imageView = findViewById(R.id.imageView);
        statusTextView = findViewById(R.id.statusTextView);
        confidenceTextView = findViewById(R.id.confidenceTextView);
        recommendationTextView = findViewById(R.id.recommendationTextView);
        errorTextView = findViewById(R.id.errorTextView);
        newAnalysisButton = findViewById(R.id.newAnalysisButton);
        testApiButton = findViewById(R.id.testApiButton);
        progressBar = findViewById(R.id.progressBar);

        // Get image URI from intent
        String uriString = getIntent().getStringExtra("imageUri");
        if (uriString != null) {
            imageUri = Uri.parse(uriString);

            // Display the image
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                inputStream.close();

                // Analyze the image
                analyzeImage();
            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                showError("Error loading image: " + e.getMessage());
            }
        } else {
            showError("No image selected");
            finish();
        }

        // Set up button click listeners
        newAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to main activity
            }
        });

        testApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResultActivity.this, "Testing API formats, check logs", Toast.LENGTH_SHORT).show();
                ApiClient.testApiFormats(ResultActivity.this, imageUri);
            }
        });
    }

    private void analyzeImage() {
        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        statusTextView.setVisibility(View.GONE);
        confidenceTextView.setVisibility(View.GONE);
        recommendationTextView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        // Call API to analyze image
        ApiClient.analyzeImage(this, imageUri, new ApiClient.AnalysisCallback() {
            @Override
            public void onSuccess(ApiClient.AnalysisResult result) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);

                // Check for errors
                if (result.hasError()) {
                    showError("API Error: " + result.error);
                    return;
                }

                // Display results
                statusTextView.setVisibility(View.VISIBLE);
                confidenceTextView.setVisibility(View.VISIBLE);
                recommendationTextView.setVisibility(View.VISIBLE);

                // Set health status with appropriate color
                if (!result.isFeces) {
                    statusTextView.setText("Not Chicken Feces");
                    statusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    statusTextView.setText(capitalizeFirstLetter(result.healthStatus));

                    // Set color based on health status
                    if ("healthy".equalsIgnoreCase(result.healthStatus)) {
                        statusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else {
                        statusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                }

                // Set confidence and recommendation
                confidenceTextView.setText("Confidence: " + capitalizeFirstLetter(result.confidence));
                recommendationTextView.setText(result.recommendation);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);

                // Show error message
                showError(errorMessage);
            }
        });
    }

    private void showError(String message) {
        Log.e(TAG, "Error: " + message);
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        statusTextView.setVisibility(View.GONE);
        confidenceTextView.setVisibility(View.GONE);
        recommendationTextView.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}