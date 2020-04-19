package com.example.finalyearproject.Model;

public class RateProfile {

    String reviewID;
    String homestayID;
    String userID;
    String reviewHomestay;
    String starHomestay;
    String homestayName;
    String userName;


    public RateProfile(){

    }

    public RateProfile(String homestayID, String userID, String reviewHomestay, String starHomestay, String homestayName, String userName) {

        this.homestayID = homestayID;
        this.userID = userID;
        this.reviewHomestay = reviewHomestay;
        this.starHomestay = starHomestay;
        this.homestayName = homestayName;
        this.userName = userName;
    }

    public String getRateID() {

        return reviewID;
    }

    public void setRateID(String rateID)
    {
        this.reviewID = reviewID;
    }

    public String getHomestayID() {
        return homestayID;
    }

    public void setHomestayID(String homestayID) {
        this.homestayID = homestayID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReviewHomestay() {
        return reviewHomestay;
    }

    public void setReviewHomestay(String reviewHomestay) {
        this.reviewHomestay = reviewHomestay;
    }

    public String getStarHomestay() {
        return starHomestay;
    }

    public void setStarHomestay(String starHomestay) {
        this.starHomestay = starHomestay;
    }

    public String getHomestayName() {
        return homestayName;
    }

    public void setHomestayName(String homestayName) {
        this.homestayName = homestayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
