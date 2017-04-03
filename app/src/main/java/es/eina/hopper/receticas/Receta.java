package es.eina.hopper.receticas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //"rowId" "local" 1 true 0 false


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        long rowId = -1; // or other values
        boolean local = false;
        User user = new User("","");
        if(b != null)
            rowId = b.getLong("rowId");
            local = b.getBoolean("local");
            user = (User)b.getSerializable("user");


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView imagen = (ImageView) findViewById(R.id.imagen);
        final TextView titulo = (TextView)  findViewById(R.id.titulo);
        System.out.println("HASTA AQUI GOOD");
        UtilRecipes u = new UtilRecipes();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://receticas.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UtilService service = retrofit.create(UtilService.class);
        Recipe re = new Recipe();
        System.out.println(user.getName());
        System.out.println(rowId);
        Call<Recipe> call =  service.getRecipe(user.getName(), rowId);
        call.enqueue(new Callback<Recipe>() {

            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                int statusCode = response.code();
                System.out.println(statusCode);
                if(statusCode == 200){
                    //Encontrada
                    Recipe resp = response.body();
                    if(resp.getPicture()!=null) {
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(resp.getPicture());
                        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                        imagen.setImageBitmap(theImage);
                    }
                    titulo.setText(resp.getName() + "\n" +
                            "Duracion: " + resp.getTotal_time() + "\n" +
                            "Creado: " + resp.getUser().getName() + "\n" +
                            "Nº de comensales: " + resp.getPerson() + "\n");

                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
            }
        });
    }
}
