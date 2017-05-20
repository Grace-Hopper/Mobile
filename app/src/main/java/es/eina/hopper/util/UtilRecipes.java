package es.eina.hopper.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.Step;
import es.eina.hopper.models.User;
import es.eina.hopper.models.Utensil;
import es.eina.hopper.receticas.LoginActivity;
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
        if(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE))!=null) {
            resul.setPicture(Base64.encodeToString(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)), Base64.DEFAULT));
        }
        else{
            resul.setPicture("");
        }
        resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));


        resul.setUser(new User(-1, aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_CREATOR)),""));

        //Recupero ingredientes de la receta
        aux = mDb.fetchRecipeIngredients(rowId);

        List<Ingredient> ingredients = new ArrayList();

        if (aux.moveToFirst()) {
            while (!aux.isAfterLast()) {

                Ingredient ing = new Ingredient();
                //resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                ing.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.INGREDIENTS_KEY_NAME)));
                ing.setQuantity(aux.getString(aux.getColumnIndex(RecipesDbAdapter.USE_KEY_QUANTITY)));


                ingredients.add(ing);
                aux.moveToNext();
            }
        }

        resul.setIngredients(ingredients);

        //Recupero utensilios de la receta
        aux = mDb.fetchRecipeUtensils(rowId);

        List<Utensil> utensils = new ArrayList();
        if (aux.moveToFirst()){
            while(!aux.isAfterLast()){

                Utensil ut = new Utensil();
                System.out.println("Utensilname :" + aux.getString(aux.getColumnIndex(RecipesDbAdapter.UTENSILS_KEY_NAME)));
                //ut.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                ut.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.UTENSILS_KEY_NAME)));


                utensils.add(ut);
                aux.moveToNext();
            }
        }

        resul.setUtensils(utensils);

        //Recupero pasos de la receta
        aux = mDb.fetchSteps(rowId);

        List<Step> steps = new ArrayList();

        Cursor aux2;

        if(aux.moveToFirst()) {
            while (!aux.isAfterLast()) {

                Step s = new Step();
                //ut.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
                Long stepNumber = aux.getLong(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_STEP));
                s.setStep(stepNumber);
                s.setTimer(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_TIME)));
                s.setInformation(aux.getString(aux.getColumnIndex(RecipesDbAdapter.STEPS_KEY_INFORMATION)));

                //Recupero ingredientes del paso
                aux2 = mDb.fetchStepIngredients(rowId, stepNumber);
                ingredients = new ArrayList();

                if(aux2.moveToFirst()) {
                    while (!aux2.isAfterLast()) {

                        Ingredient ing = new Ingredient();
                        ing.setName(aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.INGREDIENTS_KEY_NAME)));
                        ing.setQuantity(aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.USE_KEY_QUANTITY)));


                        ingredients.add(ing);
                        aux2.moveToNext();
                    }
                }

                s.setIngredients(ingredients);


                //Recupero utensilios del paso
                aux2 = mDb.fetchStepUtensils(rowId, stepNumber);

                utensils = new ArrayList();

                if(aux2.moveToFirst()) {
                    while (!aux2.isAfterLast()) {

                        Utensil ut = new Utensil();
                        ut.setName(aux2.getString(aux2.getColumnIndex(RecipesDbAdapter.UTENSILS_KEY_NAME)));


                        utensils.add(ut);
                        aux2.moveToNext();
                    }
                }

                s.setUtensils(utensils);


                steps.add(s);
                aux.moveToNext();
            }
        }

        resul.setSteps(steps);


        return resul;
    }



    public static List<Recipe> getAll(String user, Context ctx){
        List<Recipe> listaFinal = new ArrayList<Recipe>();
        //en local
        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();


        Cursor aux = mDb.fetchAllRecipes(user);

        aux.moveToFirst();
        System.out.println("Usuar2 " + user);
        System.out.println("Numero de filas " + aux.getCount());
        while(!aux.isAfterLast()){

            Recipe resul = new Recipe();
            resul.setId(aux.getInt(aux.getColumnIndexOrThrow(RecipesDbAdapter.RECIPES_KEY_ROWID)));
            resul.setName(aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_NAME)));
            resul.setPerson(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_PERSON)));
            if(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE))!=null) {
                resul.setPicture(Base64.encodeToString(aux.getBlob(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_IMAGE)), Base64.DEFAULT));
            }
            else{
                resul.setPicture("");
            }
            resul.setTotal_time(aux.getLong(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_TOTAL_TIME)));

            resul.setUser(new User(-1,aux.getString(aux.getColumnIndex(RecipesDbAdapter.RECIPES_KEY_CREATOR)),""));

            listaFinal.add(resul);
            aux.moveToNext();
        }


        return listaFinal;
    }

    public static long insertRecipe(String user, Context ctx, Recipe recipe){

        RecipesDbAdapter mDb = new RecipesDbAdapter(ctx);
        mDb.open();

        // Recupero id del propietario
        System.out.println("Usuarioooo " + user);
        Cursor aux = mDb.fetchUserId(user);
        long userId = aux.getLong(aux.getColumnIndex(RecipesDbAdapter.USERS_KEY_ROWID));

        // Inserto la receta
        long rowId = mDb.insertRecipe(recipe.getName(), recipe.getTotal_time(), recipe.getPerson(), recipe.getPicture(), userId, recipe.getUser().getName());

        //Inserto ingredientes
        List<Ingredient> ingredients = recipe.getIngredients();
        Iterator<Ingredient> ii = ingredients.iterator();

        while(ii.hasNext()){
            Ingredient ing = ii.next();
            System.out.println("Ingrediente " + ing.getName());
            mDb.insertIngredient(ing.getName(),rowId, ing.getQuantity());

        }

        //Inserto utensilios
        List<Utensil> utensils = recipe.getUtensils();
        Iterator<Utensil> iu = utensils.iterator();

        while(iu.hasNext()){
            Utensil ut = iu.next();
            mDb.insertUtensil(ut.getName(),rowId);
        }

        //Inserto pasos
        List<Step> steps = recipe.getSteps();
        Iterator<Step> is = steps.iterator();

        int numPaso = 1;
        while(is.hasNext()){
            System.out.println("Joder, al menos hay uno");

            Step st = is.next();
            long stepRowId = mDb.insertStep(numPaso, st.getTimer(), st.getInformation(), rowId);
            System.out.println("StepRowId" + stepRowId);

            //Inserto ingredientes de cada paso

            ingredients = st.getIngredients();
            ii = ingredients.iterator();
            while(ii.hasNext()){
                Ingredient ing = ii.next();
                System.out.println("Ingredienti" + ing.getName());
                mDb.insertStepIngredient(ing.getName(),rowId, "-1", stepRowId);
            }

            //Inserto utensilios de cada paso
            utensils = recipe.getUtensils();
            iu = utensils.iterator();

            while(iu.hasNext()){
                Utensil ut = iu.next();
                mDb.insertStepUtensil(ut.getName(),rowId, stepRowId);
            }

            numPaso++;

        }

        return rowId;


    }

}
