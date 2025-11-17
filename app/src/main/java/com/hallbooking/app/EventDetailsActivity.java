package com.hallbooking.app;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import helper.classes.DatabaseHelper;
import models.EventData;
import models.Venue;

public class EventDetailsActivity extends AppCompatActivity {

    private EventData hallData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView hallImage = findViewById(R.id.details_hall_image);
        TextView hallName = findViewById(R.id.details_hall_name);
        RatingBar averageRating = findViewById(R.id.details_average_rating);
        TextView hallLocation = findViewById(R.id.details_hall_location);
        TextView hallCapacity = findViewById(R.id.details_hall_capacity);
        TextView hallFee = findViewById(R.id.details_hall_fee);
        TextView ownerContact = findViewById(R.id.details_owner_contact);
        TextView ownerEmail = findViewById(R.id.details_owner_email);
        RatingBar submitRating = findViewById(R.id.details_submit_rating);
        Button submitRatingButton = findViewById(R.id.submit_rating_button);
        Button bookNowButton = findViewById(R.id.book_now_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String eventDataJson = extras.getString("data");
            if (eventDataJson != null) {
                hallData = new Gson().fromJson(eventDataJson, EventData.class);
                populateUI(hallData, hallImage, hallName, averageRating, hallLocation, hallCapacity, hallFee, ownerContact, ownerEmail);
            }
        }

        bookNowButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            long userId = prefs.getLong("user_id", -1);

            if (userId != -1) {
                showDatePickerDialog(userId);
            } else {
                // Not logged in, redirect to login
                Intent intent = new Intent(EventDetailsActivity.this, LoginActivityNew.class);
                startActivity(intent);
            }
        });

        submitRatingButton.setOnClickListener(v -> {
            float rating = submitRating.getRating();
            if (rating > 0) {
                new Thread(() -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(EventDetailsActivity.this);
                    dbHelper.addRating(Long.parseLong(hallData.get_id()), rating);
                    runOnUiThread(() -> {
                        Toast.makeText(EventDetailsActivity.this, "Thank you for your rating!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }).start();
            } else {
                Toast.makeText(this, "Please select a rating before submitting.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog(long userId) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    DatabaseHelper dbHelper = new DatabaseHelper(EventDetailsActivity.this);
                    if (dbHelper.isBooked(Long.parseLong(hallData.get_id()), selectedDate)) {
                        Toast.makeText(EventDetailsActivity.this, "This hall is already booked for the selected date.", Toast.LENGTH_LONG).show();
                    } else {
                        dbHelper.addBooking(userId, Long.parseLong(hallData.get_id()), selectedDate);
                        Toast.makeText(EventDetailsActivity.this, "Hall booked successfully for " + selectedDate, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void populateUI(EventData data, ImageView hallImage, TextView hallName, RatingBar averageRating, TextView hallLocation, TextView hallCapacity, TextView hallFee, TextView ownerContact, TextView ownerEmail) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(data.getEventName());
        }

        String imageFileName = data.getImageUrl();
        if (imageFileName != null && !imageFileName.isEmpty()) {
            File imageFile = new File(getFilesDir(), imageFileName);
            if (imageFile.exists()) {
                hallImage.setImageURI(Uri.fromFile(imageFile));
            } else {
                hallImage.setImageResource(R.mipmap.ic_launcher);
            }
        } else {
            hallImage.setImageResource(R.mipmap.ic_launcher);
        }

        hallName.setText(data.getEventName());
        averageRating.setRating(data.getAverageRating());

        Venue venue = data.getVenue();
        if (venue != null) {
            hallLocation.setText(venue.getArea() + ", " + venue.getCity());
        }

        hallCapacity.setText("Capacity: " + data.getDetails());
        hallFee.setText("Fee: " + data.getFee());
        ownerContact.setText("Contact: " + data.getCollege());
        ownerEmail.setText("Email: " + data.getOwnerEmail());
        ownerEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onSupportNavigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
