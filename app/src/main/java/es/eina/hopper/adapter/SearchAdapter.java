package es.eina.hopper.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Utensil;
import es.eina.hopper.receticas.AddReceta;
import es.eina.hopper.receticas.Buscador;
import es.eina.hopper.receticas.R;

import static android.graphics.Color.*;

/**
 * Created by davy on 3/04/17.
 */


public class SearchAdapter extends ArrayAdapter<Ingredient> {
    List<Ingredient> list;
    final ArrayAdapter<Ingredient> a;
    final ArrayList<View> vistas;
    ListView parent;
    boolean cargarDatos = true;
    public SearchAdapter(Context context, ArrayList<Ingredient> ingredients, ListView parent) {
        super(context, 0, ingredients);
        list=ingredients;
        a=this;
        vistas=new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Ingredient mIngredient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_buscar, parent, false);
        }

        final TextView buscador = (TextView) convertView.findViewById(R.id.buscador);
        buscador.setText(mIngredient.getName());
        TextWatcher watcher= new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(cargarDatos){
                    mIngredient.setName(buscador.getText().toString());
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do something or nothing.
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do something or nothing
            }
        };

        buscador.addTextChangedListener(watcher);
        final ImageButton remove = (ImageButton) convertView.findViewById(R.id.button);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                cargarDatos = false;
                a.notifyDataSetChanged();
                cargarDatos = true;
                /*for(int i=0;i<list.size();i++){
                    System.out.println(list.get(i).getName());
                }*/
            }
        });
        // Return the completed view to render on screen
        return convertView;

    }

    void addItem(Ingredient ingredient){
        list.add(ingredient);
        cargarDatos = false;
        a.notifyDataSetChanged();
        cargarDatos = true;
    }

    public ArrayList<Ingredient> getIngredientes() {
        ArrayList<Ingredient> aux = new ArrayList<Ingredient>();
        for(int i=0;i<list.size();i++){
            if(!list.get(i).getName().equals("")) {
                aux.add(list.get(i));
            }
        }
        return aux;
    }
}