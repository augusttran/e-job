package com.example.ejob.ui.user.applications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    private MutableLiveData<List<JobPostingforUser>> mListJobLivedata;
    private List<JobPostingforUser> mListJob;

    public MutableLiveData<List<JobPostingforUser>> getmListJobLivedata() {
        return mListJobLivedata;
    }

    public MyJobsViewModel(){
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


    private ArrayList<JobPostingforUser> getJobFromFirestore(){
        ArrayList<JobPostingforUser> jobPostingArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;
//        DocumentReference df = firebaseFirestore.collection("Users").document(uid);

        firebaseFirestore.collection("JobsEmp")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("myjobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                JobPostingforUser jobPosting = new JobPostingforUser();
                                jobPosting.setJobId(document.getReference().getPath());
                                Log.d("TAG", document.getReference().getPath());
                                jobPosting.setEmployerName(document.get("jobEmployer").toString());
                                jobPosting.setJobId(document.get("jobId").toString());
                                jobPosting.setJobStatus(document.get("isAvailable").toString());
                                jobPosting.setNumberneed(document.get("numberNeed").toString());
//                                jobPosting.setEmployerFbID(document.get("empId").toString());
                                jobPosting.setJobDescription(document.get("jobDescription").toString());
                                jobPosting.setJobTitle(document.get("jobTitle").toString());
                                jobPosting.setJobLocation(document.get("jobLocation").toString());
                                jobPosting.setEmpEmail(document.get("employerEmail").toString());
                                Log.d("TAG", document.get("employerEmail").toString());
                                jobPosting.setNumberneed(document.get("numberNeed").toString());
                                Log.d("TAG", document.get("numberNeed").toString());
                                jobPosting.setJobType(document.get("jobType").toString());
                                jobPosting.setSalary(document.get("jobSalary").toString());
                                try{
                                    jobPosting.setJobDeadline(document.get("jobOod").toString());
                                }catch (NullPointerException npe){
                                    npe.getMessage();
                                }
                                jobPosting.setJobDateCreated(document.get("jobDateCreated").toString());
                                jobPostingArrayList.add(jobPosting);
                                Log.d("TAG", document.getId() + " => " + jobPosting.getJobLocation());
                                Log.d("TAG", document.getId() + " => " + jobPosting.getJobDeadline());

                            }
                        }
                    }
                });

        Log.d("TAG_sizeMyJob", String.valueOf(jobPostingArrayList.size()));
        return jobPostingArrayList;
    }


}
