package helper.classes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.EventData;

// Stormpath SDK is deprecated. All related code has been removed.

public class Global extends Application {
    private static final String PREFERENCE_NAME = "Application_V_Group";
    private static final String TAG = "Global";
    private static Context context;
    private static List<EventData> recent = null;
    private static Set<EventData> item = null;

    // Using a static IP is not recommended for production apps. Consider a configurable approach.
    public static final String ipAddr = "gentle-mesa-83442.herokuapp.com";
    public static final String GET_EVENTS_DATA = "http://" + ipAddr + "/events/getData";
    public static final String GET_PLACES_DATA = "http://" + ipAddr + "/events/getPlaces";
    public static final String GET_COLLEGES_DATA = "http://" + ipAddr + "/events/getColleges";
    public static final String baseUrl = "http://" + ipAddr + "";
    public static final String ACTION_DATA_RECEIVED = "dataReceived";

    // Navigation View to set itemSelected
    static NavigationView navigationView;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // Stormpath SDK is deprecated and has been removed.
        // A new authentication provider needs to be implemented.
        Log.i(TAG, "Application context initialized.");
    }

    public static Context getContext() {
        return context;
    }

    public static void setRecent(EventData recently_used) {
        if (recent == null) {
            recent = new ArrayList<>();
            item = new HashSet<>();
        }
        //check for redundant entries
        if (item.add(recently_used)) {
            recent.add(recently_used);
        }
    }

    public static List<EventData> getRecent() {
        return recent;
    }

    public static void setNavigationView(NavigationView view) {
        navigationView = view;
    }

    public static NavigationView getNavigationView() {
        return navigationView;
    }

    public static void saveEventDataList(Context context, List<EventData> eventDataList) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String objectToJson = gson.toJson(eventDataList);
        editor.putString("eventDataList", objectToJson);
        editor.apply(); // Use apply() for asynchronous saving
    }

    public static List<EventData> loadEventDataList(Context context) {
        if (context == null) return null;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonToObject = preferences.getString("eventDataList", "");

        if (jsonToObject == null || jsonToObject.isEmpty()) {
            return null;
        } else {
            try {
                return gson.fromJson(jsonToObject, new TypeToken<List<EventData>>() {}.getType());
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "Error parsing event data list from SharedPreferences", e);
                return null;
            }
        }
    }

    public static void setUserName(String uName) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", uName);
        editor.apply();
    }

    public static String getUserName() {
        if (context == null) return "";
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString("userName", "");
    }

    public static void removeUserName() {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("userName");
        editor.apply();
    }
}
