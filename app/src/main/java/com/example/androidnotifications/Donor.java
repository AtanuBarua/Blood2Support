package com.example.androidnotifications;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import java.util.Comparator;

public class Donor {
    String email;
    String name;
    String contactNumber;
    String city;
    String bloodGroup;
    String lat;
    String lan;
    String disString;
    String phonePrivacy;
    String availability;
    String city_bloodGroup;
    String status;
    //String city_bloodGroup_availability;
    String locationPrivacy;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public Donor() {

    }


    public Donor(String email,String name, String contactNumber, String bloodGroup, String city, String lat, String lng, String phonePrivacy, String availability,String city_bloodGroup,String locationPrivacy, String status) {
        this.email = email;
        this.name = name;
        this.contactNumber = contactNumber;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.lat = lat;
        this.lan = lng;
        this.phonePrivacy = phonePrivacy;
        this.availability = availability;
        this.city_bloodGroup = city_bloodGroup;
        //this.city_bloodGroup_availability = city_bloodGroup_availability;
        this.locationPrivacy = locationPrivacy;
        this.status = status;
    }

    public Donor(String name, String contactNumber, String bloodGroup) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.bloodGroup = bloodGroup;


    }

    public Donor(String name, String disString) {
        this.name = name;
        this.disString = disString;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistance() {
        return disString;
    }

    public void setDistance(String disString) {
        this.disString = disString;
    }

    public String getPhonePrivacy() {
        return phonePrivacy;
    }

    public void setPhonePrivacy(String phonePrivacy) {
        this.phonePrivacy = phonePrivacy;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCity_bloodGroup() {
        return city_bloodGroup;
    }

    public void setCity_bloodGroup(String city_bloodGroup) {

        this.city_bloodGroup = city_bloodGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

   /* public String getCity_bloodGroup_availability() {
        return city_bloodGroup_availability;
    }

    public void setCity_bloodGroup_availability(String city_bloodGroup_availability) {

        this.city_bloodGroup_availability = city_bloodGroup_availability;
    }*/

    public String getLocationPrivacy() {
        return locationPrivacy;
    }

    public void setLocationPrivacy(String locationPrivacy) {
        this.locationPrivacy = locationPrivacy;
    }

    public static Comparator<Donor> myDistance = new Comparator<Donor>() {
        @Override
        public int compare(Donor donor, Donor t1) {
            float r1 = Float.parseFloat(donor.getDistance());
            float r2 = Float.parseFloat(t1.getDistance());
            return Float.compare(r1,r2);
            //return donor.getDistance().compareTo(t1.getDistance());
        }
    };

}