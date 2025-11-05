package com.nikith_shetty.vgroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivityNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signupPrompt = findViewById(R.id.login_signup_prompt);

        loginButton.setOnClickListener(v -> {
            // Placeholder for login logic. Replace with your authentication provider (e.g., Firebase).
            Toast.makeText(LoginActivityNew.this, "Login functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        signupPrompt.setOnClickListener(v -> {
            // Navigate to the Signup screen
            Intent intent = new Intent(LoginActivityNew.this, SignupActivityNew.class);
            startActivity(intent);
        });
    }
}
