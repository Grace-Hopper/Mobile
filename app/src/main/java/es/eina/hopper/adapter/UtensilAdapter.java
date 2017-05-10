package es.eina.hopper.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.receticas.R;

/**
 * Created by angelp on 3/04/17.
 */


public class RecipesAdapter extends ArrayAdapter<Recipe> {
    public RecipesAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe myRecipe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_layout, parent, false);
        }
        // Lookup view for data population
        TextView nombre_receta = (TextView) convertView.findViewById(R.id.nombre_receta);
        // 22 caracteres maximo
        TextView descripcion_receta = (TextView) convertView.findViewById(R.id.descripcion_receta);
        ImageView imagen_receta = (ImageView) convertView.findViewById(R.id.imagen_receta);
        // Populate the data into the template view using the data object
        String aux = myRecipe.getName();
        if (aux.length() > 25){
            aux = aux.substring(0, 22) + "...";
        }
        nombre_receta.setText(aux);
        descripcion_receta.setText(Long.toString(myRecipe.getPerson()));
        imagen_receta.setImageResource(R.drawable.logo1);
        // Return the completed view to render on screen
        return convertView;
    }
}