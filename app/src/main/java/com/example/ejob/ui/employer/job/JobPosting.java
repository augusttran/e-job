package com.example.ejob.ui.employer.job;

import android.os.Parcel;
import android.os.Parcelable;

public class JobPosting implements Parcelable {
    String jobId;
    String jobTitle;
    String jobDescription;
    String jobLocation;
    String salary;
    String employerName;
    String jobDeadline;
    String jobDateCreated;
    String imageUrl;
    int jobSkills;
    int countView;
    int countLike;
    boolean jobStatus;
    int updateHistory;

    int numberApplied;

    public JobPosting() {

    }

    public JobPosting(String jobId, String jobTitle, String jobDescription, String jobLocation, String salary,
            String employerName, String jobDeadline, String jobDateCreated, int jobSkills, int countView, int countLike,
            boolean jobStatus, int updateHistory) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobLocation = jobLocation;
        this.salary = salary;
        this.employerName = employerName;
        this.jobDeadline = jobDeadline;
        this.jobDateCreated = jobDateCreated;
        this.jobSkills = jobSkills;
        this.countView = countView;
        this.countLike = countLike;
        this.jobStatus = jobStatus;
        this.updateHistory = updateHistory;
    }

    protected JobPosting(Parcel in) {
        jobId = in.readString();
        jobTitle = in.readString();
        jobDescription = in.readString();
        jobLocation = in.readString();
        salary = in.readString();
        employerName = in.readString();
        jobDeadline = in.readString();
        jobDateCreated = in.readString();
        imageUrl = in.readString();
        jobSkills = in.readInt();
        countView = in.readInt();
        countLike = in.readInt();
        jobStatus = in.readByte() != 0;
        updateHistory = in.readInt();
        numberApplied = in.readInt();
    }

    public static final Creator<JobPosting> CREATOR = new Creator<JobPosting>() {
        @Override
        public JobPosting createFromParcel(Parcel in) {
            return new JobPosting(in);
        }

        @Override
        public JobPosting[] newArray(int size) {
            return new JobPosting[size];
        }
    };

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getJobDeadline() {
        return jobDeadline;
    }

    public void setJobDeadline(String jobDeadline) {
        this.jobDeadline = jobDeadline;
    }

    public String getJobDateCreated() {
        return jobDateCreated;
    }

    public void setJobDateCreated(String jobDateCreated) {
        this.jobDateCreated = jobDateCreated;
    }

    public int getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(int jobSkills) {
        this.jobSkills = jobSkills;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public boolean isJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(boolean jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getUpdateHistory() {
        return updateHistory;
    }

    public void setUpdateHistory(int updateHistory) {
        this.updateHistory = updateHistory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumberApplied() {
        return numberApplied;
    }

    public void setNumberApplied(int numberApplied) {
        this.numberApplied = numberApplied;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeString(jobTitle);
        dest.writeString(jobDescription);
        dest.writeString(jobLocation);
        dest.writeString(salary);
        dest.writeString(employerName);
        dest.writeString(jobDeadline);
        dest.writeString(jobDateCreated);
        dest.writeString(imageUrl);
        dest.writeInt(jobSkills);
        dest.writeInt(countView);
        dest.writeInt(countLike);
        dest.writeByte((byte) (jobStatus ? 1 : 0));
        dest.writeInt(updateHistory);
        dest.writeInt(numberApplied);
    }
}
