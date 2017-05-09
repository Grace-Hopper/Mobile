package es.eina.hopper.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
import es.eina.hopper.receticas.R;
import es.eina.hopper.receticas.Receta;
import es.eina.hopper.receticas.RecipesDbAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fernando on 03/04/2017.
 */

public class UtilRecipes {
    public static Recipe getRecipe(String user, long rowId, Context ctx) {
        //en local
        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();
        Cursor aux = mDb.fetchRecipe(rowId);
        Recipe resul = new Recipe();
        resul.setId(aux.getInt(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_ROWID)));
        resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
        resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
        resul.setPicture(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)));
        resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));

        Cursor aux2 = mDb.fetchUser(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_USER)));

        resul.setUser(new User(-1,aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.USERS_KEY_NAME)),""));

        return resul;
    }

    public static List<Recipe> getAll(String user, Context ctx){
        List<Recipe> listaFinal = new ArrayList<Recipe>();
        //en local
        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();


        Cursor aux = mDb.fetchAllRecipes();

        aux.moveToFirst();

        System.out.println("Numero de filas " + aux.getCount());
        while(!aux.isAfterLast()){

            Recipe resul = new Recipe();
            resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
            resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
            resul.setPicture(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)));
            resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));
            Cursor aux2 = mDb.fetchUser(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_USER));

            resul.setUser(new User(-1,aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.USERS_KEY_NAME)),""));
            listaFinal.add(resul);
            aux.moveToNext();
        }

        return listaFinal;
    }
}
