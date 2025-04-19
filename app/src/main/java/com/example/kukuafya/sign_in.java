package com.example.kukuafya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class sign_in extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CardView cardSignIn;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cardSignIn = findViewById(R.id.cardSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);

        cardSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences loginCredentials = getSharedPreferences("logininfo", MODE_PRIVATE);
            String savedEmail = loginCredentials.getString("email", "");
            String savedPassword = loginCredentials.getString("password", "");

            if (email.equals(savedEmail) && password.equals(savedPassword)) {
                // Mark as logged in
                SharedPreferences.Editor editor = loginCredentials.edit();
                editor.putString("status", "logged in");
                editor.apply();

                // Go to main activity
                Intent intent = new Intent(sign_in.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(sign_in.this, sign_up.class);
            startActivity(intent);
        });
    }
}