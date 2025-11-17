package com.hallbooking.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, appTitleInterface {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFab();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupSidebar();
        updateHeaderView();

        if (savedInstanceState == null) {
            if (getIntent().getBooleanExtra("open_browse_halls", false)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_area, placesFragment.newInstance()).commit();
                navigationView.setCheckedItem(R.id.nav_browse_halls);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_area, homeFragment.newInstance()).commit();
            }
        }
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab_chat);
        if (fab != null) {
            fab.setOnClickListener(view -> {
                startActivity(new Intent(this, ChatActivity.class));
            });
        }
    }

    private void setupSidebar() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void updateHeaderView() {
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) { // Add a null check here
            TextView userEmail = headerView.findViewById(R.id.user_email_text);
            Button signInButton = headerView.findViewById(R.id.button_signIn);

            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (isLoggedIn) {
                userEmail.setText(prefs.getString("user_email", ""));
                userEmail.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.GONE);
            } else {
                userEmail.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                signInButton.setOnClickListener(v -> {
                    startActivity(new Intent(MainActivity.this, LoginActivityNew.class));
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (!prefs.getBoolean("is_logged_in", false)) {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = homeFragment.newInstance();
        } else if (id == R.id.nav_browse_halls) {
            fragment = placesFragment.newInstance();
        } else if (id == R.id.nav_my_halls) {
            if (isUserLoggedIn()) {
                fragment = new MyHallsFragment();
            } else {
                startActivity(new Intent(this, LoginActivityNew.class));
            }
        } else if (id == R.id.nav_my_bookings) {
            if (isUserLoggedIn()) {
                fragment = new MyBookingsFragment();
            } else {
                startActivity(new Intent(this, LoginActivityNew.class));
            }
        } else if (id == R.id.nav_add_hall) {
            if (isUserLoggedIn()) {
                startActivity(new Intent(this, AddHallActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivityNew.class));
            }
        } else if (id == R.id.nav_rate_us) {
            startActivity(new Intent(this, RateUsActivity.class));
        } else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(this, ContactUsActivity.class));
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_area, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getBoolean("is_logged_in", false);
    }

    @Override
    public void onSetTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
