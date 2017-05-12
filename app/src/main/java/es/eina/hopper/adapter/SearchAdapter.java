package es.eina.hopper.adapter;


import android.app.Activity;
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
    ListView parent;
    boolean cargarDatos = true;
    Activity mContext;
    public SearchAdapter(Context context, ArrayList<Ingredient> ingredients, ListView parent, Activity mContext) {
        super(context, 0, ingredients);
        list=ingredients;
        this.parent = parent;
        a=this;
        //vistas=new ArrayList<>();
        this.mContext = mContext;
    }

    private class Holder
    {
        TextView mTextView;
        Button mButton;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (position < list.size()){
            final Holder holder;
            if (convertView == null) {
                holder = new Holder();
                LayoutInflater inflater = mContext.getLayoutInflater();
                convertView = inflater.inflate(R.layout.lista_buscar, null);
                holder.mTextView = (TextView) convertView.findViewById(R.id.textobuscador);
                holder.mButton = (Button) convertView.findViewById(R.id.imagebutton);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();

            }
            holder.mTextView.setText(list.get(position).getName());
        /*final TextView buscador = (TextView) convertView.findViewById(R.id.buscador);
        buscador.setText(mIngredient.getName());*/
            TextWatcher watcher= new TextWatcher() {
                public void afterTextChanged(Editable s) {

                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Do something or nothing.
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Do something or nothing
                    if(cargarDatos){
                        list.get(position).setName(holder.mTextView.getText().toString());
                    }
                }
            };

            holder.mTextView.addTextChangedListener(watcher);
            //final ImageButton remove = (ImageButton) convertView.findViewById(R.id.button);
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    cargarDatos = false;
                    a.notifyDataSetChanged();
                    cargarDatos = true;
                    //mostrar();
                /*for(int i=0;i<list.size();i++){
                    System.out.println(list.get(i).getName());
                }*/
                }
            });
            // Return the completed view to render on screen
        }
        return convertView;
    }

    public void addItem(Ingredient ingredient){
        list.add(ingredient);
        cargarDatos = false;
        a.notifyDataSetChanged();
        cargarDatos = true;
        mostrar();
    }

    public boolean contains(Ingredient ingredient){
        boolean encontrado = false;
        for(int i=0;i<list.size() && !encontrado;i++){
            if(list.get(i).getName().equals(ingredient.getName())){
                System.out.println(i + ": " + list.get(i).getName());
                encontrado = true;
            }
        }
        return encontrado;
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

    public void mostrar(){
        for(int i=0;i<list.size();i++){
            System.out.println(i + ": " + list.get(i).getName());
        }
    }


}