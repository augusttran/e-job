package com.example.ejob.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ejob.ui.employer.ApplicationsEmp;
import com.example.ejob.ui.employer.EmployerHome;
import com.example.ejob.ui.employer.EmployerProfile;
import com.example.ejob.ui.employer.JobApplicationEmp;
import com.example.ejob.ui.employer.ShortlistedApps;
import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.R;
import com.example.ejob.utils.CommonUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdminActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar chipNavigationBar;
    FragmentContainer fragmentContainer;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        chipNavigationBar = findViewById(R.id.menu3);
        chipNavigationBar.setMenuOrientation(ChipNavigationBar.MenuOrientation.HORIZONTAL);
        chipNavigationBar.setOnItemSelectedListener(this);
        chipNavigationBar.setItemSelected(R.id.nav_employer_acc, true);
    }

    @Override
    public void onItemSelected(int i) {
        androidx.fragment.app.Fragment selectedFragment = null;
        switch (i) {

            case R.id.nav_employer_acc:
                selectedFragment = new EmployerAccount();
                break;

            case R.id.nav_usr_acc:
                selectedFragment = new UserAccount();
                break;

            default:
                chipNavigationBar.setItemSelected(R.id.nav_employer_acc, true);
                selectedFragment = new EmployerHome();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_employer, selectedFragment)
                .commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}