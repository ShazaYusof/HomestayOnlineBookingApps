package com.example.finalyearproject.Model;

public class Booking {

    private String userName,userPhone,userEmail,homestayName,bookingID,checkInDate,checkOutDate,totalDays,totalPrice,homestayID,userID,homestayStatus;

    public Booking(){

    }

    public Booking(String userName, String userPhone, String userEmail, String homestayName, String checkInDate, String checkOutDate, String totalDays, String totalPrice, String homestayID, String userID, String homestayStatus) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.homestayName = homestayName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalDays = totalDays;
        this.totalPrice = totalPrice;
        this.homestayID = homestayID;
        this.userID = userID;
        this.homestayStatus = homestayStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHomestayName() {
        return homestayName;
    }

    public void setHomestayName(String homestayName) {
        this.homestayName = homestayName;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getHomestayStatus() {
        return homestayStatus;
    }

    public void setHomestayStatus(String homestayStatus) {
        this.homestayStatus = homestayStatus;
    }
}
