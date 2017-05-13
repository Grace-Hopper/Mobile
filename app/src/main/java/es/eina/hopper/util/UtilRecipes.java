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

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.Step;
import es.eina.hopper.models.User;
import es.eina.hopper.models.Utensil;
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

        //Recupero receta
        Cursor aux = mDb.fetchRecipe(rowId);
        Recipe resul = new Recipe();
        resul.setId(rowId);
        resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
        resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
        resul.setPicture(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)));
        resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));

        Cursor aux2 = mDb.fetchUser(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_USER)));

        resul.setUser(new User(-1,aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.USERS_KEY_NAME)),""));

        //Recupero ingredientes de la receta
        aux = mDb.fetchRecipeIngredients(rowId);
        aux.moveToFirst();

        List<Ingredient> ingredients = new ArrayList();

        while(!aux.isAfterLast()){

            Ingredient ing = new Ingredient();
            //resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            ing.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.INGREDIENTS_KEY_NAME)));


            ingredients.add(ing);
            aux.moveToNext();
        }

        resul.setIngredients(ingredients);

        //Recupero utensilios de la receta
        aux = mDb.fetchRecipeUtensils(rowId);
        aux.moveToFirst();

        List<Utensil> utensils = new ArrayList();

        while(!aux.isAfterLast()){

            Utensil ut = new Utensil();
            //ut.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            ut.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.UTENSILS_KEY_NAME)));


            utensils.add(ut);
            aux.moveToNext();
        }

        resul.setUtensils(utensils);

        //Recupero pasos de la receta
        aux = mDb.fetchSteps(rowId);
        aux.moveToFirst();

        List<Step> steps = new ArrayList();

        while(!aux.isAfterLast()){

            Step s = new Step();
            //ut.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            Long stepNumber = aux.getLong(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_STEP));
            s.setStep(stepNumber);
            s.setTimer(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_TIME)));
            s.setInformation(aux.getString(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_INFORMATION)));

            //Recupero ingredientes del paso
            aux2 = mDb.fetchStepIngredients(rowId, stepNumber);
            aux2.moveToFirst();

            ingredients = new ArrayList();

            while(!aux2.isAfterLast()){

                Ingredient ing = new Ingredient();
                //resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                ing.setName(aux2.getString(aux.getColumnIndex(RecipesDbAdapter.INGREDIENTS_KEY_NAME)));


                ingredients.add(ing);
                aux.moveToNext();
            }

            s.setIngredients(ingredients);


            //Recupero utensilios del paso
            aux2 = mDb.fetchStepUtensils(rowId, stepNumber);
            aux2.moveToFirst();

            utensils = new ArrayList();

            while(!aux.isAfterLast()){

                Utensil ut = new Utensil();
                //ut.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                ut.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.UTENSILS_KEY_NAME)));


                utensils.add(ut);
                aux.moveToNext();
            }

            s.setUtensils(utensils);


            steps.add(s);
            aux.moveToNext();
        }

        resul.setSteps(steps);


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
