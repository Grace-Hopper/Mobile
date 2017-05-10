package es.eina.hopper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Utensil;
import es.eina.hopper.receticas.R;

/**
 * Created by angelp on 3/04/17.
 */


public class UtensilAdapter extends ArrayAdapter<Utensil> {
    List<Utensil> list;
    final ArrayAdapter<Utensil> a;
    final ArrayList<View> vistas;
    public UtensilAdapter(Context context, ArrayList<Utensil> utensils) {
        super(context, 0, utensils);
        list=utensils;
        a=this;
        vistas=new ArrayList<>();
        for(int i = 0;i<list.size();i++) {
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Utensil mUten = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_utensilios, parent, false);
        }

        final EditText utensilio = (EditText) convertView.findViewById(R.id.utensilio);
        utensilio.setText(mUten.getName());
        utensilio.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mUten.setName(utensilio.getText().toString());
            }
        });
        Button add = (Button) convertView.findViewById(R.id.button);
        if(position==list.size()-1){
            add.setText("+");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.add(new Utensil(0, ""));
                    a.notifyDataSetChanged();
                }
            });
        }
        else{
            add.setText("-");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list.size()>1) {
                        list.remove(position);
                    }
                    else{
                        list.get(position).setName("");
                    }
                    a.notifyDataSetChanged();
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    public ArrayList<Utensil> getUtensilios() {
        ArrayList<Utensil> aux = new ArrayList<Utensil>();
        for(int i=0;i<list.size();i++){
            if(!list.get(i).getName().equals("")) {
                aux.add(list.get(i));
            }
        }
        return aux;
    }
}