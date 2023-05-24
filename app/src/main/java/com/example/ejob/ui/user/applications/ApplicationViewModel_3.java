package com.example.ejob.ui.user.applications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.user.application.JobApplication;
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

public class ApplicationViewModel_3 extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String jobID;
    ArrayList<JobApplication> applicationList;
    JobApplication application;

    private MutableLiveData<List<JobApplication>> mApplicationLiveData;
    private List<JobApplication> mApplicationList;

    public MutableLiveData<List<JobApplication>> getMyApplicationsLivedata() {
        return mApplicationLiveData;
    }


    public ApplicationViewModel_3(){
        firebaseAuth = FirebaseAuth.getInstance();
        mApplicationLiveData = new MutableLiveData<>();
        initData(firebaseAuth.getCurrentUser().getUid());
    }

    private void initData(String uid) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mApplicationList = new ArrayList<>();
        mApplicationList = getApplicationsFromFirestore(firebaseUser.getUid());
        mApplicationLiveData.setValue(mApplicationList);
    }

    private ArrayList<JobApplication> getApplicationsFromFirestore(String uid) {
        applicationList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;

        firebaseFirestore.collection("Applications")
                .whereEqualTo("applicantID",uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
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
                                application.setJobID(document.get("jobID").toString());

                                application.setJobLocation(document.get("jobLocation").toString());
                                application.setJobType(document.get("jobType").toString());

                                try{
                                    application.setCvitaeLink(document.get("cvitaeLink").toString());
                                }catch (NullPointerException nullPointerException){
                                    Log.d("Tag_NPE", nullPointerException.getMessage());
                                }

                                application.setPosition(document.get("position").toString());

                                applicationList.add(application);
                            }
                        }
                    }
                });

        return applicationList;

    }


}
