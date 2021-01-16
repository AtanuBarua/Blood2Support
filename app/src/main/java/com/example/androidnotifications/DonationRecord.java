package com.example.androidnotifications;

public class DonationRecord {

    String donorEmail;
    String receiverEmail;
    String city;
    String bloodGroup;
    String donationDate;
    String donor_receiver;


    public String getDonor_receiver() {
        return donor_receiver;
    }

    public void setDonor_receiver(String donor_receiver) {
        this.donor_receiver = donor_receiver;
    }


    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
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



    public DonationRecord() {

    }

    public DonationRecord(String donorEmail, String receiverEmail,  String city, String bloodGroup, String donationDate, String donor_receiver) {
        this.donorEmail = donorEmail;
        this.receiverEmail = receiverEmail;
        this.city = city;
        this.bloodGroup = bloodGroup;
        this.donationDate = donationDate;
        this.donor_receiver = donor_receiver;

    }


}
