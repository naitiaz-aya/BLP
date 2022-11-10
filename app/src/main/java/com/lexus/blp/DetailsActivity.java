package com.lexus.blp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {
    TextView mat, idblp, tge, atc, clt,  dts, pex, vd;
    Button verificationbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = this.getIntent();


        String matriculeCa = intent.getStringExtra("matriculeCa");
        String article = intent.getStringExtra("article");
        String tonnage = intent.getStringExtra("tonnage");
        String villeDestination = intent.getStringExtra("villeDestination");
        String dtSortie = intent.getStringExtra("dtSortie");
        String pexSortie = intent.getStringExtra("pexSortie");
        String client = intent.getStringExtra("client");
        String idBl = intent.getStringExtra("idBl");

        mat = (TextView) findViewById(R.id.mat);
        idblp = (TextView) findViewById(R.id.idblp);
        tge = (TextView) findViewById(R.id.tge);
        atc = (TextView) findViewById(R.id.atc);
        vd = (TextView) findViewById(R.id.vd);
        dts = (TextView) findViewById(R.id.dts);
        clt = (TextView) findViewById(R.id.clt);
        pex = (TextView) findViewById(R.id.pex);

        mat.setText(matriculeCa);
        idblp.setText(idBl);
        atc.setText(article);
        tge.setText(tonnage);
        vd.setText(villeDestination);
        dts.setText(dtSortie);
        pex.setText(pexSortie);
        clt.setText(client);
        verificationbtn = findViewById(R.id.verificationbtn);
        verificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointageActivity.class);
                startActivity(intent);
            }
        });

    }
}
