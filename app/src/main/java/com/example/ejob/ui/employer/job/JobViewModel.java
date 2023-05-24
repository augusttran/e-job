package com.example.ejob.ui.employer.job;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    private MutableLiveData<List<JobPosting>> mListJobLivedata;
    private List<JobPosting> mListJob;

    public MutableLiveData<List<JobPosting>> getmListJobLivedata() {
        return mListJobLivedata;
    }

    public JobViewModel() {
        mListJobLivedata = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid().toString();
        mListJob = new ArrayList<>();
        mListJob = getJobFromFirestore(uid);
        mListJobLivedata.setValue(mListJob);

    }

    private ArrayList<JobPosting> getJobFromFirestore(String uid) {
        ArrayList<JobPosting> jobPostingArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);

        firebaseFirestore.collection("Jobs")
                .whereEqualTo("isAvailable", "1")
                .orderBy("jobTitle")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                JobPosting jobPosting = new JobPosting();
                                jobPosting.setEmployerName(document.get("jobEmployer").toString());
                                jobPosting.setJobDescription(document.get("jobDescription").toString());
                                jobPosting.setJobTitle(document.get("jobTitle").toString());
                                jobPosting.setJobLocation(document.get("jobLocation").toString());
                                jobPosting.setSalary(document.get("jobSalary").toString());
                                jobPosting.setJobDateCreated(document.get("jobDateCreated").toString());

                                try {
                                    jobPosting.setJobDeadline(document.get("jobOod").toString());
                                } catch (NullPointerException npe) {
                                    Log.d("NPE_OOD", npe.getMessage());
                                }
                                jobPostingArrayList.add(jobPosting);
                                Log.d("TAG", document.getId() + " => " + jobPosting.getJobLocation());
                                Log.d("TAG", document.getId() + " => " + jobPosting.getJobDeadline());

                            }
                        }
                    }
                });

        return jobPostingArrayList;
    }
}
