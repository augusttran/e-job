package com.example.ejob.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.ejob.R;
import com.example.ejob.data.ProfileAdapter;
import com.example.ejob.data.ProfileViewModel;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.pdf.UploadPdf;
import com.example.ejob.utils.Date;
import com.example.ejob.utils.DatePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ApplicantModel applicantModel, applicantModel2;
    ApplicantModel model;
    ImageView userAvatar, upload, change, changePdf;
    boolean cvExist = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private Button pdfBrowse, uploadButton, updateProfile;
    private TextView filepath, cvTitle, cv;
    private EditText email, dob, school, phone, address, fullname, dateCre, etCvurl, etCvname;
    Pair<ApplicantModel, String> pair;
    ProfileViewModel profileViewModel;
    ProfileAdapter profileAdapter;
    Long dateCreated;
    Date date;
    Context context;
    Uri fileUri;
    FirebaseStorage fStorage;
    boolean uploaded = false;
    String cvUploadedUrl;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.v = view;
        this.context = getContext();

        mapping();
        init();
        fetchData();

        dob.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(new DatePickerDialog.OnDatePickedListener() {
                @Override
                public void onDateOk(int date, int month, int year) {
                    ((EditText) v).setError(null);
                    ((EditText) v).setText(Date.getInstance(date, month - 1, year).toString());
                }

                @Override
                public void onDateError(int date, int month, int year) {
                    v.requestFocus();
                    ((EditText) v).setError("Tuổi phải lớn hơn hoặc bằng 18");
                }
            }, (date, month, year) -> {
                Date dateObj = Date.getInstance();
                Date minValidDate = Date.getInstance(date, month, year + 18);
                return minValidDate.getEpochSecond() <= dateObj.getEpochSecond();
            });
            dialog.show(getActivity().getSupportFragmentManager(), null);
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(valFullName() && valAddress() && valEmail() && valAddress()
                        && valDob() && valPhone() & valUni())) {
                    return;
                } else {
                    updateEvent();
                }

            }
        });

        changePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfEvent();
            }
        });

    }

    private ApplicantModel gatherData() {
        ApplicantModel profile = new ApplicantModel();
        profile.setApplicantEmail(email.getText().toString());
        profile.setApplicantAddress(address.getText().toString());
        profile.setApplicantFullname(fullname.getText().toString());
        profile.setApplicantUniversity(school.getText().toString());
        profile.setApplicantPhone(phone.getText().toString());
        profile.setApplicantAddress(address.getText().toString());

        return profile;
    }

    private void init() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        applicantModel = new ApplicantModel();
        date = new Date();
        email.setEnabled(false);
        dateCre.setEnabled(false);
        // swipeRefreshLayout = v.findViewById(R.id.swipeProfile);

    }

    private void mapping() {

        fullname = v.findViewById(R.id.etFullnameUP1);
        email = v.findViewById(R.id.etEmailUP1);
        school = v.findViewById(R.id.etSchoolUP1);
        phone = v.findViewById(R.id.etPersonalPhoneUP1);
        address = v.findViewById(R.id.etPersonalAddressUP1);
        cv = v.findViewById(R.id.pdfLinks);
        dob = v.findViewById(R.id.etDateofBirthUP1);
        dateCre = v.findViewById(R.id.etNgaydangkyUser);
        cvTitle = v.findViewById(R.id.tvFileTitle);
        upload = v.findViewById(R.id.cvUpload);
        filepath = v.findViewById(R.id.pdfLinks);
        etCvurl = v.findViewById(R.id.etDuongDanCV);
        etCvname = v.findViewById(R.id.etTenfileCV);
        updateProfile = v.findViewById(R.id.btnUpdate);
        changePdf = v.findViewById(R.id.changePdf);
    }

    private void updateEvent() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("UserEmail", email.getText().toString());
        hashMap.put("Address", address.getText().toString());
        hashMap.put("DoB", dob.getText().toString());
        hashMap.put("FullName", fullname.getText().toString());
        hashMap.put("PhoneNumber", phone.getText().toString());
        hashMap.put("University", school.getText().toString());
        DatabaseReference profileRef = firebaseDatabase.getReference("Profiles")
                .child(firebaseAuth.getCurrentUser().getUid());
        profileRef.updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ApplicantModel fetchData() {

        DatabaseReference cvRef = firebaseDatabase.getReference("cvUploads");
        DatabaseReference profileRef = firebaseDatabase.getReference("Profiles");

        cvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    cvExist = true;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        etCvname.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("fileName")
                                .getValue().toString());

                        String cvLink = snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("cvURL").getValue()
                                .toString();
                        etCvurl.setText(cvLink);
                    }
                } else {
                    filepath.setText("Click vào icon bên phải phía trên để upload CV");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    for (DataSnapshot children : snapshot.child(firebaseAuth.getCurrentUser().getUid()).getChildren()) {
                        fullname.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("FullName")
                                .getValue().toString());
                        email.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("UserEmail")
                                .getValue().toString());
                        dob.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("DoB").getValue()
                                .toString());
                        phone.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("PhoneNumber")
                                .getValue().toString());
                        school.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("University")
                                .getValue().toString());
                        address.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Address")
                                .getValue().toString());

                        dateCreated = Long.parseLong(snapshot.child(firebaseAuth.getCurrentUser().getUid())
                                .child("AccountDateCreated").getValue().toString());
                        dateCre.setText(date.getInstance(dateCreated).toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return applicantModel2;
    }

    private boolean valFullName() {
        String name = fullname.getText().toString();
        if (name.isEmpty()) {
            fullname.setError("Vui lòng nhập Tên công ty");
            return false;
        } else {
            fullname.setError(null);
            return true;
        }
    }

    private boolean valEmail() {
        String name = email.getText().toString();
        if (name.isEmpty()) {
            email.setError("Vui lòng nhập Email");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean valPhone() {
        String name = phone.getText().toString();
        if (name.isEmpty()) {
            phone.setError("Vui lòng nhập số điện thoại");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    private boolean valUni() {
        String name = school.getText().toString();
        if (name.isEmpty()) {
            school.setError("Vui lòng nhập trường học");
            return false;
        } else {
            school.setError(null);
            return true;
        }
    }

    private boolean valAddress() {
        String name = address.getText().toString();
        if (name.isEmpty()) {
            address.setError("Vui lòng nhập Địa chỉ");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean valDob() {
        String name = dob.getText().toString();
        if (name.isEmpty()) {
            dob.setError("Vui lòng nhập Năm thành lập");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
                        // avatar.setImageURI(result);
                        fileUri = result;
                    }
                }
            });

    private void uploadPdfEvent() throws IllegalStateException, NullPointerException {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Upload CV");
        progressDialog.setMessage("Progress Bar");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        getContent.launch("application/pdf");
        if (fileUri != null) {
            int cvRandom = new Random().nextInt(5000);
            double progress = 0;
            StorageReference folder = fStorage.getInstance().getReference("CVFiles")
                    .child(firebaseAuth.getCurrentUser().getUid());
            String possibleNameFile = fileUri.getLastPathSegment().replaceAll(".*/", "");
            HashMap<String, String> hashMap = new HashMap<>();

            folder.putFile(fileUri)
                    .addOnProgressListener(snapshot -> {
                        double progress1 = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.show();
                        progressDialog.setProgress(((int) progress1));
                        if (progress1 == 100) {
                            progressDialog.setMessage("Uploaded " + progress1 + " %");
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete())
                                ;
                            uploaded = true;
                            Uri uri = uriTask.getResult();
                            hashMap.put("cvURL", uri.toString());
                            cvUploadedUrl = uri.toString();
                            etCvurl.setText(cvUploadedUrl);
                        }
                    })
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (task1.isComplete()) {
                                // upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                                // upCv.setEnabled(false);
                                etCvname.setText("Tên file: " + possibleNameFile + ".pdf");
                                progressDialog.setCancelable(true);
                                progressDialog.dismiss();

                                hashMap.put("cvUri", String.valueOf(task1.getResult().getStorage().getDownloadUrl()));
                                hashMap.put("fileName", possibleNameFile);
                                // jobApplying.setCvitaeLink(String.valueOf(task1.getResult()));

                                firebaseDatabase.getReference("cvUploads")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Upload xong!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {

                            }
                        }
                    });

        }

    }

}