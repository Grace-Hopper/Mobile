package es.eina.hopper.receticas;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.adapter.RecipesAdapter;
import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.Step;
import es.eina.hopper.models.User;
import es.eina.hopper.models.Utensil;
import es.eina.hopper.util.UtilService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static es.eina.hopper.receticas.R.string.user;

public class RecetarioGlobal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user;
    Activity yo;
    private ListView mList;
    TextView error;

    public ArrayList<Recipe> lista_recetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yo = this;
        setContentView(R.layout.activity_recetario_global);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        user = new User(-1,"","");
        if(b != null)
            user = (User)b.getSerializable("user");

        System.out.println(user.getName());

        ImageButton fab = (ImageButton) findViewById(R.id.buscador);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(yo, Buscador.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user); //Your id
                //b.putSerializable("receta", new Recipe(-1,"",0,0,0,new byte[]{},user,null,null,null));
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user)).setText(user.getName());
        mList = (ListView)findViewById(R.id.list);

        navigationView.getMenu().getItem(1).setChecked(true);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Recipe a = (Recipe)parent.getItemAtPosition(position);
                Intent i = new Intent(yo, Receta.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user); //Your id
                b.putLong("rowId",a.getId());
                b.putBoolean("local",false);
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                //or do your stuff
            }

        });
        
        error = (TextView)  findViewById(R.id.ERROR);
        error.setVisibility(View.VISIBLE);
        error.setText("LOADING...");
        fillData();
    }

    /**
     * Rellena el adaptador con la información necesaria
     */
    private void fillData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://receticas.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Gson a = new Gson();
        Recipe b = new Recipe(-1,"NOMBRE",1,2,3,"",user,new ArrayList<Utensil>(),new ArrayList<Ingredient>(),new ArrayList<Step>());
        System.out.println(a.toJson(b));
        UtilService service = retrofit.create(UtilService.class);
        Call<List<Recipe>> call = service.getAllRecipes(user.getName());
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                int statusCode = response.code();
                System.out.println(statusCode);
                if (statusCode == 200) {
                    error.setVisibility(View.GONE);
                    lista_recetas = new ArrayList(response.body());

                    RecipesAdapter adapter = new RecipesAdapter(RecetarioGlobal.this, lista_recetas);


                    mList.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.out.println("Fallo to bestia");
                error.setVisibility(View.VISIBLE);
                error.setText("NO SE PUDO CONECTAR CON EL SERVIDOR");
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recetario_global, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mis_recetas) {
            finish();
        } else if (id == R.id.recetas) {

        } else if (id == R.id.configuracion) {
            Intent i = new Intent(this, Configuracion.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
            finish();
        }else if (id == R.id.cambiar_usuario) {
            Intent i = new Intent(this, LoginActivity.class);
            Bundle b = new Bundle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
            finish();

        } else if (id == R.id.acerca_de) {
            Intent i = new Intent(this, AcercaDe.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
            finish();
        } else if(id == R.id.destacadas){
            Intent i = new Intent(this, Destacados.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
