package es.eina.hopper.adapter;


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
    public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredientes) {
        super(context, 0, ingredientes);
        list = ingredientes;
        a = this;
        if(ingredientes.size()<1){
            ingredientes.add(new Ingredient(0,"","",null));
        }
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
        TextWatcher watcher= new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mIngredient.setName(nombre_ingrediente.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
            }
        };

        nombre_ingrediente.addTextChangedListener(watcher);
        final EditText cantidad_ingrediente = (EditText) convertView.findViewById(R.id.ingredienteQuantity);
        cantidad_ingrediente.setText(mIngredient.getQuantity());
        TextWatcher watcher1= new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mIngredient.setQuantity(cantidad_ingrediente.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
            }
        };

        cantidad_ingrediente.addTextChangedListener(watcher1);

        Button add = (Button) convertView.findViewById(R.id.ingredienteButton);
        if(position==list.size()-1){
            add.setText("+");
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.add(new Ingredient(-1, "", "", null));
                    a.notifyDataSetChanged();
                    view.focusSearch(View.FOCUS_DOWN);
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
                        view.focusSearch(View.FOCUS_UP);
                    }
                    else{
                        list.get(position).setName("");
                        list.get(position).setQuantity("");
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
            if(!list.get(i).getName().equals("") || !list.get(i).getQuantity().equals("")) {
                aux.add(list.get(i));
            }
        }
        return aux;
    }
}

