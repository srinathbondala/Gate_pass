package com.example.easyexit;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NotificationManagerClass {

    private final DatabaseReference databaseReference;
    private final StorageReference storageReference;
    private final Context context;

    public NotificationManagerClass(Context context) {
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void deleteNotification(String faculty, String title, String imageUrl, String Range) {
        databaseReference.child(Range).child(title.toLowerCase()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Notification deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete notification", Toast.LENGTH_SHORT).show();
                    }
                });

        // Delete image from Firebase Storage (if needed)
        if (imageUrl != null && !imageUrl.isEmpty()) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            imageRef.delete().addOnSuccessListener(aVoid ->
                    Toast.makeText(context, "Image deleted successfully", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show()
            );
        }

    }
}
