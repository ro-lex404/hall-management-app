package com.nikith_shetty.vgroup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import models.EventData;
import models.Venue;

public class EventDetailsActivity extends AppCompatActivity {

    private EventData data;
    private TextView eventNameTxt;
    private TextView collegeTxt;
    private TextView feeTxt;
    private TextView detailsTxt;
    private TextView venueTxt;
    private LinearLayout fee_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views using findViewById
        eventNameTxt = findViewById(R.id.details_event_name);
        collegeTxt = findViewById(R.id.details_college);
        feeTxt = findViewById(R.id.details_fee);
        detailsTxt = findViewById(R.id.details_details);
        venueTxt = findViewById(R.id.details_venue);
        fee_layout = findViewById(R.id.details_fee_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String eventDataJson = extras.getString("data");
            if (eventDataJson != null) {
                data = new Gson().fromJson(eventDataJson, EventData.class);
            }
        }

        if (data != null) {
            populateUI();
        }
    }

    private void populateUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(data.getEventName());
        }

        eventNameTxt.setText(data.getEventName());
        collegeTxt.setText(data.getCollege());

        try {
            int fee = Integer.parseInt(data.getFee());
            if (fee == 0) {
                fee_layout.setVisibility(View.GONE);
            } else {
                feeTxt.setText(data.getFee());
            }
        } catch (NumberFormatException e) {
            // If fee is not a valid number, hide the layout
            fee_layout.setVisibility(View.GONE);
        }

        detailsTxt.setText(data.getDetails());

        Venue venue = data.getVenue();
        if (venue != null) {
            String venueString = venue.getStreetAddr() + ", " + venue.getArea() + ", " + venue.getCity() + ", " + venue.getState() + "\n" + venue.getPincode();
            venueTxt.setText(venueString);
        } else {
            venueTxt.setText("Venue information not available");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            onSupportNavigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // This ensures the Up button behaves like the back button
        onBackPressed();
        return true;
    }
}
