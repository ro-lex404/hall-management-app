package com.hallbooking.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import helper.classes.DatabaseHelper;
import models.EventData;
import models.Venue;

public class AddHallActivity extends AppCompatActivity {

    private ImageView hallImagePreview;
    private Uri selectedImageUri;

    // Launcher for getting content from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        // This is the crucial line that fixes the crash.
                        // It takes persistent permission to read the image URI.
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    } catch (SecurityException e) {
                        Log.e("AddHallActivity", "Failed to take persistent permission for URI", e);
                    }

                    selectedImageUri = uri;
                    hallImagePreview.setImageURI(uri);
                    hallImagePreview.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);

        EditText hallName = findViewById(R.id.hall_name);
        EditText hallLocation = findViewById(R.id.hall_location);
        EditText hallCity = findViewById(R.id.hall_city);
        EditText hallCapacity = findViewById(R.id.hall_capacity);
        EditText hallFee = findViewById(R.id.hall_fee);
        Button selectImageButton = findViewById(R.id.select_image_button);
        hallImagePreview = findViewById(R.id.hall_image_preview);
        EditText ownerContact = findViewById(R.id.owner_contact);
        EditText ownerEmail = findViewById(R.id.owner_email);
        Button addHallButton = findViewById(R.id.add_hall_button);

        selectImageButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        addHallButton.setOnClickListener(v -> {
            String name = hallName.getText().toString().trim();
            String location = hallLocation.getText().toString().trim();
            String city = hallCity.getText().toString().trim();
            String capacity = hallCapacity.getText().toString().trim();
            String feeStr = hallFee.getText().toString().trim();
            String imageUriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";
            String contact = ownerContact.getText().toString().trim();
            String email = ownerEmail.getText().toString().trim();

            if (name.isEmpty() || location.isEmpty() || city.isEmpty() || capacity.isEmpty() || feeStr.isEmpty()) {
                Toast.makeText(AddHallActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int fee = 0;
            try {
                fee = Integer.parseInt(feeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AddHallActivity.this, "Please enter a valid number for the fee.", Toast.LENGTH_SHORT).show();
                return;
            }

            EventData newHall = new EventData();
            Venue venue = new Venue();
            newHall.setEventName(name);
            venue.setArea(location);
            venue.setCity(city);
            newHall.setDetails(capacity);
            newHall.setFee(fee);
            newHall.setVenue(venue);

            DatabaseHelper dbHelper = new DatabaseHelper(AddHallActivity.this);
            dbHelper.addHall(newHall, imageUriString, contact, email);

            Toast.makeText(AddHallActivity.this, "Hall '" + name + "' has been added!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
