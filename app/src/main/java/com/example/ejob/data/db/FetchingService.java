package com.example.ejob.data.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ejob.ui.admin.AdminActivity;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;

import java.util.Objects;

public class FetchingService extends Service {

    public static final String INTENT_KEY = "Firestore.Data";

    public static final String USER_INFO_KEY = "UserInfo";
    public static final String EMPLOYER_INFO_KEY = "EmployerInfo";
    public static final String ADMIN_INFO_KEY = "AdminInfo";

    public static final String SEND_USER_UID_KEY = "USER_UID";
    public static final String SEND_EMPLOYER_UID_KEY = "EMP_UID";
    public static final String SEND_ADMIN_UID_KEY = "ADM_UID";

    private static final String JOB_USERSIDE_KEY = "JobsUserSideTotal";
    private static final String JOB_USERHISTORY_KEY = "JobsUserHistory";

    private static final String JOB_EMPSIDE_KEY = "JobsEmpSideTotal";
    private static final String JOB_EMPHISTORY_KEY = "JobsEmpHistory";

    private static final String APPLICATION_HISTORY_KEY_USER = "ApplicationsApplied";
    private static final String APPLICATION_HISTORY_KEY_EMPLOYER = "ApplicationsFromUsers";

    private static final String NEW_REVIEW_CHANNEL = "ApplicationNewReviewChannel";
    private static final String NEW_APPROVAL_CHANNEL = "ApplicationApprovedChannel";

    private static final String NEW_APPLIED_CHANNEL = "ApplicationAppliedChannel";
    private static final String NEW_LIKE_CHANNEL = "LikeChannel";

    private static int accessLevelNumber;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String uid = intent.getStringExtra(SEND_USER_UID_KEY);
        int accessLevel;
        FirebaseFirestore db = FirebaseFirestore.getInstance(Objects.requireNonNull(FirebaseApp.initializeApp(this)));

        final long currentTime = Date.getInstance().getEpochSecond();

        accessLevel = checkUserAccessLevel(uid);

        if (accessLevel == 2) {
            new Thread(() -> {
                db.collection("Users")
                        .document(uid)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + USER_INFO_KEY);
                                }
                            }
                        });

                db.collection("Applications")
                        .document(uid)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + APPLICATION_HISTORY_KEY_USER);
                                }
                            }
                        });

                db.collection("Jobs")
                        .getParent()
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + JOB_USERSIDE_KEY);
                                }
                            }
                        });

            }).start();
        } else if (accessLevel == 3) {
            new Thread(() -> {
                db.collection("Employers")
                        .document(uid)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + EMPLOYER_INFO_KEY);
                                }
                            }
                        });

                db.collection("Jobs")
                        .getParent()
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + JOB_EMPSIDE_KEY);
                                }
                            }
                        });

                db.collection("Jobs/Applications" + uid)
                        .getParent()
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + APPLICATION_HISTORY_KEY_EMPLOYER);
                                }
                            }
                        });

                db.collection("Users")
                        .getParent()
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + USER_INFO_KEY);
                                }
                            }
                        });
            }).start();
        } else {
            new Thread(() -> {
                db.collection("Admin")
                        .document(uid)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + ADMIN_INFO_KEY);
                                }
                            }
                        });
                db.collection("Users")
                        .getParent()
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + USER_INFO_KEY);
                                }
                            }
                        });

                db.collection("Employers")
                        .document(uid)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (data, error) -> {
                            if (error == null) {
                                if (data != null && data.exists()) {
                                    Intent intentData = new Intent(INTENT_KEY + "." + EMPLOYER_INFO_KEY);
                                }
                            }
                        });

            }).start();

        }

        return super.onStartCommand(intent, flags, startId);
    }

    private int checkUserAccessLevel(String uid) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        int accessLevel = 0;
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                // Identify user access level
                if (documentSnapshot.getString("isAdmin") != null) {
                    // User is Employer
                    accessLevelNumber = 1;

                }

                if (documentSnapshot.getString("isEmployer") != null) {
                    // User is Employer
                    accessLevelNumber = 2;

                }

                if (documentSnapshot.getString("isUser") != null) {
                    // User is Employer
                    accessLevelNumber = 3;
                }
            }
        });
        return accessLevelNumber;
    }

}
