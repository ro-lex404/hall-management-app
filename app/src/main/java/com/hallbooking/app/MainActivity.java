package com.hallbooking.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, appTitleInterface {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu optionsMenu;
    private boolean isOwnerMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpHeaderView();

        if (savedInstanceState == null) {
            switchToUserMode();
        }
    }

    private void setUpHeaderView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerView);

        Button signin = headerView.findViewById(R.id.button_signIn);
        signin.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            startActivity(new Intent(getApplicationContext(), LoginActivityNew.class));
        });

        SwitchCompat ownerModeSwitch = headerView.findViewById(R.id.owner_mode_switch);
        ownerModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchToOwnerMode();
            } else {
                switchToUserMode();
            }
            drawer.closeDrawer(GravityCompat.START);
        });
    }

    private void switchToUserMode() {
        isOwnerMode = false;
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setCheckedItem(R.id.nav_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_area, homeFragment.newInstance())
                .commit();
        if (optionsMenu != null) {
            optionsMenu.findItem(R.id.action_search).setVisible(false);
        }
    }

    private void switchToOwnerMode() {
        isOwnerMode = true;
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.owner_drawer_menu);
        navigationView.setCheckedItem(R.id.nav_owner_dashboard);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_area, OwnerDashboardFragment.newInstance())
                .commit();
        if (optionsMenu != null) {
            optionsMenu.findItem(R.id.action_search).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.optionsMenu = menu;
        // Hide search initially
        menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        // User Mode Navigation
        if (!isOwnerMode) {
            if (id == R.id.nav_home) {
                fragment = homeFragment.newInstance();
            } else if (id == R.id.nav_account) {
                fragment = accountsFragment.newInstance();
            } else if (id == R.id.nav_places) {
                fragment = placesFragment.newInstance();
            }
        } else { // Owner Mode Navigation
            if (id == R.id.nav_owner_dashboard) {
                fragment = OwnerDashboardFragment.newInstance();
            } else if (id == R.id.nav_add_hall) {
                startActivity(new Intent(this, AddHallActivity.class));
            }
        }

        // Common navigation for both modes
        if (id == R.id.nav_share) {
            Toast.makeText(this, "Share feature not yet implemented.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_rate_us) {
            startActivity(new Intent(this, RateUsActivity.class));
        } else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(this, ContactUsActivity.class));
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_area, fragment)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSetTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
