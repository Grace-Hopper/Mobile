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
        this.mContext = mContext;
    }
    private class Holder
    {
        TextView mUten;
        Button borrar;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.lista_utensilios, null);
            holder.mUten = (TextView) convertView
                    .findViewById(R.id.utensilio);

            holder.borrar = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.mUten.setText(list.get(position).getName());
        holder.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                a.notifyDataSetChanged();
                AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren((ListView) parent);
            }
        });
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

    public boolean contiene(String compare, int position) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(compare) && i != position) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(Utensil utensil){
        if(!contiene(utensil.getName(),-1)){
            utensil.setName(utensil.getName().substring(0, 1).toUpperCase() + utensil.getName().substring( 1).toLowerCase());
            list.add(0, utensil);
            a.notifyDataSetChanged();
            AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(parent);
            return true;
        }
        else{
            return false;
        }
    }
}