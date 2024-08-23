package com.example.easyexit;

import java.util.ArrayList;

public interface FirebaseDataCallback {
    void onDataFetched(ArrayList<notification_data> data);
}
