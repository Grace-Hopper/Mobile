package es.eina.hopper.receticas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
import es.eina.hopper.util.UtilRecipes;
import es.eina.hopper.util.UtilService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Receta extends AppCompatActivity {
    User user;
    Activity yo;
    boolean local;
    Recipe rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //"rowId" "local" 1 true 0 false


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        yo = this;
        Bundle b = getIntent().getExtras();
        long rowId = 1; // or other values
        user = new User(-1,"","");
        if(b != null)
            rowId = b.getLong("rowId");
            local = b.getBoolean("local");
            user = (User)b.getSerializable("user");

        ImageButton ib = (ImageButton)toolbar.findViewById(R.id.editar);
        final long finalRowId = rowId;
        final long finalRowId1 = rowId;
        if(local){
            ib.setVisibility(View.VISIBLE);
        }
        else{
            ib.setVisibility(View.GONE);
        }
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(local){
                    Intent i = new Intent(yo, AddReceta.class);
                    Bundle b = new Bundle();
                    Recipe a = UtilRecipes.getRecipe(user.getName(), finalRowId,yo);
                    b.putSerializable("user", user); //Your id
                    b.putSerializable("receta",a);
                    b.putBoolean("local",true);
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
                else{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://receticas.herokuapp.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    UtilService service = retrofit.create(UtilService.class);
                    Call<Recipe> call = service.getRecipe(user.getName(), finalRowId1);
                    call.enqueue(new Callback<Recipe>() {

                        @Override
                        public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                            int statusCode = response.code();
                            System.out.println(statusCode);
                            if (statusCode == 200) {
                                //Encontrada
                                Intent i = new Intent(yo, AddReceta.class);
                                Bundle b = new Bundle();
                                Recipe a = response.body();
                                b.putSerializable("user", user); //Your id
                                b.putSerializable("receta",a);
                                b.putBoolean("local",false);
                                i.putExtras(b); //Put your id to your next Intent
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onFailure(Call<Recipe> call, Throwable t) {
                            System.out.println("Fallo to bestia");
                        }
                    });
                }
            }
        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        final Button comen = (Button) findViewById(R.id.comenzar);
        comen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(yo, Pasos.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user); //Your id
                b.putSerializable("receta",rec);
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
            }
        });
        final Button pub = (Button) findViewById(R.id.publicar);
        comen.setVisibility(View.GONE);
        pub.setVisibility(View.GONE);
        if(local) {
            pub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgressBar(true);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://receticas.herokuapp.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    UtilService service = retrofit.create(UtilService.class);
                    Call<Recipe> call = service.updateRecipe(user.getName(), rec.getId(), rec);
                    call.enqueue(new Callback<Recipe>() {
                        @Override
                        public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                            int statusCode = response.code();
                            System.out.println(statusCode);
                            if (statusCode == 200) {
                                showProgressBar(false);
                                new AlertDialog.Builder(yo).setTitle("Receta publicada").setMessage("La receta ha sido publicada con exito")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        }).show();
                            } else {
                                showProgressBar(false);
                                new AlertDialog.Builder(yo).setTitle("Error").setMessage("No se ha podido publicar la receta, compruebe su conexion a internet")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Recipe> call, Throwable t) {
                            System.out.println("Fallo to bestia");
                            showProgressBar(false);
                            new AlertDialog.Builder(yo).setTitle("Error").setMessage("No se ha podido publicar la receta, compruebe su conexion a internet")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            showProgressBar(false);
                                        }
                                    }).show();
                        }
                    });

                }
            });
        }
        else{
            pub.setText("AÑADIR A MIS RECETAS");
            pub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //UtilRecipes.insertRecipe(user.getName(),yo,rec);
                    new AlertDialog.Builder(yo).setTitle("RECETA AÑADIDA")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                }
            });
            pub.setVisibility(View.GONE);
            ib.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView imagen = (ImageView) findViewById(R.id.imagen);
        final TextView titulo = (TextView)  findViewById(R.id.titulo);
        final TextView info = (TextView)  findViewById(R.id.Info);
        UtilRecipes u = new UtilRecipes();
        if(!local) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://receticas.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UtilService service = retrofit.create(UtilService.class);
            Call<Recipe> call = service.getRecipe(user.getName(), rowId);
            call.enqueue(new Callback<Recipe>() {

                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    if (statusCode == 200) {
                        //Encontrada
                        Recipe resp = response.body();
                        rec=resp;
                        if (resp.getPicture() != null) {
                            if(resp.getPicture()!="") {
                                ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(resp.getPicture(), Base64.DEFAULT));
                                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                                theImage = Bitmap.createScaledBitmap(theImage, 500, 500, true);
                                imagen.setImageBitmap(theImage);
                            }
                            else{
                                Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.recdefault);//image is your image
                                bmp=Bitmap.createScaledBitmap(bmp, 500,500, true);
                                imagen.setImageBitmap(bmp);
                            }
                        }
                        titulo.setText(resp.getName() + "\n");
                        String ingr="";
                        for(int i=0;i<resp.getIngredients().size();i++){
                            ingr+=resp.getIngredients().get(i).getName() +", ";
                        }
                        String uten="";
                        for(int i=0;i<resp.getUtensils().size();i++){
                            uten+=resp.getUtensils().get(i).getName() +", ";
                        }
                        info.setText("Duracion: " + resp.getTotal_time() + " min" + "\n" +
                                "Nº de comensales: " + resp.getPerson() + " personas\n" +
                                "Creado: " + resp.getUser().getName() + "\n" +
                                "Ingredientes: " + ingr + "\n" +
                                "Utensilios: " + uten);

                        comen.setVisibility(View.VISIBLE);
                        pub.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    System.out.println("Fallo to bestia");
                }
            });
        }
        else {
            Recipe resp = UtilRecipes.getRecipe(user.getName(),rowId,this);
            rec=resp;
            if (resp.getPicture() != null) {
                if(resp.getPicture()!="") {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(Base64.decode(resp.getPicture(), Base64.DEFAULT));
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    theImage = Bitmap.createScaledBitmap(theImage, 500, 500, true);
                    imagen.setImageBitmap(theImage);
                }
                else{
                    Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.recdefault);//image is your image
                    bmp=Bitmap.createScaledBitmap(bmp, 500,500, true);
                    imagen.setImageBitmap(bmp);
                }
            }
            comen.setVisibility(View.VISIBLE);
            if(!user.getName().equals(rec.getUser().getName())){
                pub.setVisibility(View.GONE);
            }
            else{
                pub.setVisibility(View.VISIBLE);
            }
            titulo.setText(resp.getName() + "\n");
            String ingr="";
            for(int i=0;i<resp.getIngredients().size();i++){
                ingr+=resp.getIngredients().get(i).getName() +", ";
            }
            String uten="";
            for(int i=0;i<resp.getIngredients().size();i++){
                uten+=resp.getIngredients().get(i).getName() +", ";
            }
            info.setText("Duracion: " + resp.getTotal_time() + " min" + "\n" +
                    "Nº de comensales: " + resp.getPerson() + " personas\n" +
                    "Creado: " + resp.getUser().getName() + "\n" +
                    "Ingredientes: " + ingr + "\n" +
                    "Utensilios: " + uten);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("Esto se llama puto amo");
        super.onSaveInstanceState(outState);
        outState.putSerializable("user",user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do something useful
                System.out.println("ojo");
                super.getIntent().getExtras().putSerializable("user",user);
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }
    void showProgressBar(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            findViewById(R.id.editar).setVisibility(show ? View.GONE : View.VISIBLE);
            findViewById(R.id.titulo).setVisibility(show ? View.GONE : View.VISIBLE);
            findViewById(R.id.Info).setVisibility(show ? View.GONE : View.VISIBLE);
            findViewById(R.id.comenzar).setVisibility(show ? View.GONE : View.VISIBLE);
            findViewById(R.id.publicar).setVisibility(show ? View.GONE : View.VISIBLE);
            findViewById(R.id.imagen).setVisibility(show ? View.GONE : View.VISIBLE);
            if(!show && local){
                findViewById(R.id.editar).setVisibility(View.VISIBLE);
            }
            else{
                findViewById(R.id.editar).setVisibility(View.GONE);
            }
            final View mProgressView = findViewById(R.id.progreso);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}
