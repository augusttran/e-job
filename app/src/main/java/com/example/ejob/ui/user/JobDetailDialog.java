package com.example.ejob.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ejob.R;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.firebase.firestore.auth.User;

public class JobDetailDialog extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Job_JobDetail_SharedViewModel viewModel;

    private EditText etPositionApplyfor, etFullname, etAddress1, etAddress2, etPhone, etEmail, etMessage;
    private Button chooseFileCV, submitApplication;

    Bundle bundle;
    private TextView employerName, positionHiring, dateCreated;

    private UserHomeFragment userHomeFragment;

    private View v;

    public JobDetailDialog(Bundle thisbundle) {
        bundle = this.getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        bundle = this.getArguments();
        String jobTitle = bundle.getString("jobtitle");
        String jobemployer = bundle.getString("jobemployer");
        String dateCreateds = bundle.getString("jobdate");

        employerName.setText(jobemployer);
        positionHiring.setText(jobTitle);
        dateCreated.setText(dateCreateds);

        try {
            // Date dateCre =
            // Date.getInstance(Long.parseLong(thisJobpost.getJobDateCreated()));
            // this.dateCreated.setText(dateCre.toString());
        } catch (NumberFormatException nfe) {
            nfe.getMessage();
        }

        v = inflater.inflate(R.layout.fragment_application_job, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        employerName = v.findViewById(R.id.tvCompany);
        positionHiring = v.findViewById(R.id.tvPositionHiring);
        dateCreated = v.findViewById(R.id.tvJobDateCreated);

        chooseFileCV = v.findViewById(R.id.btnUploadCV);
        submitApplication = v.findViewById(R.id.btnSubmitApplication);

        chooseFileCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submitApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        etFullname = v.findViewById(R.id.etApplicantName);
        etPhone = v.findViewById(R.id.etApplicantPhone);
        etAddress1 = v.findViewById(R.id.etApplicantAddr);
        etAddress2 = v.findViewById(R.id.etApplicantAddr2);
        etEmail = v.findViewById(R.id.etApplicantEmail);
        etMessage = v.findViewById(R.id.etApplicantMessage);

    }

}
