package com.example.finalyearproject;

import com.example.finalyearproject.Model.HomestayProfile;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<HomestayProfile> homestayList);
    void onFirebaseLoadFailed(String message);
}
