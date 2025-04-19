package com.example.kukuafya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check login status first
        if (!checkLoginStatus()) {
            return; // Redirects to sign-in if not logged in
        }

        setContentView(R.layout.activity_main);

        initializeViews();
        setupNavigationDrawer();
        setupBottomNavigation();
        setupFloatingActionButton();

        // Load home fragment by default
        fragmentManager = getSupportFragmentManager();
        openFragment(new homeFragment());
    }

    private void initializeViews() {
        fab = findViewById(R.id.fab);
        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_open_drawer, R.string.navigation_close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.Home) {
                openFragment(new homeFragment());
                return true;
            } else if (itemId == R.id.Detect) {
                openFragment(new detectFragment());
                return true;
            } else if (itemId == R.id.Reminder) {
                openFragment(new reminderFragment());
                return true;
            } else if (itemId == R.id.Notes) {
                openFragment(new notesFragment());
                return true;
            }
            return false;
        });
    }

    private void setupFloatingActionButton() {
        fab.setOnClickListener(view -> openFragment(new chatFragment()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoginStatus();
    }

    private boolean checkLoginStatus() {
        SharedPreferences loginPrefs = getSharedPreferences("logininfo", MODE_PRIVATE);
        if (!"logged in".equals(loginPrefs.getString("status", ""))) {
            redirectToSignIn();
            return false;
        }
        return true;
    }

    private void redirectToSignIn() {
        startActivity(new Intent(this, sign_in.class));
        Toast.makeText(this, "Please sign in", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

       if (itemId == R.id.About) {
            openFragment(new AboutFragment());
        } else if (itemId == R.id.logout) {  // Changed to match your XML @+id/logout
            handleLogout();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogout() {
        getSharedPreferences("logininfo", MODE_PRIVATE)
                .edit()
                .remove("status")
                .apply();

        redirectToSignIn();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}