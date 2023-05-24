package com.example.ejob.ui.admin.user_accounts;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.employer.job.JobPosting;
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

public class UserViewModel extends ViewModel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db1, db2;
    FirebaseUser firebaseUser;
    UserModel user;

    private MutableLiveData<List<UserModel>> mListEmployerLivedata;
    private List<UserModel> mListEmployer;

    private ArrayList<UserModel> jobPostingArrayList;

    public UserViewModel() {
        mListEmployerLivedata = new MutableLiveData<>();
        initData();
    }

    public MutableLiveData<List<UserModel>> getmListJobLivedata() {
        return mListEmployerLivedata;
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mListEmployer = new ArrayList<>();
        mListEmployer = getUsersFromFirestor();
        mListEmployerLivedata.setValue(mListEmployer);
    }

    private ArrayList<UserModel> getUsersFromFirestor() {
        ArrayList<UserModel> employerArrayList = new ArrayList<>();
        String employername;
        DocumentSnapshot snapshot;

        db1.collection("Users")
                .whereEqualTo("isUser", "1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = new UserModel();
                                String email = document.get("UserEmail").toString();
                                String phone = document.get("PhoneNumber").toString();
                                String fullname = document.get("FullName").toString();
                                String accDateCreatd = document.get("AccountDateCreated").toString();

                                try {
                                    String avail = document.get("isAvailable").toString();
                                    String id = document.get("id").toString();
                                    String address = document.get("Address").toString();
                                    user.setUserStatus(avail);
                                    user.setUserId(id);
                                    user.setUsrAddress(address);

                                } catch (NullPointerException npe) {
                                    npe.getMessage();
                                }
                                user.setUsrEmail(email);
                                user.setUserPhone(phone);
                                user.setUserFullname(fullname);
                                user.setDateCreated(accDateCreatd);

                                try {
                                } catch (NullPointerException npe) {
                                    npe.getMessage();
                                }

                                employerArrayList.add(user);

                            }
                        }
                    }
                });

        return employerArrayList;
    }

    private void getJobListOfEmployer() {
        db2.collection("Jobs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        }
                    }
                });
    }

}
