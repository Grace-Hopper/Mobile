package es.eina.hopper.receticas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.adapter.RecipesAdapter;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
import es.eina.hopper.util.UtilRecipes;
import es.eina.hopper.util.UtilService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecetarioLocal
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user;
    private ListView mList;
    public ArrayList<Recipe> lista_recetas;
    Activity yo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetario_local);

        Bundle b = getIntent().getExtras();
        user = new User("","");
        if(b != null)
            user = (User)b.getSerializable("user");

        yo=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user)).setText(user.getName());
        mList = (ListView)findViewById(R.id.list);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Recipe a = (Recipe)parent.getItemAtPosition(position);
                Intent i = new Intent(yo, Receta.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user); //Your id
                b.putLong("rowId",a.getId());
                b.putBoolean("local",true);
                i.putExtras(b); //Put your id to your next Intent
                startActivity(i);
                //or do your stuff
            }

        });
        fillData();

    }

    /**
     * Rellena el adaptador con la información necesaria
     */
    private void fillData() {

        lista_recetas = new ArrayList(UtilRecipes.getAll(user.getName(), this));

        RecipesAdapter adapter = new RecipesAdapter(this, lista_recetas);


        mList.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mis_recetas) {
            Intent i = new Intent(this, RecetarioLocal.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        } else if (id == R.id.recetas) {
            Intent i = new Intent(this, RecetarioGlobal.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        } else if (id == R.id.configuracion) {
            Intent i = new Intent(this, Configuracion.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        }else if (id == R.id.cambiar_usuario) {
            Intent i = new Intent(this, LoginActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        } else if (id == R.id.acerca_de) {
            Intent i = new Intent(this, AcercaDe.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
