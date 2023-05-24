package com.example.ejob.ui.employer.applications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicationStatus;
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

public class MyJobsViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    private MutableLiveData<List<com.example.ejob.ui.user.userjob.JobPostingforUser>> mListJobLivedata;
    private List<com.example.ejob.ui.user.userjob.JobPostingforUser> mListJob;

    public MutableLiveData<List<com.example.ejob.ui.user.userjob.JobPostingforUser>> getmListJobLivedata() {
        return mListJobLivedata;
    }

    public MyJobsViewModel() {
        mListJobLivedata = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid().toString();
        mListJob = new ArrayList<>();
        mListJob = getJobFromFirestore();
        mListJobLivedata.setValue(mListJob);
    }

    private ArrayList<com.example.ejob.ui.user.userjob.JobPostingforUser> getJobFromFirestore() {
        ArrayList<JobPostingforUser> jobPostingArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;

        firebaseFirestore.collection("JobsEmp")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myjobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JobPostingforUser jobPosting = new JobPostingforUser();
                                try {
                                    jobPosting.setJobId(document.getReference().getPath());
                                    jobPosting.setEmployerName(document.get("jobEmployer").toString());
                                    jobPosting.setJobId(document.get("jobId").toString());
                                    jobPosting.setJobStatus(document.get("isAvailable").toString());
                                    jobPosting.setNumberneed(document.get("numberNeed").toString());
                                    jobPosting.setJobDescription(document.get("jobDescription").toString());
                                    jobPosting.setJobTitle(document.get("jobTitle").toString());
                                    jobPosting.setCvRequired(document.get("cvRequired").toString());
                                    jobPosting.setJobLocation(document.get("jobLocation").toString());
                                    jobPosting.setEmpEmail(document.get("employerEmail").toString());
                                    jobPosting.setNumberneed(document.get("numberNeed").toString());
                                    jobPosting.setJobType(document.get("jobType").toString());
                                    jobPosting.setSalary(document.get("jobSalary").toString());
                                } catch (NullPointerException npe) {
                                    npe.printStackTrace();
                                }

                                try {
                                    jobPosting.setJobDateCreated(document.get("jobDateCreated").toString());
                                    jobPosting.setJobDeadline(document.get("jobOod").toString());
                                } catch (NullPointerException npe) {
                                    npe.getMessage();
                                }
                                jobPostingArrayList.add(jobPosting);
                            }
                        }
                    }
                });
        return jobPostingArrayList;
    }

}
