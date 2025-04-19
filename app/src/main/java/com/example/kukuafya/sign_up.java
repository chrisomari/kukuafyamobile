package com.example.kukuafya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class sign_up extends AppCompatActivity {

    private EditText etEmail, etPassword, etUsername;
    private CardView signup_cv;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        signIn = findViewById(R.id.tvSignIn);
        signup_cv = findViewById(R.id.cardCreateAccount);

        signup_cv.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String username = etUsername.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // Save credentials
                SharedPreferences loginCredentials = getSharedPreferences("logininfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.putString("password", password);
                editor.putString("email", email);
                editor.putString("username", username);
                editor.putString("status", "logged in");
                editor.apply();

                // Go to main activity
                Intent intent = new Intent(sign_up.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(sign_up.this, sign_in.class);
            startActivity(intent);
            finish();
        });
    }
}