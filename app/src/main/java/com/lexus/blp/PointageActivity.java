package com.lexus.blp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class PointageActivity extends AppCompatActivity {
    DBHelper instance;
    EditText idpt, idblp, datePointage, lat, longi,  agent;
    CheckBox intime, inzone, valide,novalide, matriculeOK, matriculeLu;
    Button _CustomButtomAdd;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointage);

        instance = new DBHelper(this);


        _CustomButtomAdd = findViewById(R.id.customButtomPointer);
        idblp = findViewById(R.id.idblp);
        datePointage = findViewById(R.id.datePointage);
        lat = findViewById(R.id.lat);
        longi = findViewById(R.id.longi);
        agent = findViewById(R.id.agent);
        intime = findViewById(R.id.intime);
        inzone = findViewById(R.id.inzone);
        novalide = findViewById(R.id.valide);
        matriculeOK = findViewById(R.id.matriculeOK);
        matriculeLu = findViewById(R.id.matriculeLu);

        _AddData();
    }
    public void _AddData() {

        //in time
        boolean time;
        if (intime.isChecked()) {
            time = true;
        } else {
            time = false;
        }

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

        _CustomButtomAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean b = instance.insertDataIntoTheDB( Integer.valueOf(idblp.getText().toString().trim()), datePointage.getText().toString().trim(), lat.getText().toString().trim(), longi.getText().toString().trim(), agent.getText().toString().trim(), time , zone, vl, ok, lu);
                if (b == true) {
                    Toast.makeText(PointageActivity.this, "VERIFICATION SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PointageActivity.this, "OOP'S SOMETHING WRONG :( ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}