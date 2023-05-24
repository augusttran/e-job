package com.example.ejob.ui.admin.employer_accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ejob.data.model.Industry;
import com.example.ejob.data.model.JobPostHistory;
import com.example.ejob.ui.employer.job.JobPosting;

import java.util.ArrayList;

public class Employer implements Parcelable {
    String employerId;
    String employerName;
    Industry industry;
    String phoneNumber;
    String employerLocation;
    String employerEmail;
    String photoUrl;
    ArrayList<JobPosting> allJobPosts;

    public static Creator<Employer> getCREATOR() {
        return CREATOR;
    }

    public Employer() {

    }

    public Employer(String employerId, String employerName, Industry industry, String employerLocation,
            JobPostHistory jobPostHistory) {
        this.employerId = employerId;
        this.employerName = employerName;
        this.industry = industry;
        this.employerLocation = employerLocation;
    }

    protected Employer(Parcel in) {
        employerId = in.readString();
        employerName = in.readString();
        employerLocation = in.readString();
    }

    public static final Creator<Employer> CREATOR = new Creator<Employer>() {
        @Override
        public Employer createFromParcel(Parcel in) {
            return new Employer(in);
        }

        @Override
        public Employer[] newArray(int size) {
            return new Employer[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getEmployerLocation() {
        return employerLocation;
    }

    public void setEmployerLocation(String employerLocation) {
        this.employerLocation = employerLocation;
    }

    public ArrayList<JobPosting> getAllJobPosts() {
        return allJobPosts;
    }

    public void setAllJobPosts(ArrayList<JobPosting> allJobPosts) {
        this.allJobPosts = allJobPosts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(employerId);
        dest.writeString(employerName);
        dest.writeString(employerLocation);
    }
}
