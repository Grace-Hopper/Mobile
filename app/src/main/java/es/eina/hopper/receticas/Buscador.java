package es.eina.hopper.receticas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.adapter.RecipesAdapter;
import es.eina.hopper.adapter.SearchAdapter;
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

public class Buscador extends AppCompatActivity {
    private Button mAddItemToList;
    public Activity yo;
    User user;
    private ArrayList<Ingredient> mItems;
    private SearchAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        yo = this;
        setContentView(R.layout.activity_buscador);

        Bundle b = getIntent().getExtras();
        user = new User(-1,"","");
        if(b != null)
            user = (User)b.getSerializable("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText mTexto = (EditText)findViewById(R.id.buscador);
        final ListView lista = (ListView) findViewById(R.id.listBusqueda);
        mItems = new ArrayList<Ingredient>();
        mListAdapter = new SearchAdapter(this, mItems,lista, yo);

        lista.setAdapter(mListAdapter);
        //CUANDO PULSAN ENTER AÑADIR A LA LISTA
        mTexto.setFocusableInTouchMode(true);
        mTexto.requestFocus();
        mTexto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(mListAdapter.contiene(mTexto.getText().toString())){
                        mTexto.setError("Ya ha introducido "+ mTexto.getText().toString().substring(0, 1).toUpperCase() +mTexto.getText().toString().substring( 1).toLowerCase() + ".");
                    }
                    else if(!"".equals(mTexto.getText().toString())) {
                        mListAdapter.addItem(new Ingredient(-1, mTexto.getText().toString().substring(0, 1).toUpperCase() + mTexto.getText().toString().substring( 1).toLowerCase(), ""));
                        mTexto.setText("");
                    }
                    else{
                        mTexto.setError("No se ha introducido ning\u00FAn ingrediente");
                    }
                    return true;
                }
                return false;
            }
        });
        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListAdapter.contiene(mTexto.getText().toString())){
                    mTexto.setError("Ya ha introducido "+ mTexto.getText().toString().substring(0, 1).toUpperCase() +mTexto.getText().toString().substring( 1).toLowerCase() + ".");
                }
                else if(!"".equals(mTexto.getText().toString())) {
                    mListAdapter.addItem(new Ingredient(-1, mTexto.getText().toString().substring(0, 1).toUpperCase() + mTexto.getText().toString().substring( 1).toLowerCase(), ""));
                    mTexto.setText("");
                }
                else{
                    mTexto.setError("No se ha introducido ning\u00FAn ingrediente");
                }
                //mListAdapter.mostrar();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBuscar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListAdapter.getIngredientes().size() == 0){
                    new AlertDialog.Builder(yo).setTitle("Error").setMessage("Debe introducir al menos un ingrediente para comenzar la busqueda.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(R.drawable.ic_report_problem_black_24dp)
                            .show();
                }
                else{
                    showProgressBar(true);
                    lista.setAdapter(new RecipesAdapter(yo,new ArrayList<Recipe>()));
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://receticas.herokuapp.com/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    UtilService service = retrofit.create(UtilService.class);
                    Call<List<Recipe>> call = service.busqueda(user.getName(), mItems);
                    call.enqueue(new Callback<List<Recipe>>() {

                        @Override
                        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                            int statusCode = response.code();
                            System.out.println(statusCode);
                            if (statusCode == 200) {
                                //RECIBES LA PETICION WENA
                                //lista_recetas = new ArrayList(response.body());
                                showProgressBar(false);
                                lista.setAdapter(new RecipesAdapter(yo,new ArrayList<Recipe>(response.body())));
                                lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

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
                            }
                            else{
                                //MENSAJE DE ERRROR
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Recipe>> call, Throwable t) {
                            //MENSAJE DE ERRROR
                        }
                    });

                /*mListAdapter = new SearchAdapter(this, mItems,lista);
            lista.setAdapter(mListAdapter);*/
                /*
                if(((LinearLayout) pantalla).getChildCount() > 0) {
                    ((LinearLayout) pantalla).removeAllViews();
                }*/
                    //lista.setEmptyView(findViewById(R.id.emptyListView));
                }
            }
        });
    }
    void showProgressBar(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            findViewById(R.id.layourIngr).setVisibility(View.GONE);
            findViewById(R.id.fabBuscar).setVisibility(View.GONE);
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
}