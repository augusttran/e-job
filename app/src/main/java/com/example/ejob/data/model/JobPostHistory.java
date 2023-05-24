package com.example.ejob.data.model;

import com.example.ejob.ui.employer.job.JobPosting;

import java.util.List;

public class JobPostHistory {
    String jobposthistoryId;
    List<JobPosting> jobPostingList;


    public JobPostHistory() {
    }

    public JobPostHistory(String jobposthistoryId, List<JobPosting> jobPostingList) {
        this.jobposthistoryId = jobposthistoryId;
        this.jobPostingList = jobPostingList;
    }

    public String getJobposthistoryId() {
        return jobposthistoryId;
    }

    public void setJobposthistoryId(String jobposthistoryId) {
        this.jobposthistoryId = jobposthistoryId;
    }

    public List<JobPosting> getJobPostingList() {
        return jobPostingList;
    }

    public void setJobPostingList(List<JobPosting> jobPostingList) {
        this.jobPostingList = jobPostingList;
    }
}
