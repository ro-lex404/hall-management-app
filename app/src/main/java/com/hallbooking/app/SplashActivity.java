package com.hallbooking.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_splash);

        // Instead of downloading data, show the role selection dialog immediately.
        showRoleSelectionDialog();
    }

    private void showRoleSelectionDialog() {
        final CharSequence[] roles = {"User", "Owner"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Your Role");
        builder.setItems(roles, (dialog, which) -> {
            if (roles[which].equals("User")) {
                // Navigate to the main screen to view bookings
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (roles[which].equals("Owner")) {
                // Navigate to the page for adding a new hall
                Intent intent = new Intent(SplashActivity.this, AddHallActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Prevent the user from dismissing the dialog by tapping outside of it
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No handler to clean up anymore, but good practice to keep the method.
    }
}
