package com.example.ejob.ui.employer.applications;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.RecruiteType;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ApplicationViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String jobID;
    ArrayList<JobApplication> applicationList;
    JobApplication application;

    private MutableLiveData<List<JobApplication>> mApplicationLiveData;
    private List<JobApplication> mApplicationList;

    public MutableLiveData<List<JobApplication>> getmListApplicationsLivedata() {
        return mApplicationLiveData;
    }

    public ApplicationViewModel(String jobId) {
        this.jobID = jobId;
        mApplicationLiveData = new MutableLiveData<>();
        initData(jobID);
    }

    private void initData(String jobID) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid().toString();
        mApplicationList = new ArrayList<>();
        mApplicationList = getApplicationsFromFirestore(jobID);
        mApplicationLiveData.setValue(mApplicationList);
    }

    private ArrayList<JobApplication> getApplicationsFromFirestore(String jobId) {
        applicationList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;

        firebaseFirestore.collection("Applications")
                .whereEqualTo("employerFbId", firebaseUser.getUid())
                .whereEqualTo("applicationStatus", "SUBMITTED")
                .whereArrayContains("applicationId", jobId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG_getData", document.getId() + " => " + document.getData());

                                application = new JobApplication();

                                application.setApplicationId(document.get("applicationId").toString());
                                application.setApplicantID(document.get("applicantID").toString());
                                application.setApplicantFullname(document.get("applicantFullname").toString());
                                application.setApplicantPhone(document.get("applicantPhone").toString());
                                application.setApplicantAddress(document.get("applicantAddress").toString());
                                application.setApplicantEmail(document.get("applicantEmail").toString());
                                application.setApplicantUniversity(document.get("applicantUniversity").toString());
                                application.setApplicationDate(document.get("applicationDate").toString());
                                application.setPosition(document.get("position").toString());
                                application.setEmployerFbId(document.get("employerFbId").toString());
                                application.setPhotoURL(document.get("photoURL").toString());
                                application.setApplicantSocialmedia(document.get("applicantSocialmedia").toString());
                                String selfDescript = document.get("selfDescription").toString();
                                application.setSelfDescription(selfDescript);
                                String statusStr = document.get("applicationStatus").toString();
                                application.setApplicationStatus(statusStr);
                                application.setApplicationDate(document.get("applicationDate").toString());
                                application.setCvitaeLink(document.get("cvitaeLink").toString());
                                application.setPosition(document.get("position").toString());

                                applicationList.add(application);
                                Log.d("TAG_999_in", String.valueOf(applicationList.size()));
                            }
                        }
                    }
                });

        Log.d("TAG_999_out", String.valueOf(applicationList.size()));

        return applicationList;

    }

}
