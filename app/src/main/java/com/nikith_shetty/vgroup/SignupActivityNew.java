package com.nikith_shetty.vgroup;

import android.os.Bundle;
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

        EditText email = findViewById(R.id.signup_email);
        EditText password = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginPrompt = findViewById(R.id.signup_login_prompt);

        signupButton.setOnClickListener(v -> {
            // Placeholder for signup logic. Replace with your authentication provider (e.g., Firebase).
            Toast.makeText(SignupActivityNew.this, "Signup functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        loginPrompt.setOnClickListener(v -> {
            // Finish this activity to return to the Login screen
            finish();
        });
    }
}
