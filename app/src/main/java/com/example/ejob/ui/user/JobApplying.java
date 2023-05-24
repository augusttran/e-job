package com.example.ejob.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.pdf.UploadPdf;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class JobApplying extends AppCompatActivity {
    String date;
    Bundle bundle;
    JobPostingforUser jobPosting;
    FirebaseFirestore db1, db2, db3, db4, db5;
    FirebaseDatabase fbDb;
    DatabaseReference cvRef;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    StorageReference storageReference;
    FirebaseStorage fStorage;
    Boolean cvExist;
    boolean uploaded, agreeAttach = false;
    String timeCreated;
    String jobId, employerId, jobStatus;
    JobApplication jobApplying;
    ApplicantModel applyModel;
    UploadPdf uploadPdf;
    Uri fileUri;
    String cvUploadedUrl, cvExistedUrl;
    JobApplication jobApplication;
    String uid;
    private long lastBackPressTime = 0;
    private Toast toast;

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

    private TextView employerName, positionHiring, jobtype, linkCv, tvCvAttach, cvRequired;
    private EditText getEtFullname, getEtPhone, getEtAddress, getEtEmail, getEtSchool, getEtDescription,
            getEtSocialMedia;
    private RelativeLayout submit;
    private ImageView upCv, attachCv;
    private Context jobApplyingContext;
    private TextWatcher jobApplyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            boolean allVal = valFullName() && valAddress() && valPhone() && valSchool() && valSelfDescription()
                    && valSocialmedia();
            if (allVal) {
                submit.setBackground(getDrawable(R.drawable.button_green));
            }
            submit.setEnabled(allVal);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private UploadTask urlTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_apply);

        mapping();
        initFb();

        jobApplyingContext = this;

        jobPosting = getIntent().getExtras().getParcelable("myJobposting1");

        jobId = jobPosting.getJobId();
        employerId = jobPosting.getEmployerFbID();
        jobStatus = jobPosting.getJobStatus();
        positionHiring.setText("Đơn ứng tuyển cho vị trí " + jobPosting.getJobTitle());
        employerName.setText("Gửi đến " + jobPosting.getEmployerName());
        jobtype.setText(jobPosting.getJobType());
        getEtEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        cvRequired.setText(jobPosting.getCvRequired());
        getEtEmail.setEnabled(false);
        timeCreated = String.valueOf(date);

        upCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfEvent();

            }
        });

        attachCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachEvent();
            }
        });

        getEtFullname.addTextChangedListener(jobApplyTextWatcher);
        getEtSchool.addTextChangedListener(jobApplyTextWatcher);
        getEtAddress.addTextChangedListener(jobApplyTextWatcher);
        getEtPhone.addTextChangedListener(jobApplyTextWatcher);
        getEtDescription.addTextChangedListener(jobApplyTextWatcher);
        getEtSocialMedia.addTextChangedListener(jobApplyTextWatcher);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(valFullName() && valAddress() && valPhone() && valSchool() && valSelfDescription())) {
                    Toast.makeText(JobApplying.this, "Không thể để trống các trường thông tin này", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (firebaseAuth.getCurrentUser().getUid() == null) {
                        return;
                    } else {
                        gatherData();

                        AlertDialog.Builder builder = new AlertDialog.Builder(jobApplyingContext);
                        builder.setTitle("Job Applying alert");
                        builder.setMessage(
                                "Bạn không thể sửa đơn ứng tuyển sau khi nộp. \nBạn chắc rằng mình muốn tiếp tục?");
                        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        submitEvent(gatherData());
                                        Toast.makeText(JobApplying.this, "Ứng tuyển thành công!", Toast.LENGTH_LONG)
                                                .show();
                                        // startActivity(new Intent(jobApplyingContext, ViewJobDetail.class));
                                        finish();

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        builder.setPositiveButton("Yes", dialogListener);
                        builder.setNegativeButton("No", dialogListener);
                        AlertDialog alert = builder.create();
                        alert.show();

                    }

                }
            }
        });
    }

    private void attachEvent() throws NullPointerException {
        if (!(uploaded == true || cvExist == true)) {
            Toast.makeText(jobApplyingContext, "Bạn cần upload CV trước", Toast.LENGTH_SHORT).show();
        } else {
            agreeAttach = true;
            attachCv.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_ok_24));
            attachCv.setEnabled(false);
            tvCvAttach.setText("CV đã được đính kèm");
        }
    }

    private Pair<JobApplication, ApplicantModel> gatherData() {
        jobApplying = new JobApplication();
        applyModel = new ApplicantModel();

        jobApplying.setApplicantID(firebaseAuth.getCurrentUser().getUid()); //
        jobApplying.setEmployerFbId(jobPosting.getEmployerFbID());
        jobApplying
                .setApplicationId(jobPosting.getJobId().replaceAll(".*/", "") + firebaseAuth.getCurrentUser().getUid());
        jobApplying.setPosition(positionHiring.getText().toString());
        jobApplying.setApplicationDate(String.valueOf(timeCreated));
        jobApplying.setApplicationStatus("SUBMITTED");
        jobApplying.setJobID(jobPosting.getJobId().replaceAll(".*/", ""));
        jobApplying.setJobType(jobPosting.getJobType());

        if (cvUploadedUrl == null && cvExistedUrl == null) {
            jobApplying.setCvitaeLink("none");
            applyModel.setCvURl("none");
        } else if (!cvExistedUrl.isEmpty()) {
            if (agreeAttach == true) {
                jobApplying.setCvitaeLink(tvCvAttach.getText().toString());
                applyModel.setCvURl(cvCheck(firebaseAuth.getCurrentUser().getUid()));
            }
        } else if (!cvUploadedUrl.isEmpty()) {
            if (agreeAttach == true) {
                jobApplying.setCvitaeLink(cvUploadedUrl);
                applyModel.setCvURl(cvUploadedUrl);
            }
        }

        jobApplying.setApplicantAddress(getEtAddress.getText().toString());
        jobApplying.setSelfDescription(getEtDescription.getText().toString());
        jobApplying.setApplicantEmail(getEtEmail.getText().toString());
        jobApplying.setApplicantPhone(getEtPhone.getText().toString());
        jobApplying.setApplicantSocialmedia(getEtSocialMedia.getText().toString());
        jobApplying.setApplicantFullname(getEtFullname.getText().toString());
        jobApplying.setApplicantUniversity(getEtSchool.getText().toString());
        jobApplying.setPhotoURL("imgUrl");
        jobApplying.setJobLocation(jobPosting.getJobLocation());
        jobApplying.setEmployerFullname(jobPosting.getEmployerName());

        // set Applicant model
        applyModel.setApplicantID(firebaseAuth.getCurrentUser().getUid());
        applyModel.setApplicantFullname(getEtFullname.getText().toString());
        applyModel.setApplicantEmail(getEtEmail.getText().toString());
        applyModel.setApplicantPhone(getEtPhone.getText().toString());
        applyModel.setApplicantSocialmedia(getEtSocialMedia.getText().toString());
        applyModel.setApplicantUniversity(getEtSchool.getText().toString());
        applyModel.setApplicantAddress(getEtAddress.getText().toString());

        return new Pair<JobApplication, ApplicantModel>(jobApplying, applyModel);
    }

    private void initFb() {
        firebaseAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        fStorage = FirebaseStorage.getInstance();
        db1 = FirebaseFirestore.getInstance();

        Date dateInsc = Date.getInstance();
        date = dateInsc.toString();
        submit.setEnabled(false);
        submit.setBackground(getDrawable(R.drawable.button_grayout));
        cvRef = FirebaseDatabase.getInstance().getReference("cvUploads");
        cvCheck(firebaseAuth.getCurrentUser().getUid());
    }

    private boolean valFullName() {
        String name = getEtFullname.getText().toString();
        if (name.isEmpty()) {
            getEtFullname.setError("Vui lòng nhập Họ và Tên");
            return false;
        } else {
            getEtFullname.setError(null);
            return true;
        }
    }

    private boolean valPhone() {
        String name = getEtPhone.getText().toString();
        if (name.isEmpty()) {
            getEtPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        } else {
            getEtPhone.setError(null);
            return true;
        }
    }

    private boolean valAddress() {
        String name = getEtAddress.getText().toString();
        if (name.isEmpty()) {
            getEtAddress.setError("Vui lòng nhập Địa chỉ");
            return false;
        } else {
            getEtAddress.setError(null);
            return true;
        }
    }

    private boolean valSocialmedia() {
        String name = getEtSocialMedia.getText().toString();
        if (name.isEmpty()) {
            getEtSocialMedia.setError("Vui lòng nhập tài khoản Mạng xã hội");
            return false;
        } else {
            getEtSocialMedia.setError(null);
            return true;
        }
    }

    private boolean valSelfDescription() {
        String name = getEtDescription.getText().toString();
        if (name.isEmpty()) {
            getEtDescription.setError("Vui lòng nhập Mô tả bản thân");
            return false;
        } else {
            getEtDescription.setError(null);
            return true;
        }
    }

    private boolean valSchool() {
        String name = getEtSchool.getText().toString();
        if (name.isEmpty()) {
            getEtSchool.setError("Vui lòng nhập Trường học");
            return false;
        } else {
            getEtSchool.setError(null);
            return true;
        }
    }

    private void submitEvent(Pair<JobApplication, ApplicantModel> paramPair) {

        jobApplying = paramPair.first;
        applyModel = paramPair.second;

        db1.collection("Applications")
                // .document(jobId.replaceAll(".*/", ""))
                // .collection("applied")
                .document(jobPosting.getJobId().replaceAll(".*/", "") + firebaseAuth.getCurrentUser().getUid())
                .set(jobApplying)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(JobApplying.this,
                                "Ứng tuyển thành công cho vị trí " + jobPosting.getJobTitle() + "!", Toast.LENGTH_SHORT)
                                .show();
                        // startActivity(new Intent(jobApplyingContext, ViewJobDetail.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JobApplying.this, "Ứng tuyển thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        db1.collection("Applications1")
                .document(jobApplying.getApplicantID())
                .collection("applied")
                .document()
                .set(jobApplying)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        fbDb.getReference("userapplications")
                .child(jobPosting.getJobId().replaceAll(".*/", ""))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(jobApplying);

        fbDb.getReference("employerapplications")
                .child(jobPosting.getEmployerFbID())
                .child(jobPosting.getJobId().replaceAll(".*/", ""))
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(jobApplying);

        db1.collection("Applicants")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(applyModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Applicants", "Succeeded add job to Applicants");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Applicants", "Failed add job to Applicants");

                    }
                });

        db1.collection("JobsPending")
                .document(jobPosting.getEmployerFbID())
                .collection("pending")
                .document()
                .set(jobApplying)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("AppliedHistory", "Success add job to JobAppliedHistory");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AppliedHistory", "Failure add job to JobAppliedHistory");

                    }
                });

    }

    @Override
    public void onBackPressed() {

        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            Toast.makeText(this, "Nhấn Back thêm lần nữa để thoát", Toast.LENGTH_LONG).show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    public void mapping() {
        positionHiring = findViewById(R.id.titleJob);
        employerName = findViewById(R.id.nameEmp);
        jobtype = findViewById(R.id.typeJob);
        submit = findViewById(R.id.btnApply);
        upCv = findViewById(R.id.cvAttach);
        attachCv = findViewById(R.id.cvAttach2);
        cvRequired = findViewById(R.id.cvRequirement);

        tvCvAttach = findViewById(R.id.tvAttachCV);
        linkCv = findViewById(R.id.pdfLinks);
        getEtFullname = findViewById(R.id.etFullname);
        getEtSchool = findViewById(R.id.etInsitution);
        getEtPhone = findViewById(R.id.etPersonalPhone);
        getEtEmail = findViewById(R.id.etFacebook);
        getEtAddress = findViewById(R.id.etPersonalAddress);
        getEtDescription = findViewById(R.id.etSelfDescription);
        getEtSocialMedia = findViewById(R.id.etSocialMedia);
    }

    private void uploadImageEvent() {
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setMessage("Uploading........");
        getContent.launch("image/*");

        if (fileUri != null) {
            int imgRandomLink = new Random().nextInt(5000);
            // imageUri = String.valueOf(imgRandomLink);
            StorageReference reference = fStorage.getInstance().getReference().child("images/" + imgRandomLink);
            reference.putFile(fileUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                task.getResult().getMetadata().getReference().getDownloadUrl();
                                Toast.makeText(JobApplying.this, task.getResult().toString(), Toast.LENGTH_SHORT)
                                        .show();

                            } else {
                                progressBar.dismiss();
                                Toast.makeText(JobApplying.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }

                        }
                    });
        }
    }

    private String cvCheck(String UID) {
        cvRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(UID).exists()) {
                    cvExist = true;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        upCv.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_ok_24));
                        upCv.setEnabled(false);
                        // linkCv.setText(child.child("fileName").getValue().toString());
                        linkCv.setText(snapshot.child(UID).child("fileName").getValue().toString());

                        cvExistedUrl = snapshot.child(UID).child("cvURL").getValue().toString();
                        tvCvAttach.setText(cvExistedUrl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String cvurl = tvCvAttach.getText().toString();
        return cvurl;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void uploadPdfEvent() throws IllegalStateException, NullPointerException {
        ProgressDialog progressDialog = new ProgressDialog(jobApplyingContext);
        progressDialog.setTitle("Upload CV");
        progressDialog.setMessage("Progress Bar");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        getContent.launch("pdf");
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
                        }
                    })
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (task1.isComplete()) {
                                upCv.setImageResource(R.drawable.ic_baseline_check_ok_24);
                                upCv.setEnabled(false);
                                linkCv.setText("Tên file: " + possibleNameFile + ".pdf");
                                progressDialog.setCancelable(true);
                                progressDialog.dismiss();

                                hashMap.put("cvUri", String.valueOf(task1.getResult().getStorage().getDownloadUrl()));
                                hashMap.put("fileName", possibleNameFile);
                                // jobApplying.setCvitaeLink(String.valueOf(task1.getResult()));

                                fbDb.getReference("cvUploads")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(jobApplyingContext, "Upload xong!", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            } else {

                            }
                        }
                    });

        }

    }

}
