package com.hallbooking.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import models.EventData;
import models.Venue;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView hallImage = findViewById(R.id.details_hall_image);
        TextView hallName = findViewById(R.id.details_hall_name);
        TextView hallLocation = findViewById(R.id.details_hall_location);
        TextView hallCapacity = findViewById(R.id.details_hall_capacity);
        TextView hallFee = findViewById(R.id.details_hall_fee);
        TextView ownerContact = findViewById(R.id.details_owner_contact);
        TextView ownerEmail = findViewById(R.id.details_owner_email);
        Button bookNowButton = findViewById(R.id.book_now_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String eventDataJson = extras.getString("data");
            if (eventDataJson != null) {
                EventData data = new Gson().fromJson(eventDataJson, EventData.class);
                populateUI(data, hallImage, hallName, hallLocation, hallCapacity, hallFee, ownerContact, ownerEmail);
            }
        }

        bookNowButton.setOnClickListener(v -> {
            // Redirect to the login page
            Intent intent = new Intent(EventDetailsActivity.this, LoginActivityNew.class);
            startActivity(intent);
        });
    }

    private void populateUI(EventData data, ImageView hallImage, TextView hallName, TextView hallLocation, TextView hallCapacity, TextView hallFee, TextView ownerContact, TextView ownerEmail) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(data.getEventName());
        }

        String imageUriString = data.getImageUrl();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            hallImage.setImageURI(Uri.parse(imageUriString));
        }

        hallName.setText(data.getEventName());

        Venue venue = data.getVenue();
        if (venue != null) {
            hallLocation.setText(venue.getArea() + ", " + venue.getCity());
        } else {
            hallLocation.setText("Location not available");
        }

        hallCapacity.setText("Capacity: " + data.getDetails());
        hallFee.setText("Fee: " + data.getFee());
        ownerContact.setText("Contact: " + data.getCollege());
        ownerEmail.setVisibility(TextView.GONE);
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
