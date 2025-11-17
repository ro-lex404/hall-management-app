package com.hallbooking.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import helper.classes.DatabaseHelper;

public class LoginActivityNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        setTitle("Login");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signupPrompt = findViewById(R.id.login_signup_prompt);

        loginButton.setOnClickListener(v -> {
            String emailStr = email.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();

            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.checkUser(emailStr, passwordStr);

            if (cursor != null && cursor.moveToFirst()) {
                long userId = cursor.getLong(cursor.getColumnIndex("_id"));
                String userName = cursor.getString(cursor.getColumnIndex("name"));
                String userContact = cursor.getString(cursor.getColumnIndex("contact"));
                String userEmail = cursor.getString(cursor.getColumnIndex("email"));
                cursor.close();

                // Save all user info to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putLong("user_id", userId);
                editor.putString("user_name", userName);
                editor.putString("user_contact", userContact);
                editor.putString("user_email", userEmail);
                editor.apply();

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(LoginActivityNew.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
            }
        });

        signupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivityNew.this, SignupActivityNew.class);
            startActivity(intent);
        });
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
