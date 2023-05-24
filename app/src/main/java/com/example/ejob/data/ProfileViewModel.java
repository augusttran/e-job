package com.example.ejob.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicantModel;
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

public class ProfileViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String jobID;
    ApplicantModel applicant;

    private MutableLiveData<ApplicantModel> mApplicationLiveData;
    private ApplicantModel applicantModel;

    public MutableLiveData<ApplicantModel> getApplicantModel() {
        return mApplicationLiveData;

    }

    public ProfileViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        mApplicationLiveData = new MutableLiveData<>();
        initData(firebaseAuth.getCurrentUser().getUid());
    }

    private void initData(String uid) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        applicant = new ApplicantModel();
        applicant = getApplicantModel(firebaseUser.getUid());
        mApplicationLiveData.setValue(applicantModel);
    }

    public ApplicantModel getApplicantModel(String uid) {
        applicantModel = new ApplicantModel();
        String employername;
        DocumentSnapshot snapshot;

        firebaseFirestore.collection("Applicants")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d("TAG_query", document.getData().toString());
                            applicantModel.setApplicantAddress(document.get("applicantAddress").toString());
                            Log.d("TAG_result", applicantModel.getApplicantAddress());
                            applicantModel.setApplicantEmail(document.get("applicantEmail").toString());
                            applicantModel.setApplicantFullname(document.get("applicantFullname").toString());
                            applicantModel.setApplicantID(document.get("applicantID").toString());
                            applicantModel.setApplicantPhone(document.get("applicantPhone").toString());
                            applicantModel.setApplicantSocialmedia(document.get("applicantSocialmedia").toString());
                            applicantModel.setApplicantUniversity(document.get("applicantUniversity").toString());
                        }
                    }
                });

        return applicantModel;

    }

}
