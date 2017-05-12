package es.eina.hopper.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.receticas.AddReceta;
import es.eina.hopper.receticas.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by davy on 3/04/17.
 **/


public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
    ArrayList<Ingredient> list;
    final ArrayAdapter<Ingredient> a;
    ListView parent;
    boolean cogerDatos=true;
    Activity mContext;
    public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredientes, ListView parent, Activity mContext) {
        super(context, 0, ingredientes);
        list = ingredientes;
        this.parent = parent;
        a = this;
        if(ingredientes.size()<1){
            ingredientes.add(new Ingredient(0,"",""));
        }
        this.mContext = mContext;
    }
    private class Holder
    {
        EditText mNombre;
        EditText mCantidad;
        Button add;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater =mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.lista_ingredientes, null);
            holder.mNombre = (EditText) convertView.findViewById(R.id.ingredienteText);
            holder.mCantidad = (EditText) convertView.findViewById(R.id.ingredienteQuantity);
            holder.add = (Button) convertView.findViewById(R.id.ingredienteButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Lookup view for data population
        holder.mNombre.setText(list.get(position).getName());
        TextWatcher watcher= new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
                if(cogerDatos) {
                    list.get(position).setName(holder.mNombre.getText().toString());
                }
            }
        };

        holder.mNombre.addTextChangedListener(watcher);
        holder.mCantidad.setText(list.get(position).getQuantity());
        TextWatcher watcher1= new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
                if(cogerDatos) {
                    list.get(position).setQuantity(holder.mCantidad.getText().toString());
                }
            }
        };

        holder.mCantidad.addTextChangedListener(watcher1);

        if(position==list.size()-1){
            holder.add.setText("+");
            final ListView lp = this.parent;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cogerDatos=false;
                    list.add(new Ingredient(-1, "", ""));
                    a.notifyDataSetChanged();
                    ArrayList<Ingredient> li = (ArrayList)list.clone();
                    for(int i=0;i<li.size();i++){
                        System.out.println("INGREDIENTES: " + li.get(i).getName());
                    }
                    AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(lp);
                    cogerDatos=true;
                    for(int i=0;i<li.size();i++){
                        System.out.println("INGREDIENTES: " + li.get(i).getName());
                    }
                }
            });
        }
        else{
            holder.add.setText("-");
            final ListView lp = this.parent;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cogerDatos=false;
                    if(list.size()>1) {
                        list.remove(position);
                    }
                    else {
                        list.get(position).setName("");
                        list.get(position).setQuantity("");
                    }
                    a.notifyDataSetChanged();
                    AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(lp);
                    cogerDatos=true;
                    for(int i=0;i<list.size();i++){
                        System.out.println("INGREDIENTES: " + list.get(i).getName());
                    }
                }
            });
        }
        // Return the completed view to render on screen
        return convertView;
    }

    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> aux = new ArrayList<Ingredient>();
        for(int i=0;i<list.size();i++){
            if(!list.get(i).getName().equals("") || !list.get(i).getQuantity().equals("")) {
                aux.add(list.get(i));
            }
        }
        return aux;
    }
    public void setCogerDatos(boolean a){
        cogerDatos=a;
    }
}

