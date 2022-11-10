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
    TextView date;
    Button btn, loca;

    String cameraPermission[];
    String storagePermission[];

    FusedLocationProviderClient fusedLocationProviderClient;

    private final  static  int REQUEST_CODE = 100;

    EditText mResult;
    String[] idBl = { "1","2","3","4","5","6"};

    DBHelper instance;
    EditText  idblp, datePointage, lat, longi,  agent;
    CheckBox intime, inzone,  valide, matriculeOK, matriculeLu;
    Button _CustomButtomAdd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointage);
        btn = findViewById(R.id.btn);
        button_capture = findViewById(R.id.img);
        textView_data = findViewById(R.id.result);
        instance = new DBHelper(this);
        mResult = findViewById(R.id.result);
        //mImg = findViewById(R.id.img);
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



       /* intime = findViewById(R.id.intime);
        inzone = findViewById(R.id.inzone);
        novalide = findViewById(R.id.valide);
        matriculeOK = findViewById(R.id.matriculeOK);
        matriculeLu = findViewById(R.id.matriculeLu);

        //in zone
        boolean zone;
        if (inzone.isChecked()) {
            zone = true;

        } else {
            zone = false;
        }
        //valide
        boolean vl;
        if (valide.isChecked()) {
            vl = true;

        } else {
            vl = false;
        }
        //Matricule OK
        boolean ok;
        if (matriculeOK.isChecked()) {
            ok = true;

        } else {
            ok = false;
        }
        //matricule Lu
        boolean lu;
        if (matriculeLu.isChecked()) {
            lu = true;

        } else {
            lu = false;
        }

         */
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
        _AddData();

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
/*
                String url = "https://time.is/Unix_time_now";
                Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                String[] tags = new String[] {
                        "div[id=time_section]",
                        "div[id=clock0_bg]"
                };
                Elements elements= doc.select(tags[0]);
                for (int i = 0; i <tags.length; i++) {
                    elements = elements.select(tags[i]);
                }
                return Long.parseLong(elements.text() + "000");
            }*/
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




  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.addImage) {
            showImageImportDialog();
        }
        if(id == R.id.settings){
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {

        String[] items = {" Camera" , " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    //camera option Click
                    if (!checkCameraPermission()){
                        //camera permission not allowed, request it;
                        requestCameraPermission();
                    }else{
                        //camera permission allowed
                        pickCamera();
                    }
                }
                if (which == 1){
                    //Gallery option Click
                    if (!checkStoragePermission()){
                        //Storage permission not allowed, request it;
                        requestStoragePermission();
                    }else{
                        //Storage permission allowed
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show();


    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mStartForResult.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                    }
            }
    });
    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Reconnaissance de Matricule");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent =   new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        mStartForResult.launch(cameraIntent);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result ;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }


    private boolean checkCameraPermission() {
        /*  Check camera permission and return the result
        *in order to get high quality  image we have to save image to external storage first
        * before inserting to image view that's why storage permission will also be required
        *
       boolean result  = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }
*/


    public void _AddData() {
        _CustomButtomAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean b = instance.insertDataIntoTheDB( Integer.valueOf(idblp.getText().toString().trim()), datePointage.getText().toString().trim(), lat.getText().toString().trim(), longi.getText().toString().trim(), agent.getText().toString().trim(), Boolean.valueOf(intime.getText().toString().trim()) , Boolean.valueOf(inzone.getText().toString().trim()), Boolean.valueOf(valide.getText().toString().trim()), Boolean.valueOf(matriculeOK.getText().toString().trim()), Boolean.valueOf(matriculeLu.getText().toString().trim()));

                if (b) {
                    Toast.makeText(PointageActivity.this, "VERIFICATION SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PointageActivity.this, "OOP'S SOMETHING WRONG :( ", Toast.LENGTH_SHORT).show();
                }
            }
        });
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