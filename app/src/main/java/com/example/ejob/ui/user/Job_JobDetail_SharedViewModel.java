package com.example.ejob.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ejob.ui.user.userjob.JobPostingforUser;

public class Job_JobDetail_SharedViewModel extends ViewModel {

    private MutableLiveData<JobPostingforUser> jobPostingforUserMutableLiveData = new MutableLiveData<>();

    public void setText(JobPostingforUser input) {
        jobPostingforUserMutableLiveData.setValue(input);

    }

    public LiveData<JobPostingforUser> getJobPostingforUser() {
        return jobPostingforUserMutableLiveData;
    }

}
