package com.hallbooking.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivityNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        // Set the title of the page
        setTitle("Sign Up");

        // Add back arrow to the app bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText email = findViewById(R.id.signup_email);
        EditText password = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginPrompt = findViewById(R.id.signup_login_prompt);

        signupButton.setOnClickListener(v -> {
            Toast.makeText(SignupActivityNew.this, "Signup functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        loginPrompt.setOnClickListener(v -> {
            // Finish this activity to return to the Login screen
            finish();
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
