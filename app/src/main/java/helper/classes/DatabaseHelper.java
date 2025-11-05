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
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HALLS = "halls";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HALL_NAME = "hall_name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_CAPACITY = "capacity";

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
                + COLUMN_CAPACITY + " INTEGER" + ")";
        db.execSQL(CREATE_HALLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALLS);
        onCreate(db);
    }

    public void addHall(EventData event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HALL_NAME, event.getEventName());
        values.put(COLUMN_LOCATION, event.getVenue().getArea());
        values.put(COLUMN_CITY, event.getVenue().getCity());
        values.put(COLUMN_CAPACITY, event.getDetails()); // Using details field for capacity

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

                event.setEventName(cursor.getString(cursor.getColumnIndex(COLUMN_HALL_NAME)));
                venue.setArea(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                venue.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_CITY)));
                event.setDetails("Capacity: " + cursor.getString(cursor.getColumnIndex(COLUMN_CAPACITY)));
                event.setVenue(venue);

                // Set default values for other fields to prevent crashes
                event.setCollege("N/A");
                event.setFee(0);

                hallList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hallList;
    }
}
