package com.example.ejob.ui.employer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.employer.job.JobViewModel;
import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.example.ejob.ui.user.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.List;

public class EmployerActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    private ChipNavigationBar chipNavigationBar;
    FragmentContainer fragmentContainer;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        display = findViewById(R.id.displayEmailEmployer);
        checkuserstatus();
        chipNavigationBar = findViewById(R.id.menu2);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_employer_main, true);

    }

    public void changeLanguage() {

    }

    void checkuserstatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("logincounter", Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        String email = sharedPreferences.getString("useremail", String.valueOf(MODE_PRIVATE));
        if (counter) {
            display.setText("Chào mừng quay lại, " + email + "!");
        } else {
            startActivity(new Intent(EmployerActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {
            case R.id.nav_employer_main:
                selectedFragment = new EmployerHome();
                break;

            case R.id.nav_employer_case:
                selectedFragment = new JobApplicationEmp();
                break;

            case R.id.nav_applications:
                selectedFragment = new ApplicationsEmp();
                break;

            case R.id.nav_shortlisted:
                selectedFragment = new ShortlistedApps();
                break;

            case R.id.nav_employer_profile:
                selectedFragment = new EmployerProfileFragment();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_employer, selectedFragment)
                .commit();
    }
}