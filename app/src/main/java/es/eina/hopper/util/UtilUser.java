package es.eina.hopper.util;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
import es.eina.hopper.receticas.RecipesDbAdapter;

/**
 * Created by Fernando on 27/04/2017.
 */

public class UtilUser {
    /*public static User getUser(String user, Context ctx){
        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();
        Cursor aux = mDb.fetchRecipe(rowId);
    }*/
    public static List<User> getAll(Context ctx){
        List<User> listaFinal = new ArrayList<User>();
        //en local
        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();


        Cursor aux = mDb.fetchAllUsers();

        aux.moveToFirst();

        System.out.println("Numero de filas " + aux.getCount());
        while(!aux.isAfterLast()){

            User resul = new User(-1,"","");
            resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.USERS_KEY_ROWID)));
            resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.USERS_KEY_NAME)));
            resul.setPassword("");
            listaFinal.add(resul);
            aux.moveToNext();
        }

        return listaFinal;
    }

}
