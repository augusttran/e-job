package com.example.ejob.ui.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.ejob.MainActivity;
import com.example.ejob.R;
import com.example.ejob.data.db.FetchingService;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashscreenActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private Intent intent;
    private int accessNumber;

    private enum TaskCompleteState {
        JobsCompleted, UsersCompleted, UserProfileCompleted, EmployerProfileCompleted, UsersUncompleted;
    }

    private TaskCompleteState state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        state = TaskCompleteState.UsersUncompleted;

        intent = new Intent(this, MainActivity.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // SplashscreenActivity.this.intent.putExtra(FetchingService.)
                if (intent.getAction().equals(FetchingService.INTENT_KEY + "." +
                        FetchingService.USER_INFO_KEY)) {
                    ApplicantModel userInfo = intent.getExtras().getParcelable(FetchingService.USER_INFO_KEY);
                    SplashscreenActivity.this.intent.putExtra(FetchingService.USER_INFO_KEY, userInfo);
                } else if (intent.getAction()
                        .equals(FetchingService.INTENT_KEY + "." + FetchingService.EMPLOYER_INFO_KEY)) {
                    Employer employerInfo = intent.getExtras().getParcelable(FetchingService.EMPLOYER_INFO_KEY);
                    SplashscreenActivity.this.intent.putExtra(FetchingService.EMPLOYER_INFO_KEY, employerInfo);
                }
            }
        };

        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startService(new Intent(this, FetchingService.class)
                    .putExtra(FetchingService.SEND_USER_UID_KEY, user.getUid()));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
