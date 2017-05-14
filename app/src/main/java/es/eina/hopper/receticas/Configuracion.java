package es.eina.hopper.receticas;

import android.content.Intent;
import android.os.Build;
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
import android.view.WindowManager;
import android.widget.TextView;

import es.eina.hopper.models.User;

public class Configuracion extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_configuracion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        user = new User(-1,"","");
        if(b != null)
            user = (User)b.getSerializable("user");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user)).setText(user.getName());
        navigationView.getMenu().getItem(3).setChecked(true);
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
        getMenuInflater().inflate(R.menu.configuracion, menu);
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
            Intent i = new Intent(this, RecetarioGlobal.class);
            Bundle b = new Bundle();
            b.putSerializable("user", user); //Your id
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        } else if (id == R.id.configuracion) {

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
