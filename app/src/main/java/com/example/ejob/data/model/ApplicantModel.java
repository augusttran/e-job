package com.example.ejob.data.model;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.ui.user.userjob.JobPostingforUser;

import java.util.ArrayList;

public class ApplicantModel implements Parcelable {

    String applicantID;
    String applicantFullname;
    String applicantEmail;
    String applicantPhone;
    String applicantSocialmedia;
    String applicantUniversity;
    String applicantAddress;
    String photoURL;
    String cvURl;
    ArrayList<JobApplication> jobApplicationArrayList;
    ArrayList<JobPostingforUser> jobSaved;

    public ApplicantModel() {
    }

    public ApplicantModel(ApplicantModel applicantModel){
        this.applicantID = applicantModel.applicantID;
        this.applicantFullname = applicantModel.applicantFullname;
        this.applicantEmail = applicantModel.applicantEmail;
        this.applicantPhone = applicantModel.applicantPhone;
        this.applicantSocialmedia = applicantModel.applicantSocialmedia;
        this.applicantUniversity = applicantModel.applicantUniversity;
        this.applicantAddress = applicantModel.applicantAddress;
        this.photoURL = applicantModel.photoURL;
        this.cvURl = applicantModel.cvURl;
        this.jobApplicationArrayList = applicantModel.jobApplicationArrayList;
        this.jobSaved = applicantModel.jobSaved;
    }

    public ApplicantModel(String applicantID, String applicantFullname, String applicantEmail, String applicantPhone, String applicantSocialmedia, String applicantUniversity, String applicantAddress, String photoURL, String cvURl, ArrayList<JobApplication> jobApplicationArrayList, ArrayList<JobPostingforUser> jobSaved) {
        this.applicantID = applicantID;
        this.applicantFullname = applicantFullname;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantSocialmedia = applicantSocialmedia;
        this.applicantUniversity = applicantUniversity;
        this.applicantAddress = applicantAddress;
        this.photoURL = photoURL;
        this.cvURl = cvURl;
        this.jobApplicationArrayList = jobApplicationArrayList;
        this.jobSaved = jobSaved;
    }


    protected ApplicantModel(Parcel in) {
        applicantID = in.readString();
        applicantFullname = in.readString();
        applicantEmail = in.readString();
        applicantPhone = in.readString();
        applicantSocialmedia = in.readString();
        applicantUniversity = in.readString();
        applicantAddress = in.readString();
        photoURL = in.readString();
        cvURl = in.readString();
        jobApplicationArrayList = in.createTypedArrayList(JobApplication.CREATOR);
        jobSaved = in.createTypedArrayList(JobPostingforUser.CREATOR);
    }

    public static final Creator<ApplicantModel> CREATOR = new Creator<ApplicantModel>() {
        @Override
        public ApplicantModel createFromParcel(Parcel in) {
            return new ApplicantModel(in);
        }

        @Override
        public ApplicantModel[] newArray(int size) {
            return new ApplicantModel[size];
        }
    };

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
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

    public String getCvURl() {
        return cvURl;
    }

    public void setCvURl(String cvURl) {
        this.cvURl = cvURl;
    }

    public ArrayList<JobApplication> getJobApplicationArrayList() {
        return jobApplicationArrayList;
    }

    public void setJobApplicationArrayList(ArrayList<JobApplication> jobApplicationArrayList) {
        this.jobApplicationArrayList = jobApplicationArrayList;
    }

    public ArrayList<JobPostingforUser> getJobSaved() {
        return jobSaved;
    }

    public void setJobSaved(ArrayList<JobPostingforUser> jobSaved) {
        this.jobSaved = jobSaved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applicantID);
        dest.writeString(applicantFullname);
        dest.writeString(applicantEmail);
        dest.writeString(applicantPhone);
        dest.writeString(applicantSocialmedia);
        dest.writeString(applicantUniversity);
        dest.writeString(applicantAddress);
        dest.writeString(photoURL);
        dest.writeString(cvURl);
        dest.writeTypedList(jobApplicationArrayList);
        dest.writeTypedList(jobSaved);
    }
}
