package com.example.finalyearproject.Model;

public class HomestayProfile {

    public String homestayAddress;
    public String numBed;
    public String homestayName;
    public String homestayPrice;
    public String numToilet,homestayID;



    public HomestayProfile()
    {

    }

    public HomestayProfile(String homestayAddress, String numBed, String homestayName, String homestayPrice, String numToilet, String homestayID) {
        this.homestayAddress = homestayAddress;
        this.numBed = numBed;
        this.homestayName = homestayName;
        this.homestayPrice = homestayPrice;
        this.numToilet = numToilet;
        this.homestayID = homestayID;
    }

    public String getHomestayAddress() {
        return homestayAddress;
    }

    public void setHomestayAddress(String homestayAddress) {
        this.homestayAddress = homestayAddress;
    }

    public String getNumBed() {
        return numBed;
    }

    public void setNumBed(String numBed) {
        this.numBed = numBed;
    }

    public String getHomestayName() {
        return homestayName;
    }

    public void setHomestayName(String homestayName) {
        this.homestayName = homestayName;
    }

    public String getHomestayPrice() {
        return homestayPrice;
    }

    public void setHomestayPrice(String homestayPrice) {
        this.homestayPrice = homestayPrice;
    }

    public String getNumToilet() {
        return numToilet;
    }

    public void setNumToilet(String numToilet) {
        this.numToilet = numToilet;
    }

    public String getHomestayID() {
        return homestayID;
    }

    public void setHomestayID(String homestayID) {
        this.homestayID = homestayID;
    }

//    public String getPictureLink() {
//        return pictureLink;
//    }
//
//    public void setPictureLink(String pictureLink) {
//        this.pictureLink = pictureLink;
//    }
}
