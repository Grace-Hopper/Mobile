package es.eina.hopper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.receticas.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by davy on 3/04/17.
 */


public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
    List<Ingredient> list;
    final ArrayAdapter<Ingredient> a;
    final ArrayList<View> vistas;
    public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredientes) {
        super(context, 0, ingredientes);
        list = ingredientes;
        a = this;
        vistas = new ArrayList<>();
        for(int i = 0;i < list.size(); i++){}
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Ingredient mIngredient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_ingredientes, parent, false);
        }
        // Lookup view for data population
        final EditText nombre_ingrediente = (EditText) convertView.findViewById(R.id.ingredienteText);
        nombre_ingrediente.setText(mIngredient.getName());
        nombre_ingrediente.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mIngredient.setName(nombre_ingrediente.getText().toString());
            }
        });
        final EditText cantidad_ingrediente = (EditText) convertView.findViewById(R.id.ingredienteQuantity);
        cantidad_ingrediente.setText(mIngredient.getName());
        cantidad_ingrediente.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mIngredient.setName(cantidad_ingrediente.getText().toString());
            }
        });

        Button add = (Button) convertView.findViewById(R.id.ingredienteButton);
        if(position==list.size()-1){
            add.setText("+");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.add(new Ingredient(-1, "", "", null));
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

    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> aux = new ArrayList<Ingredient>();
        for(int i=0;i<list.size();i++){
            if(!list.get(i).getName().equals("")) {
                aux.add(list.get(i));
            }
        }
        return aux;
    }
}

