package helper.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import models.EventData;
import models.Venue;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HallBooking.db";
    // Incrementing the version to 9 to ensure all schema changes are applied.
    private static final int DATABASE_VERSION = 9;

    private static final String TABLE_HALLS = "halls";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HALL_NAME = "hall_name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_FEE = "fee";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_CONTACT_NUMBER = "contact_number";
    private static final String COLUMN_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HALLS_TABLE = "CREATE TABLE " + TABLE_HALLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HALL_NAME + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_CAPACITY + " INTEGER,"
                + COLUMN_FEE + " INTEGER,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_CONTACT_NUMBER + " TEXT,"
                + COLUMN_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_HALLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALLS);
        onCreate(db);
    }

    public void addHall(EventData event, String imageUrl, String contact, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HALL_NAME, event.getEventName());
        values.put(COLUMN_LOCATION, event.getVenue().getArea());
        values.put(COLUMN_CITY, event.getVenue().getCity());
        values.put(COLUMN_CAPACITY, event.getDetails());
        values.put(COLUMN_FEE, Integer.parseInt(event.getFee()));
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_CONTACT_NUMBER, contact);
        values.put(COLUMN_EMAIL, email);

        db.insert(TABLE_HALLS, null, values);
        db.close();
    }

    public List<EventData> getAllHalls() {
        List<EventData> hallList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_HALLS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EventData event = new EventData();
                Venue venue = new Venue();

                int hallNameIndex = cursor.getColumnIndex(COLUMN_HALL_NAME);
                int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);
                int cityIndex = cursor.getColumnIndex(COLUMN_CITY);
                int capacityIndex = cursor.getColumnIndex(COLUMN_CAPACITY);
                int contactIndex = cursor.getColumnIndex(COLUMN_CONTACT_NUMBER);
                int imageIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);
                int feeIndex = cursor.getColumnIndex(COLUMN_FEE);

                event.setEventName(hallNameIndex != -1 ? cursor.getString(hallNameIndex) : "");
                venue.setArea(locationIndex != -1 ? cursor.getString(locationIndex) : "");
                venue.setCity(cityIndex != -1 ? cursor.getString(cityIndex) : "");
                event.setDetails(capacityIndex != -1 ? "Capacity: " + cursor.getString(capacityIndex) : "");
                event.setCollege(contactIndex != -1 ? cursor.getString(contactIndex) : ""); // Contact Number
                event.setImageUrl(imageIndex != -1 ? cursor.getString(imageIndex) : "");
                event.setFee(feeIndex != -1 ? cursor.getInt(feeIndex) : 0);

                event.setVenue(venue);
                hallList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hallList;
    }
}
