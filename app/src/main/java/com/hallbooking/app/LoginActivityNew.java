package com.hallbooking.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

        // Set the title of the page
        setTitle("Login");

        // Add back arrow to the app bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signupPrompt = findViewById(R.id.login_signup_prompt);

        loginButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivityNew.this, "Login functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        signupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivityNew.this, SignupActivityNew.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Finish the activity when the back arrow is pressed
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
