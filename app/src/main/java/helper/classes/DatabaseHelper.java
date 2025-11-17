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
    // Version updated for major schema change
    private static final int DATABASE_VERSION = 14;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_HALLS = "halls";
    private static final String TABLE_RATINGS = "ratings";
    private static final String TABLE_BOOKINGS = "bookings";

    // Common Columns
    private static final String COLUMN_ID = "_id";

    // Users Table Columns
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_CONTACT = "contact";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";

    // Halls Table Columns
    private static final String HALL_COLUMN_OWNER_ID = "owner_id";
    private static final String HALL_COLUMN_NAME = "hall_name";
    private static final String HALL_COLUMN_LOCATION = "location";
    private static final String HALL_COLUMN_CITY = "city";
    private static final String HALL_COLUMN_CAPACITY = "capacity";
    private static final String HALL_COLUMN_FEE = "fee";
    private static final String HALL_COLUMN_IMAGE_URL = "image_url";
    private static final String HALL_COLUMN_CONTACT = "contact_number";
    private static final String HALL_COLUMN_EMAIL = "email";
    private static final String HALL_COLUMN_AVG_RATING = "average_rating";
    private static final String HALL_COLUMN_RATING_COUNT = "rating_count";

    // Ratings Table Columns
    private static final String RATING_COLUMN_HALL_ID = "hall_id";
    private static final String RATING_COLUMN_STARS = "stars";

    // Bookings Table Columns
    private static final String BOOKING_COLUMN_USER_ID = "user_id";
    private static final String BOOKING_COLUMN_HALL_ID = "hall_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_COLUMN_NAME + " TEXT,"
                + USER_COLUMN_CONTACT + " TEXT,"
                + USER_COLUMN_EMAIL + " TEXT UNIQUE,"
                + USER_COLUMN_PASSWORD + " TEXT" + ")";

        String CREATE_HALLS_TABLE = "CREATE TABLE " + TABLE_HALLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HALL_COLUMN_OWNER_ID + " INTEGER,"
                + HALL_COLUMN_NAME + " TEXT,"
                + HALL_COLUMN_LOCATION + " TEXT,"
                + HALL_COLUMN_CITY + " TEXT,"
                + HALL_COLUMN_CAPACITY + " INTEGER,"
                + HALL_COLUMN_FEE + " INTEGER,"
                + HALL_COLUMN_IMAGE_URL + " TEXT,"
                + HALL_COLUMN_CONTACT + " TEXT,"
                + HALL_COLUMN_EMAIL + " TEXT,"
                + HALL_COLUMN_AVG_RATING + " REAL DEFAULT 0,"
                + HALL_COLUMN_RATING_COUNT + " INTEGER DEFAULT 0" + ")";

        String CREATE_RATINGS_TABLE = "CREATE TABLE " + TABLE_RATINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RATING_COLUMN_HALL_ID + " INTEGER,"
                + RATING_COLUMN_STARS + " REAL" + ")";

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BOOKING_COLUMN_USER_ID + " INTEGER,"
                + BOOKING_COLUMN_HALL_ID + " INTEGER" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_HALLS_TABLE);
        db.execSQL(CREATE_RATINGS_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HALLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // --- User Methods ---
    public boolean addUser(String name, String contact, String email, String password) {
        if (checkUser(email)) return false; // User already exists
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_NAME, name);
        values.put(USER_COLUMN_CONTACT, contact);
        values.put(USER_COLUMN_EMAIL, email);
        values.put(USER_COLUMN_PASSWORD, password); // In a real app, HASH THE PASSWORD!
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, USER_COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public Cursor checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, USER_COLUMN_EMAIL + "=? AND " + USER_COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null);
    }

    // --- Hall Methods ---
    public long addHall(long ownerId, EventData event, String imageUrl, String contact, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HALL_COLUMN_OWNER_ID, ownerId);
        values.put(HALL_COLUMN_NAME, event.getEventName());
        values.put(HALL_COLUMN_LOCATION, event.getVenue().getArea());
        values.put(HALL_COLUMN_CITY, event.getVenue().getCity());
        values.put(HALL_COLUMN_CAPACITY, event.getDetails());
        values.put(HALL_COLUMN_FEE, Integer.parseInt(event.getFee()));
        values.put(HALL_COLUMN_IMAGE_URL, imageUrl);
        values.put(HALL_COLUMN_CONTACT, contact);
        values.put(HALL_COLUMN_EMAIL, email);
        long id = db.insert(TABLE_HALLS, null, values);
        db.close();
        return id;
    }
    
    public List<EventData> getHallsByOwner(long ownerId) {
        List<EventData> hallList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HALLS, null, HALL_COLUMN_OWNER_ID + "=?", new String[]{String.valueOf(ownerId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                EventData event = new EventData();
                Venue venue = new Venue();

                event.set_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))));
                event.setEventName(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_NAME)));
                venue.setArea(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_LOCATION)));
                venue.setCity(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CITY)));
                event.setDetails(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CAPACITY)));
                event.setCollege(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CONTACT)));
                event.setImageUrl(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_IMAGE_URL)));
                event.setFee(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_FEE)));
                event.setOwnerEmail(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_EMAIL)));
                event.setAverageRating(cursor.getFloat(cursor.getColumnIndex(HALL_COLUMN_AVG_RATING)));
                event.setRatingCount(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_RATING_COUNT)));

                event.setVenue(venue);
                hallList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hallList;
    }

    // --- Booking Methods ---
    public void addBooking(long userId, long hallId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKING_COLUMN_USER_ID, userId);
        values.put(BOOKING_COLUMN_HALL_ID, hallId);
        db.insert(TABLE_BOOKINGS, null, values);
        db.close();
    }

    public List<EventData> getBookingsByUser(long userId) {
        List<EventData> hallList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT T2.* FROM " + TABLE_BOOKINGS + " T1 JOIN " + TABLE_HALLS + " T2 ON T1."
                + BOOKING_COLUMN_HALL_ID + " = T2." + COLUMN_ID + " WHERE T1." + BOOKING_COLUMN_USER_ID + " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                EventData event = new EventData();
                Venue venue = new Venue();

                event.set_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))));
                event.setEventName(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_NAME)));
                venue.setArea(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_LOCATION)));
                venue.setCity(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CITY)));
                event.setDetails(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CAPACITY)));
                event.setCollege(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CONTACT)));
                event.setImageUrl(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_IMAGE_URL)));
                event.setFee(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_FEE)));
                event.setOwnerEmail(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_EMAIL)));
                event.setAverageRating(cursor.getFloat(cursor.getColumnIndex(HALL_COLUMN_AVG_RATING)));
                event.setRatingCount(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_RATING_COUNT)));

                event.setVenue(venue);
                hallList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hallList;
    }

    // --- Rating Methods ---
    public void addRating(long hallId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RATING_COLUMN_HALL_ID, hallId);
        values.put(RATING_COLUMN_STARS, rating);
        db.insert(TABLE_RATINGS, null, values);

        // Update the average rating in the halls table
        Cursor cursor = db.rawQuery("SELECT AVG(" + RATING_COLUMN_STARS + "), COUNT(" + RATING_COLUMN_STARS + ") FROM " + TABLE_RATINGS + " WHERE " + RATING_COLUMN_HALL_ID + " = " + hallId, null);
        if (cursor.moveToFirst()) {
            float avgRating = cursor.getFloat(0);
            int ratingCount = cursor.getInt(1);
            ContentValues updateValues = new ContentValues();
            updateValues.put(HALL_COLUMN_AVG_RATING, avgRating);
            updateValues.put(HALL_COLUMN_RATING_COUNT, ratingCount);
            db.update(TABLE_HALLS, updateValues, COLUMN_ID + " = ?", new String[]{String.valueOf(hallId)});
        }
        cursor.close();
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

                event.set_id(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))));
                event.setEventName(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_NAME)));
                venue.setArea(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_LOCATION)));
                venue.setCity(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CITY)));
                event.setDetails(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CAPACITY)));
                event.setCollege(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_CONTACT)));
                event.setImageUrl(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_IMAGE_URL)));
                event.setFee(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_FEE)));
                event.setOwnerEmail(cursor.getString(cursor.getColumnIndex(HALL_COLUMN_EMAIL)));
                event.setAverageRating(cursor.getFloat(cursor.getColumnIndex(HALL_COLUMN_AVG_RATING)));
                event.setRatingCount(cursor.getInt(cursor.getColumnIndex(HALL_COLUMN_RATING_COUNT)));

                event.setVenue(venue);
                hallList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hallList;
    }
}
