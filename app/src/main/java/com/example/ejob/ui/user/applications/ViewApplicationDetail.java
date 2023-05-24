package com.example.ejob.ui.user.applications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.CvRequiredType;
import com.example.ejob.ui.employer.applications.ViewPdf;
import com.example.ejob.ui.user.application.JobApplication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewApplicationDetail extends AppCompatActivity {

    private FirebaseFirestore db1, db2;
    private DatabaseReference db3;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDb;
    JobApplication jobApplication;
    String[] option3;
    ArrayAdapter arrayAdapter1;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView applicationName, applicant, madon, ngaytao, school, phone, address, email, cv, socialmedia, des, photo, status;
    private Button update;
    private TextInputLayout textInputLayout;
    String statusStr;
    View v;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_detail_user);
        mapping();
        initDb();
        setData();
        jobApplication = getIntent().getExtras().getParcelable("myapplication");

        applicationName.setText(jobApplication.getPosition());
        applicant.setText(jobApplication.getApplicantFullname());
        madon.setText(jobApplication.getApplicationId());
        ngaytao.setText(jobApplication.getApplicationDate());
        school.setText(jobApplication.getApplicantUniversity());
        phone.setText(jobApplication.getApplicantPhone());
        address.setText(jobApplication.getApplicantAddress());
        email.setText(jobApplication.getApplicantEmail());
        status.setText(jobApplication.getApplicationStatus());
        if(jobApplication.getApplicationStatus().equals("CANCELLED")){
            status.setText("Đã huỷ");
            status.setBackgroundResource(R.drawable.application_status_cancelled);
        }else if(jobApplication.getApplicationStatus().equals("SHORTLISTED")){
            status.setText("Đã được duyệt");
            status.setBackgroundResource(R.drawable.application_status_shortlisted);
        }else{
            status.setText("Đã nộp đơn");
            status.setBackgroundResource(R.drawable.application_status_submitted);
        }

        option3 = new String[]{"Nộp đơn", "Huỷ đơn"};
        arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.jobtype_option, option3);
        autoCompleteTextView.setText(arrayAdapter1.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(arrayAdapter1);

        JobApplication jobApplication2 = getIntent().getExtras().getParcelable("myapplication");

        if(jobApplication2.getApplicationStatus().equals("SUBMITTED")){
            autoCompleteTextView.setText(arrayAdapter1.getItem(0).toString(), false);
            autoCompleteTextView.setAdapter(arrayAdapter1);
        }else if(jobApplication2.getApplicationStatus().equals("CANCELLED")){
            autoCompleteTextView.setText(arrayAdapter1.getItem(1).toString(), false);
            autoCompleteTextView.setAdapter(arrayAdapter1);
        }else if(jobApplication2.getApplicationStatus().equals("SHORTLISTED")){
            autoCompleteTextView.setText("Đã được duyệt");
            textInputLayout.setVisibility(View.GONE);
//            autoCompleteTextView.setEnabled(false);
            update.setEnabled(false);
        }

        if(jobApplication2.getCvitaeLink() != null){
            cv.setEnabled(true);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPdf(jobApplication2);
                }
            });
        }else{
            cv.setText("Ứng viên không đính kèm CV");
        }

        try{
            if(jobApplication.getCvitaeLink() == null){
                cv.setText("Ứng viên không đính kèm CV");
            }else{
                cv.setText(jobApplication.getCvitaeLink());
            }

        }catch(NullPointerException npe){
            npe.getMessage();
        }

        des.setText(jobApplication.getSelfDescription());
        socialmedia.setText(jobApplication.getApplicantSocialmedia());

        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent(jobApplication2);
            }
        });

    }

    private void viewPdf(JobApplication application) {
        Intent intent = new Intent(ViewApplicationDetail.this, ViewPdf.class);
        intent.putExtra("cvUrl", application.getCvitaeLink());
        startActivity(intent);
    }


    private void updateEvent(JobApplication application) {
        if(autoCompleteTextView.getEditableText().toString().equals("Nộp đơn")){
            statusStr = "SUBMITTED";
        }else{
            statusStr = "CANCELLED";
        }
        Log.d("status", statusStr);

        db1.collection("Applications")
                .document(application.getApplicationId())
                .update("applicationStatus", statusStr)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Toast.makeText(ViewApplicationDetail.this, "Bạn đã " + autoCompleteTextView.getEditableText().toString().toLowerCase() + " thành công!", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void setData() {

    }

    private void initDb() {
        db1 = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fDb = FirebaseDatabase.getInstance();

    }

    private void mapping() {
            applicationName = findViewById(R.id.applicationName1);
            applicant = findViewById(R.id.applicantName3);
            madon = findViewById(R.id.etMadonungtuyen);
            ngaytao = findViewById(R.id.etNgaytaodon);
            school = findViewById(R.id.lamp);
            phone = findViewById(R.id.apPhone);
            address = findViewById(R.id.address);
            email = findViewById(R.id.apEmail);
            cv = findViewById(R.id.cvView);
            socialmedia = findViewById(R.id.apSocial);
            des = findViewById(R.id.apSelfdescription);
            status = findViewById(R.id.applicationStatus);
            autoCompleteTextView = findViewById(R.id.user_thaydoi_trangthai);
            update = findViewById(R.id.btnHuy);
            textInputLayout = findViewById(R.id.txtInputlayout);

    }
}
