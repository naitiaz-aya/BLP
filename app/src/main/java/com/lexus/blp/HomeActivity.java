package com.lexus.blp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.lexus.blp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.net.time.TimeTCPClient;

public class HomeActivity extends AppCompatActivity {


    //GP

    ListView listView;
    //GP
    Button logout;
    DBHelper DB;


    String[] idBl = { "1","2","3","4","5","6"};
    String[] matriculeCa = {"64942-1233","37808-666", "0472-3901", "0591-2157", "62785-001", "60760-902"};
    String[] article = {"Béton","Béton","Ciment", "Béton", "Ciment", "Béton"};
    String[] tonnage = {"2", "5", "4", "3", "2", "4"};
    String[] villeDestination = {"Marrakech", "Marrakech", "Marrakech", "Marrakech", "Marrakech", "Marrakech"};
    String[] borne = {"Latitude : 31.6341600° Longitude : -7.9999400°", "Latitude : 31.6341600° Longitude : -7.9999400°", "Latitude : 31.6341600° Longitude : -7.9999400°" ,"Latitude : 31.6341600° Longitude : -7.9999400°" ,"Latitude : 31.6341600° Longitude : -7.9999400°" ,"Latitude : 31.6341600° Longitude : -7.9999400°"};
    String[] dtSortie = {"2021-11-21", "2021-10-05", "2022-01-07", "2022-06-22", "2022-01-23", "2021-10-05"};
    String[] pexSortie = {"Beni Melal", "Beni Melal", "Beni Melal", "Beni Melal" ,"Beni Melal", "Beni Melal"};
    String[] dtLimite = {"2022-10-28", "2021-11-11", "2022-04-29","2022-10-18", "2022-01-23", "2022-01-21"};
    String[] client = {"rchoffin4", "krickerby7", "nlatorreb", "nquenelle", "bcrowa", "npassief"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //DB



        ListAdapter blpArrayList = new ListAdapter(this, matriculeCa, article,villeDestination, idBl);

        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(blpArrayList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
                Intent i = new Intent(HomeActivity.this, DetailsActivity.class);

                i.putExtra("matriculeCa", matriculeCa[position]);
                i.putExtra("article", article[position]);
                i.putExtra("tonnage", tonnage[position]);
                i.putExtra("villeDestination", villeDestination[position]);
                i.putExtra("borne", borne[position]);
                i.putExtra("dtSortie", dtSortie[position]);
                i.putExtra("pexSortie", pexSortie[position]);
                i.putExtra("dtLimte", dtLimite[position]);
                i.putExtra("client", client[position]);
                i.putExtra("idBl", idBl[position]);

                startActivity(i);
            }

        });

        logout = findViewById(R.id.logout);


        //GP        //GP

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("password", "");
                editor.putString("username", "");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        //logout

        //date

    }
    //date


}