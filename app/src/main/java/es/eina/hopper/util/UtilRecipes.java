package es.eina.hopper.util;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
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
    public static Recipe getRecipe(String user, long rowId, boolean local, Context ctx){
        final Recipe[] r = new Recipe[1];
        if(!local){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://receticas.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UtilService service = retrofit.create(UtilService.class);
            Recipe re = new Recipe();
            Call<Recipe> call = service.getRecipe(user, rowId);
            try {
                r[0] = call.execute().body();
            } catch (IOException e) {
                r[0]=null;
            }
        }
        else{
            //en local
            RecipesDbAdapter rdbp = new RecipesDbAdapter(ctx);
            Cursor aux = rdbp.fetchRecipe(rowId);
            Recipe resul = new Recipe();
            resul.setId(aux.getInt(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
            resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
            resul.setPicture(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)));
            resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));
            resul.setUser(new User(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_USER)),""));
            r[0]=resul;
        }
        return r[0];
    }

    List<Recipe> getAll(String user, boolean local, Context ctx){
        List<Recipe> listaFinal = new ArrayList<Recipe>();
        if(!local){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://receticas.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UtilService service = retrofit.create(UtilService.class);
            Recipe re = new Recipe();
            Call<List<Recipe>> call = service.getAllRecipes(user);
            try {
                listaFinal = call.execute().body();
            } catch (IOException e) {
                listaFinal=null;
            }
        }
        else{
            //en local
            RecipesDbAdapter rdbp = new RecipesDbAdapter(ctx);
            Cursor aux = rdbp.fetchAllRecipes();
            while(aux.isAfterLast()){
                Recipe resul = new Recipe();
                resul.setId(aux.getInt(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
                resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
                resul.setPicture(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)));
                resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));
                resul.setUser(new User(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_USER)),""));
                listaFinal.add(resul);
                aux.moveToNext();
            }
        }

        return listaFinal;
    }
}
