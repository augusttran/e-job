package com.example.ejob.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployerModel implements Parcelable {
    String employerId;
    String employerEmail;
    String employerWebsite;
    String employerFullname;
    String yearofFoundation;
    String employerIndustry;
    String employerPhone;
    String employerSize;
    String employerAddress;
    String dateCreationEmployer;
    String status;

    public EmployerModel(){

    }

    public EmployerModel(String employerId, String employerEmail, String employerWebsite, String employerFullname, String yearofFoundation, String employerIndustry, String employerPhone, String employerSize, String employerAddress, String dateCreationEmployer, String status) {
        this.employerId = employerId;
        this.employerEmail = employerEmail;
        this.employerWebsite = employerWebsite;
        this.employerFullname = employerFullname;
        this.yearofFoundation = yearofFoundation;
        this.employerIndustry = employerIndustry;
        this.employerPhone = employerPhone;
        this.employerSize = employerSize;
        this.employerAddress = employerAddress;
        this.dateCreationEmployer = dateCreationEmployer;
        this.status = status;
    }


    protected EmployerModel(Parcel in) {
        employerId = in.readString();
        employerEmail = in.readString();
        employerWebsite = in.readString();
        employerFullname = in.readString();
        yearofFoundation = in.readString();
        employerIndustry = in.readString();
        employerPhone = in.readString();
        employerSize = in.readString();
        employerAddress = in.readString();
        dateCreationEmployer = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(employerId);
        dest.writeString(employerEmail);
        dest.writeString(employerWebsite);
        dest.writeString(employerFullname);
        dest.writeString(yearofFoundation);
        dest.writeString(employerIndustry);
        dest.writeString(employerPhone);
        dest.writeString(employerSize);
        dest.writeString(employerAddress);
        dest.writeString(dateCreationEmployer);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EmployerModel> CREATOR = new Creator<EmployerModel>() {
        @Override
        public EmployerModel createFromParcel(Parcel in) {
            return new EmployerModel(in);
        }

        @Override
        public EmployerModel[] newArray(int size) {
            return new EmployerModel[size];
        }
    };

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public String getEmployerWebsite() {
        return employerWebsite;
    }

    public void setEmployerWebsite(String employerWebsite) {
        this.employerWebsite = employerWebsite;
    }

    public String getEmployerFullname() {
        return employerFullname;
    }

    public void setEmployerFullname(String employerFullname) {
        this.employerFullname = employerFullname;
    }

    public String getYearofFoundation() {
        return yearofFoundation;
    }

    public void setYearofFoundation(String yearofFoundation) {
        this.yearofFoundation = yearofFoundation;
    }

    public String getEmployerIndustry() {
        return employerIndustry;
    }

    public void setEmployerIndustry(String employerIndustry) {
        this.employerIndustry = employerIndustry;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }

    public String getEmployerSize() {
        return employerSize;
    }

    public void setEmployerSize(String employerSize) {
        this.employerSize = employerSize;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    public String getDateCreationEmployer() {
        return dateCreationEmployer;
    }

    public void setDateCreationEmployer(String dateCreationEmployer) {
        this.dateCreationEmployer = dateCreationEmployer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
