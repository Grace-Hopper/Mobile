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
import android.widget.ImageButton;
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
        this.mContext = mContext;
    }
    private class Holder
    {
        TextView mNombre;
        TextView mCantidad;
        Button borrar;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater =mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.lista_ingredientes, null);
            holder.mNombre = (TextView) convertView.findViewById(R.id.ingredienteText);
            holder.mCantidad = (TextView) convertView.findViewById(R.id.ingredienteQuantity);
            holder.borrar = (Button) convertView.findViewById(R.id.eliminar);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Lookup view for data population
        holder.mNombre.setText(list.get(position).getName());
        holder.mCantidad.setText(list.get(position).getQuantity());
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

    public boolean contiene(String compare, int position) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(compare) && i != position) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(Ingredient ingredient){
        if(!contiene(ingredient.getName(),-1)){
            ingredient.setName(ingredient.getName().substring(0, 1).toUpperCase() + ingredient.getName().substring( 1).toLowerCase());
            list.add(ingredient);
            a.notifyDataSetChanged();
            AddReceta.DescripcionReceta.setListViewHeightBasedOnChildren(parent);
            return true;
        }
        else{
            return false;
        }
    }
}

