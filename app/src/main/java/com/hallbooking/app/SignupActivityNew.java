package com.hallbooking.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import helper.classes.DatabaseHelper;

public class SignupActivityNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        setTitle("Create Account");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText name = findViewById(R.id.signup_name);
        EditText contact = findViewById(R.id.signup_contact);
        EditText email = findViewById(R.id.signup_email);
        EditText password = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginPrompt = findViewById(R.id.signup_login_prompt);

        signupButton.setOnClickListener(v -> {
            String nameStr = name.getText().toString().trim();
            String contactStr = contact.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();

            if (nameStr.isEmpty() || contactStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            if (dbHelper.addUser(nameStr, contactStr, emailStr, passwordStr)) {
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                finish(); // Go back to login screen
            } else {
                Toast.makeText(this, "User already exists with this email.", Toast.LENGTH_SHORT).show();
            }
        });

        loginPrompt.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
