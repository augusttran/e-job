package com.example.ejob.ui.employer.job;

public abstract class AbsEmployer {

    private String name;
    private String email;
    private String accountDateCreation;
    private String phoneNumber;

    public AbsEmployer() {

    }

    public AbsEmployer(String name, String email, String accountDateCreation, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.accountDateCreation = accountDateCreation;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountDateCreation() {
        return accountDateCreation;
    }

    public void setAccountDateCreation(String accountDateCreation) {
        this.accountDateCreation = accountDateCreation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
