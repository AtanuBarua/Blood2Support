package com.example.androidnotifications;

public class Request {
    public String mId, mTitle, mDesc, donorEmail, donorEmail_status, bloodGroup, donationDate;

    public Request() {

    }

    public Request(String mId, String mTitle, String mDesc, String bloodGroup, String donationDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.bloodGroup = bloodGroup;
        this.donationDate = donationDate;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }


    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }


    public String getDonorEmail_status() {
        return donorEmail_status;
    }

    public void setDonorEmail_status(String donorEmail_status) {
        this.donorEmail_status = donorEmail_status;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }


}
