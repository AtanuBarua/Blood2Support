package com.example.androidnotifications;

public class Users {

    String email;
    String name;
    String contactNumber;
    String city;
    String bloodGroup;
    String lat;
    String lan;
    String disString;
    String status;

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

    public Users() {

    }

    public Users(String email,String name, String contactNumber, String bloodGroup, String city, String lat, String lng, String status) {
        this.email = email;
        this.name = name;
        this.contactNumber = contactNumber;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.lat = lat;
        this.lan = lng;
        this.status = status;

    }

    public Users(String name, String contuctNumber, String bloodGroup) {
        this.name = name;
        this.contactNumber = contuctNumber;
        this.bloodGroup = bloodGroup;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;


    }




}
