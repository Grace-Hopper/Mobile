package es.eina.hopper.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Utensil;
import es.eina.hopper.receticas.AddReceta;
import es.eina.hopper.receticas.R;

/**
 * Created by angelp on 3/04/17.
 */


public class UtensilAdapter extends ArrayAdapter<Utensil> {
    List<Utensil> list;
    final ArrayAdapter<Utensil> a;
    ListView parent;
    boolean cogerDatos=true;
    private Activity mContext;
    public UtensilAdapter(Context context, ArrayList<Utensil> utensils, ListView parent, Activity mContext) {
        super(context, 0, utensils);
        list=utensils;
        a=this;
        this.parent = parent;
        if(utensils.size()<1){
            utensils.add(new Utensil(0,""));
        }
        this.mContext = mContext;
    }
    private class Holder
    {
        EditText mUten;
        Button add;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater =mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.lista_utensilios, null);
            holder.mUten = (EditText) convertView
                    .findViewById(R.id.utensilio);

            holder.add = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.mUten.setText(list.get(position).getName());
        TextWatcher watcher= new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
                if(cogerDatos) {
                    list.get(position).setName(holder.mUten.getText().toString());
                }
            }
        };

        holder.mUten.addTextChangedListener(watcher);
        if(position==list.size()-1){
            holder.add.setText("+");
            final ListView lp = this.parent;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cogerDatos=false;
                    list.add(new Utensil(0, ""));
                    a.notifyDataSetChanged();
                    AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(lp);
                    cogerDatos=true;
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
                    else{
                        list.get(position).setName("");
                    }
                    a.notifyDataSetChanged();
                    AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(lp);
                    cogerDatos=true;
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
    public void setCogerDatos(boolean a){
        cogerDatos=a;
    }
}