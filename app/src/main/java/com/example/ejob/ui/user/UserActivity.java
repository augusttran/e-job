package com.example.ejob.ui.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.example.ejob.utils.CommonUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class UserActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    Button logout;

    SwipeRefreshLayout swipeRefreshLayout;
    ChipNavigationBar chipNavigationBar;
    TextView display;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.navy_100));

        swipeRefreshLayout = findViewById(R.id.swipeJoblist);
        display = findViewById(R.id.displayEmail);
        chipNavigationBar = findViewById(R.id.menu);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_main, true);
        checkuserstatus();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {
            case R.id.nav_main:
                selectedFragment = new UserHomeFragment();
                break;
            case R.id.nav_applications:
                selectedFragment = new ApplicationFragment();
                break;
            case R.id.nav_profile:
                selectedFragment = new UserProfileFragment();
                break;

            default:
                selectedFragment = new UserHomeFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    void checkuserstatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        String email = sharedPreferences.getString("useremail", String.valueOf(MODE_PRIVATE));
        if (counter) {
            display.setText("Chào mừng quay lại, " + email + "!");
        } else {
            startActivity(new Intent(UserActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}