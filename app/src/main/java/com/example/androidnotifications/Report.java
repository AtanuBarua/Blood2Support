package com.example.androidnotifications;

public class Report {

    String donorName;
    String receiverEmail;
    String donationDate;





    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }



    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorEmail) {
        this.donorName = donorName;
    }





    public Report() {

    }

    public Report(String donorName, String receiverEmail, String donationDate) {
        this.donorName = donorName;
        this.receiverEmail = receiverEmail;
        this.donationDate = donationDate;


    }



}
