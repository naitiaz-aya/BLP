package com.lexus.blp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<String> {

     Activity context;
     String[] matriculeCa;
     String[] article;
     String[] villeDestination;
     String[] idBl;


    public ListAdapter( Activity context,String[] matriculeCa,String[] article,String[] villeDestination, String[] idBl) {
        super(context, R.layout.list_item, matriculeCa);
        this.context=context;
        this.matriculeCa=matriculeCa;
        this.article=article;
        this.villeDestination=villeDestination;
        this.idBl=idBl;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView matricule = (TextView) rowView.findViewById(R.id.matriculeCa);
        TextView artl= (TextView) rowView.findViewById(R.id.article);
        TextView vDestination = (TextView) rowView.findViewById(R.id.villeDestination);
        TextView iBl = (TextView) rowView.findViewById(R.id.idBl);

        matricule.setText(matriculeCa[position]);
        artl.setText(article[position]);
        vDestination.setText(villeDestination[position]);
        iBl.setText(idBl[position]);


        return rowView;

    };
}
