package es.eina.hopper.receticas;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import es.eina.hopper.adapter.SearchAdapter;
import es.eina.hopper.models.Ingredient;

public class Buscador extends AppCompatActivity {
    private Button mAddItemToList;
    public Activity yo;
    private ArrayList<Ingredient> mItems;
    private SearchAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        yo = this;
        setContentView(R.layout.activity_buscador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        ImageButton fab = (ImageButton) findViewById(R.id.buscadorToolBar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout lw = (LinearLayout) findViewById(R.id.contenido);
                lw.removeAllViewsInLayout();

                /*mListAdapter = new SearchAdapter(this, mItems,lista);
            lista.setAdapter(mListAdapter);*/
                /*
                if(((LinearLayout) pantalla).getChildCount() > 0) {
                    ((LinearLayout) pantalla).removeAllViews();
                }*/
                //lista.setEmptyView(findViewById(R.id.emptyListView));
            }
        });
    }
}