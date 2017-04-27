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
import es.eina.hopper.models.User;
import es.eina.hopper.receticas.R;

/**
 * Created by Fernando on 27/04/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User us = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_user_layout, parent, false);
        }
        // Lookup view for data population
        TextView nombre_user = (TextView) convertView.findViewById(R.id.nombre_user);
        // Populate the data into the template view using the data object
        nombre_user.setText(us.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
