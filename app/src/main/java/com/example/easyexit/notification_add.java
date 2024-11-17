package com.example.easyexit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class notification_add extends AppCompatActivity implements View.OnClickListener{
    private Button submit;
    private java.sql.Date date;
    java.util.Date date1 = new java.util.Date();
    String currentDateTimeString;
    private ConstraintLayout image_select_container;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String url;
    private EditText name,code,description;
    private TextView message;
    private Spinner mode;
    private Uri imageuri;
    private ImageView imageView , select_file;
    ProgressDialog p;
    FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference reference;
    private FusedLocationProviderClient mFusedLocationClient;
    private notification_data data1;
    private String sname,scode,sdisc;
    private String smode;
    private ImageView back;
    private List<String> mode_name= Arrays.asList("Global","Local");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);
        Objects.requireNonNull(getSupportActionBar()).hide();
        back = (ImageView) findViewById(R.id.imageView12);
        back.setOnClickListener(this);
        select_file = findViewById(R.id.probimg);
        select_file.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        image_select_container = findViewById(R.id.image_select_container);
        name=findViewById(R.id.product_name_input);
        code=findViewById(R.id.product_code_input);
        Gson gson = new Gson();
        UserData1 userData = null;
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userDataJson = sharedPreferences.getString("userData", null);
        if (userDataJson != null) {
            userData = gson.fromJson(userDataJson, UserData1.class);
        }
        assert userData != null;
        code.setText(userData.getName());
        code.setEnabled(false);
        mode=findViewById(R.id.product_mode_input);
        description=findViewById(R.id.product_disc_input);
        message=findViewById(R.id.messagel);
        submit.setOnClickListener(this);
        imageView=findViewById(R.id.imageView);
        image_select_container.setVisibility(View.GONE);
        p=new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Notification");
        reference = FirebaseStorage.getInstance().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_snipper_item, mode_name);
            arrayAdapter.setDropDownViewResource(R.layout.simple_snipper_item);
            mode.setAdapter(arrayAdapter);
            mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    smode = mode_name.get(i);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getApplicationContext(), "Please select an Range", Toast.LENGTH_SHORT).show();;
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==back){
            finish();
        }
        if (select_file.equals(v)) {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }catch (Exception e)
            {}
        } else if (submit.equals(v)) {
            sname=name.getText().toString();
            scode=code.getText().toString();
            sdisc=description.getText().toString();
            try{
                if(sname.isEmpty()){
                    name.setError("Enter Title");
                }
                else if(scode.isEmpty())
                    code.setError("Enter proper code");
                else if(sdisc.isEmpty())
                    description.setError("Enter description");
                else {
                    update_data(sname,scode, smode,sdisc);
                }
            }
            catch (Exception e)
            { e.printStackTrace();}
        }
    }
    private void update_data(String sname, String scode, String smode, String sdisc) {
        p.setMessage("Please wait....");
        p.setTitle("Adding Notification");
        p.setCanceledOnTouchOutside(false);
        p.setCancelable(false);
        if(imageuri != null) {
            uplodeuri(imageuri);
        } else {
            Toast.makeText(getApplicationContext(), "select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uplodeuri(Uri imageuri) {
        p.show();
        StorageReference fileref = reference.child(System.currentTimeMillis() + "." + getFileExtenction(imageuri));
        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url= uri.toString();
                        long millis = System.currentTimeMillis();
                        date = new java.sql.Date(millis);
                        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        data1 = new notification_data(sname, url, scode, smode, sdisc, String.valueOf(currentDateTimeString));
                        myRef.child(smode).child(sname.toLowerCase()).setValue(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                p.dismiss();
                                Toast.makeText(getApplicationContext(), "Notification Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                p.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                p.dismiss();
                Toast.makeText(getApplicationContext(), "Upload failed"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getFileExtenction(Uri murl) {
        ContentResolver cr = getContentResolver();
        String mimeType = cr.getType(murl);
        if (mimeType != null) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        } else {
            return "";
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
                imageView.setImageBitmap(bitmap);
                message.setText("Image Selected");
                image_select_container.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroy() {
        if (p != null && p.isShowing()) {
            p.dismiss();
        }
        super.onDestroy();
    }
}