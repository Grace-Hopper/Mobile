package es.eina.hopper.adapter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.receticas.R;
import retrofit2.Converter;
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
        if(myRecipe.getPicture()!=null) {
            if (myRecipe.getPicture().equals("")) {
                Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.recdefault);//image is your image
                bmp = Bitmap.createScaledBitmap(bmp, 500, 500, true);
                imagen_receta.setImageBitmap(bmp);
            } else {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(myRecipe.getPicture(), Base64.DEFAULT));
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                theImage = Bitmap.createScaledBitmap(theImage, 500, 500, true);
                imagen_receta.setImageBitmap(theImage);
            }
        }
        // Return the completed view to render on screen
        return convertView;
    }
}