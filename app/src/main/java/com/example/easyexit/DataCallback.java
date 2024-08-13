package com.example.easyexit;

public interface DataCallback {
    void onDataFetched(UserData1 userData);
    void onError(String error);
}
