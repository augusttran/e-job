package com.example.ejob.ui.user.application;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ejob.data.model.ApplicationStatus;

public class JobApplication implements Parcelable {

    String applicantID;
    String applicationId;
    String position;
    String selfDescription;
    String applicationDate;
    String cvitaeLink;
    String applicationStatus;
    String applicantFullname;
    String applicantEmail;
    String applicantPhone;
    String applicantSocialmedia;
    String applicantUniversity;
    String applicantAddress;
    String photoURL;
    String employerFbId;
    String employerFullname;
    String jobID;
    String jobType;
    String jobLocation;

    public JobApplication() {
    }

    public JobApplication(String applicantID, String applicationId, String position, String selfDescription, String applicationDate, String cvitaeLink, String applicationStatus, String applicantFullname, String applicantEmail, String applicantPhone, String applicantSocialmedia, String applicantUniversity, String applicantAddress, String photoURL, String employerFbId, String employerFullname, String jobID, String jobType, String jobLocation) {
        this.applicantID = applicantID;
        this.applicationId = applicationId;
        this.position = position;
        this.selfDescription = selfDescription;
        this.applicationDate = applicationDate;
        this.cvitaeLink = cvitaeLink;
        this.applicationStatus = applicationStatus;
        this.applicantFullname = applicantFullname;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantSocialmedia = applicantSocialmedia;
        this.applicantUniversity = applicantUniversity;
        this.applicantAddress = applicantAddress;
        this.photoURL = photoURL;
        this.employerFbId = employerFbId;
        this.employerFullname = employerFullname;
        this.jobID = jobID;
        this.jobType = jobType;
        this.jobLocation = jobLocation;
    }


    protected JobApplication(Parcel in) {
        applicantID = in.readString();
        applicationId = in.readString();
        position = in.readString();
        selfDescription = in.readString();
        applicationDate = in.readString();
        cvitaeLink = in.readString();
        applicationStatus = in.readString();
        applicantFullname = in.readString();
        applicantEmail = in.readString();
        applicantPhone = in.readString();
        applicantSocialmedia = in.readString();
        applicantUniversity = in.readString();
        applicantAddress = in.readString();
        photoURL = in.readString();
        employerFbId = in.readString();
        employerFullname = in.readString();
        jobID = in.readString();
        jobType = in.readString();
        jobLocation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicantID);
        dest.writeString(applicationId);
        dest.writeString(position);
        dest.writeString(selfDescription);
        dest.writeString(applicationDate);
        dest.writeString(cvitaeLink);
        dest.writeString(applicationStatus);
        dest.writeString(applicantFullname);
        dest.writeString(applicantEmail);
        dest.writeString(applicantPhone);
        dest.writeString(applicantSocialmedia);
        dest.writeString(applicantUniversity);
        dest.writeString(applicantAddress);
        dest.writeString(photoURL);
        dest.writeString(employerFbId);
        dest.writeString(employerFullname);
        dest.writeString(jobID);
        dest.writeString(jobType);
        dest.writeString(jobLocation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobApplication> CREATOR = new Creator<JobApplication>() {
        @Override
        public JobApplication createFromParcel(Parcel in) {
            return new JobApplication(in);
        }

        @Override
        public JobApplication[] newArray(int size) {
            return new JobApplication[size];
        }
    };

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCvitaeLink() {
        return cvitaeLink;
    }

    public void setCvitaeLink(String cvitaeLink) {
        this.cvitaeLink = cvitaeLink;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicantFullname() {
        return applicantFullname;
    }

    public void setApplicantFullname(String applicantFullname) {
        this.applicantFullname = applicantFullname;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantSocialmedia() {
        return applicantSocialmedia;
    }

    public void setApplicantSocialmedia(String applicantSocialmedia) {
        this.applicantSocialmedia = applicantSocialmedia;
    }

    public String getApplicantUniversity() {
        return applicantUniversity;
    }

    public void setApplicantUniversity(String applicantUniversity) {
        this.applicantUniversity = applicantUniversity;
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getEmployerFbId() {
        return employerFbId;
    }

    public void setEmployerFbId(String employerFbId) {
        this.employerFbId = employerFbId;
    }

    public String getEmployerFullname() {
        return employerFullname;
    }

    public void setEmployerFullname(String employerFullname) {
        this.employerFullname = employerFullname;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }
}
