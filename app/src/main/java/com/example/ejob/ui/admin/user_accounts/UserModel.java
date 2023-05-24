package com.example.ejob.ui.admin.user_accounts;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ejob.data.model.Industry;
import com.example.ejob.data.model.JobPostHistory;
import com.example.ejob.ui.employer.job.JobPosting;

import java.util.ArrayList;

public class UserModel implements Parcelable {
    String userId;
    String userFullname;
    String usrSchool;
    String usrEmail;
    String userPhone;
    String userStatus;
    String usrAddress;
    Industry industry;
    String dateCreated;
    String photoUrl;
    String cvUploadLink;
    String letterLink;
    String availability;

    public UserModel() {

    }

    public UserModel(String userId, String userFullname, String usrSchool, String usrEmail, String userPhone,
            String userStatus, String usrAddress, Industry industry, String dateCreated, String photoUrl,
            String cvUploadLink, String letterLink, String availability) {
        this.userId = userId;
        this.userFullname = userFullname;
        this.usrSchool = usrSchool;
        this.usrEmail = usrEmail;
        this.userPhone = userPhone;
        this.userStatus = userStatus;
        this.usrAddress = usrAddress;
        this.industry = industry;
        this.dateCreated = dateCreated;
        this.photoUrl = photoUrl;
        this.cvUploadLink = cvUploadLink;
        this.letterLink = letterLink;
        this.availability = availability;
    }

    protected UserModel(Parcel in) {
        userId = in.readString();
        userFullname = in.readString();
        usrSchool = in.readString();
        usrEmail = in.readString();
        userPhone = in.readString();
        userStatus = in.readString();
        usrAddress = in.readString();
        dateCreated = in.readString();
        photoUrl = in.readString();
        cvUploadLink = in.readString();
        letterLink = in.readString();
        availability = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userFullname);
        dest.writeString(usrSchool);
        dest.writeString(usrEmail);
        dest.writeString(userPhone);
        dest.writeString(userStatus);
        dest.writeString(usrAddress);
        dest.writeString(dateCreated);
        dest.writeString(photoUrl);
        dest.writeString(cvUploadLink);
        dest.writeString(letterLink);
        dest.writeString(availability);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUsrSchool() {
        return usrSchool;
    }

    public void setUsrSchool(String usrSchool) {
        this.usrSchool = usrSchool;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUsrAddress() {
        return usrAddress;
    }

    public void setUsrAddress(String usrAddress) {
        this.usrAddress = usrAddress;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCvUploadLink() {
        return cvUploadLink;
    }

    public void setCvUploadLink(String cvUploadLink) {
        this.cvUploadLink = cvUploadLink;
    }

    public String getLetterLink() {
        return letterLink;
    }

    public void setLetterLink(String letterLink) {
        this.letterLink = letterLink;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
