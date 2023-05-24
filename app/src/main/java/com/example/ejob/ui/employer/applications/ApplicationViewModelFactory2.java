package com.example.ejob.ui.employer.applications;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ejob.ui.user.application.JobApplication;

public class ApplicationViewModelFactory2 implements ViewModelProvider.Factory {

    JobApplication jobApplication;
    String jobId;
    private Object[] mParams;

    public ApplicationViewModelFactory2(String jobid) {
        jobId = jobid;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        ApplicationViewModel applicationViewModel = new ApplicationViewModel(jobId);
        return (T) applicationViewModel;
    }

}
