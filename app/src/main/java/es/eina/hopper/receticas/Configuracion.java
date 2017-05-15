package es.eina.hopper.receticas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import es.eina.hopper.models.User;
import es.eina.hopper.util.UtilService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configuracion extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AutoCompleteTextView mPasswordActual;
    private EditText mPasswordView;
    private EditText mPasswordRepView;
    private View mProgressView;
    private View mLoginFormView;
    private Activity yo;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        yo = this;
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
        mPasswordActual = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordRepView = (EditText) findViewById(R.id.rep_password);

        mProgressView = findViewById(R.id.login_progress);

        mLoginFormView = findViewById(R.id.regis_form);
        Button confirmar = (Button) findViewById(R.id.cambiar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegis();
            }
        });
    }

    private void attemptRegis() {

        mPasswordActual.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String pass = mPasswordActual.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRep = mPasswordRepView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordRep)) {
            mPasswordRepView.setError(getString(R.string.error_field_required));
            focusView = mPasswordRepView;
            cancel = true;
        }else if (!password.equals(passwordRep)) {
            mPasswordRepView.setError(getString(R.string.password_dont_match));
            focusView = mPasswordRepView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(pass)) {
            mPasswordActual.setError(getString(R.string.error_field_required));
            focusView = mPasswordActual;
            cancel = true;
        } else if (!isEmailValid(pass)) {
            mPasswordActual.setError(getString(R.string.error_invalid_email));
            focusView = mPasswordActual;
            cancel = true;
        } else if(!pass.equals(user.getPassword())){
            mPasswordActual.setError("La contraseña no coincide con la actual");
            focusView = mPasswordActual;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://receticas.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UtilService service = retrofit.create(UtilService.class);
            User u = new User(user.getId(),user.getName(),password);
            Call<User> call = service.actualizar(u);
            call.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    showProgress(false);
                    int statusCode = response.code();
                    User user = response.body();
                    System.out.println(statusCode);
                    if(statusCode == 200 || statusCode == 201){
                        //aceptado el login
                        new AlertDialog.Builder(yo).setTitle("Contraseña cambiada").setMessage("La contraseña se ha actualizado correctamente")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        Intent i = new Intent(yo, RecetarioLocal.class);
                                        startActivity(i);
                                    }
                                })
                                .setIcon(R.drawable.ic_info_black_24dp)
                                .show();
                    }
                    else if(statusCode == 422 || statusCode == 401){
                        //error de validacion
                        mPasswordActual.setError("El usuario ya existe");
                        mPasswordActual.requestFocus();
                    }
                    else {
                        //error de validacion
                        mPasswordActual.setError("Error en el servidor");
                        mPasswordActual.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false);
                    mPasswordActual.setError("Error de conexion");
                    mPasswordActual.requestFocus();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true; //email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
