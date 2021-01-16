package com.example.androidnotifications;

public class DonorReceiver {

    String donorEmail;
    String receiverEmail;
    String donationDate;
    String city;
    String bloodGroup;
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

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }





    public DonorReceiver() {

    }

    public DonorReceiver(String donorEmail, String receiverEmail, String donationDate, String bloodGroup, String city, String donor_receiver) {
        this.donorEmail = donorEmail;
        this.receiverEmail = receiverEmail;
        this.donationDate = donationDate;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.donor_receiver = donor_receiver;

    }



}
