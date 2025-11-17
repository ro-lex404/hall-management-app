package com.hallbooking.app;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import helper.classes.DatabaseHelper;
import models.EventData;
import models.Venue;

public class AddHallActivity extends AppCompatActivity {

    private ImageView hallImagePreview;
    private Uri tempImageUri; // Temporary storage for the selected image URI

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    tempImageUri = uri;
                    hallImagePreview.setImageURI(uri);
                    hallImagePreview.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);
        setTitle("Add New Hall");

        EditText hallName = findViewById(R.id.hall_name);
        EditText hallLocation = findViewById(R.id.hall_location);
        EditText hallCity = findViewById(R.id.hall_city);
        EditText hallCapacity = findViewById(R.id.hall_capacity);
        EditText hallFee = findViewById(R.id.hall_fee);
        Button selectImageButton = findViewById(R.id.select_image_button);
        hallImagePreview = findViewById(R.id.hall_image_preview);
        Button addHallButton = findViewById(R.id.add_hall_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        selectImageButton.setOnClickListener(v -> mGetContent.launch("image/*"));
        cancelButton.setOnClickListener(v -> finish());

        addHallButton.setOnClickListener(v -> {
            String name = hallName.getText().toString().trim();
            String location = hallLocation.getText().toString().trim();
            String city = hallCity.getText().toString().trim();
            String capacity = hallCapacity.getText().toString().trim();
            String feeStr = hallFee.getText().toString().trim();
            String imageFileName = saveImageToInternalStorage(tempImageUri);

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

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            long ownerId = prefs.getLong("user_id", -1);
            String ownerContact = prefs.getString("user_contact", "");
            String ownerEmail = prefs.getString("user_email", "");

            if (ownerId == -1) {
                Toast.makeText(this, "Error: Not logged in.", Toast.LENGTH_SHORT).show();
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
            dbHelper.addHall(ownerId, newHall, imageFileName, ownerContact, ownerEmail);

            Toast.makeText(AddHallActivity.this, "Hall '" + name + "' has been added!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private String saveImageToInternalStorage(Uri uri) {
        if (uri == null) return "";
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File destinationFile = new File(getFilesDir(), fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            if (inputStream == null) return "";
            try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                return fileName;
            }
        } catch (Exception e) {
            Log.e("AddHallActivity", "Error saving image", e);
            return "";
        }
    }
}
