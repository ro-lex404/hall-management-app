package com.nikith_shetty.vgroup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import helper.classes.DatabaseHelper;
import models.EventData;
import models.Venue;

public class AddHallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);

        EditText hallName = findViewById(R.id.hall_name);
        EditText hallLocation = findViewById(R.id.hall_location);
        EditText hallCity = findViewById(R.id.hall_city);
        EditText hallCapacity = findViewById(R.id.hall_capacity);
        Button addHallButton = findViewById(R.id.add_hall_button);

        addHallButton.setOnClickListener(v -> {
            String name = hallName.getText().toString().trim();
            String location = hallLocation.getText().toString().trim();
            String city = hallCity.getText().toString().trim();
            String capacity = hallCapacity.getText().toString().trim();

            if (name.isEmpty() || location.isEmpty() || city.isEmpty() || capacity.isEmpty()) {
                Toast.makeText(AddHallActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an EventData object to hold the new hall's details
            EventData newHall = new EventData();
            Venue venue = new Venue();
            newHall.setEventName(name); // Using EventName to store Hall Name
            venue.setArea(location);
            venue.setCity(city);
            newHall.setDetails(capacity); // Using Details to store Capacity
            newHall.setVenue(venue);

            // Save the new hall to the database
            DatabaseHelper dbHelper = new DatabaseHelper(AddHallActivity.this);
            dbHelper.addHall(newHall);

            Toast.makeText(AddHallActivity.this, "Hall '" + name + "' has been added!", Toast.LENGTH_LONG).show();
            finish(); // Close the activity and return to the main screen
        });
    }
}