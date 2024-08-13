package com.example.easyexit;

import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.tname;
import static com.example.easyexit.login.tphone;
import static com.example.easyexit.login.tyear;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class permission extends AppCompatActivity implements View.OnClickListener{
    Button submit;
    ImageView back;
    EditText rollno,phno,reason,time,branch,year;
    UserData2 ud;
    ProgressDialog p;
    String a1,a2,a3,a4,a5;

    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    java.sql.Date date;
    java.util.Date date1 = new java.util.Date();
    String currentDateTimeString;
    private static final String TAG = "CameraXApp";
    private static final int CAMERA_REQUEST_CODE = 100;
    private PreviewView previewView;
    private ImageCapture imageCapture;

    Intent i;
    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permition);
        back = (ImageView) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit2);
        rollno = (EditText) findViewById(R.id.rollno);
        phno = (EditText) findViewById(R.id.phone);
        time = (EditText) findViewById(R.id.time1);
        branch = (EditText) findViewById(R.id.branch);
        year = (EditText) findViewById(R.id.year);
        reason = (EditText) findViewById(R.id.editTextTextMultiLine);
        previewView = findViewById(R.id.previewView); //image preview
        p= new ProgressDialog(this);
        ud = new UserData2();
        time.setText(String.valueOf(date1));

        rollno.setText(tname);
        phno.setText(tphone);
        branch.setText(tbranch);
        year.setText(tyear);

        time.setEnabled(false);
        rollno.setEnabled(false);
        phno.setEnabled(false);
        branch.setEnabled(false);
        year.setEnabled(false);

        getSupportActionBar().hide();
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        long millis = System.currentTimeMillis();
         date = new java.sql.Date(millis);
         currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            startCamera();
        }
       // Toast.makeText(getApplicationContext(),"permission started",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Please provide Camara Access", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void takePhoto() {
        if (imageCapture == null) {
            return;
        }

        ImageCapture.OutputFileOptions outputOptions = createOutputOptions();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                String msg = "Photo capture succeeded: " + outputFileResults.getSavedUri();
                Toast.makeText(permission.this, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, msg);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private ImageCapture.OutputFileOptions createOutputOptions() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyApp");

        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        return new ImageCapture.OutputFileOptions.Builder(contentResolver, contentUri, contentValues).build();
    }
    @Override
    public void onClick(View view) {
        if(view==back)
        {
            finish();
//            Toast.makeText(getApplicationContext(),"Back pressed",Toast.LENGTH_SHORT).show();
        }
        if(view == submit)
        {
            submition();
        }
    }

    private void submition() {
        String a = rollno.getText().toString();
        String b = String.valueOf(currentDateTimeString);
        String c = reason.getText().toString();
        String d =phno.getText().toString();
        if(a.equals("") )
        {
            rollno.setError("enter Roll no");
        }
        else if(c.equals(""))
        {
            reason.setError(" enter reason");
        }
        else if(d.equals("")|d.length()<10)
        {
            phno.setError("enter valid phone number");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"permission started",Toast.LENGTH_SHORT).show();
            ud.setRollno(a);
            ud.setTime(b);
            ud.setReason(c);
            ud.setNumber(d);
            ud.setStatus("waiting");
            ud.setOutTime(null);

            p.setMessage("Please wait uploading...");
            p.setTitle("Registration");
            p.setCanceledOnTouchOutside(false);
            p.show();
            databaseReference.child(String.valueOf(date)).child(ud.getRollno()).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Uploaded successfully",Toast.LENGTH_SHORT).show();
                   /* try {
                       // SmsManager smsManager = SmsManager.getDefault();
                        String msg = "There is an Approval request for "+tname;
                        String data5 = "8639439120";
                        // smsManager.sendTextMessage(tfacaltyno,null,msg,null,null);}
                      //  smsManager.sendTextMessage(data5, null, msg, null, null);
                        Toast.makeText(getApplicationContext(), "Request message sent", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Request message not sent due to "+e,Toast.LENGTH_SHORT).show();
                    }*/
                    p.dismiss();
                    finish();
                }
            });
//            takePhoto();
//            p.dismiss();
        }
    }
}