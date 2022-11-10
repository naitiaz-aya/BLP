package com.lexus.blp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.net.time.TimeTCPClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PointageActivity extends AppCompatActivity {
    private EditText textView_data;
    private Button button_capture, bu;
    Bitmap bitmap ;
    private static final int REQUEST_CAMERA_CODE = 100;

    private TimeTCPClient timeTCPClient;
    Button btn, loca;

    String cameraPermission[];
    String storagePermission[];

    FusedLocationProviderClient fusedLocationProviderClient;

    private final  static  int REQUEST_CODE = 100;

    DBHelper instance;
    EditText  idblp, datePointage, lat, longi,  agent;
    Button _CustomButtomAdd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointage);
        btn = findViewById(R.id.btn);
        button_capture = findViewById(R.id.img);
        textView_data = findViewById(R.id.result);
        instance = new DBHelper(this);
        _CustomButtomAdd = findViewById(R.id.customButtomPointer);
        idblp = findViewById(R.id.idblp);
        datePointage = findViewById(R.id.datePointage);
        lat = findViewById(R.id.lat);
        longi = findViewById(R.id.longi);
        agent = findViewById(R.id.agent);

        //permission
        cameraPermission = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        storagePermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };



        if(ContextCompat.checkSelfPermission(PointageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PointageActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(PointageActivity.this);


            }
        });


        _CustomButtomAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] dtLimite = {"2022-10-28", "2021-11-11", "2022-04-29","2022-10-18", "2022-01-23", "2022-01-21"};
                String[] matriculeCa = {"64942-1233","37808-666", "0472-3901", "0591-2157", "62785-001", "60760-902"};
                String latt = "31.630000";
                String logitu = "-8.008889";
                String idBLP = idblp.getText().toString();
                String dPointage = datePointage.getText().toString();
                String lati = lat.getText().toString();
                String longit = longi.getText().toString();
                String agt = agent.getText().toString();
                String matricule = textView_data.getText().toString();
                Boolean time = false;
                for (String dt : dtLimite) {

                    if (dt.equals(dPointage)) {

                        time = true;
                    }
                }

                Boolean zone = false;

                if (lati == latt && longit == logitu) {
                    zone = true;
                }
                Boolean mt = false;
                for (String m : matriculeCa) {
                    if (m.equals(matricule)) {
                        mt = true;
                    }
                }
                Boolean lu = true;
                Boolean vd = false;
                if(lu == true && zone == true && time == true && lu ==true ){
                    vd = true;
                }
                boolean b = instance.insertDataIntoTheDB(idBLP,dPointage,lati, longit, agt, time, zone,vd ,mt, lu);

                if (b) {
                    Toast.makeText(PointageActivity.this, "VERIFICATION SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PointageActivity.this, "OOP'S SOMETHING WRONG :( ", Toast.LENGTH_SHORT).show();
                }
            }
        });



        lat = findViewById(R.id.lat);
        longi = findViewById(R.id.longi);
        loca = findViewById(R.id.loca);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PointageActivity.this);
        loca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
        //date
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        timeTCPClient = new TimeTCPClient();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    timeTCPClient.connect("time.nist.gov");
                    try {
                        datePointage.setText(timeTCPClient.getDate().toString().substring(0, 20));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //date
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()){
            Toast.makeText(PointageActivity.this,"Error", Toast.LENGTH_SHORT).show();
        }else{
            Frame frame  = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            textView_data.setText(stringBuilder.toString());
            button_capture.setText("Retake");
        }
    }

    private void getLastLocation(){
        if (ContextCompat.checkSelfPermission(PointageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                try {
                                    Geocoder geocoder = new Geocoder(PointageActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    lat.setText(""+addresses.get(0).getLatitude());
                                    longi.setText(""+addresses.get(0).getLongitude());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else {
            askPermission();
        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(PointageActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(PointageActivity.this,"Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}