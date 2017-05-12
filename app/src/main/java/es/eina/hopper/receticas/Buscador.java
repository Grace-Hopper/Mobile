package es.eina.hopper.receticas;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
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
        yo = this;
        setContentView(R.layout.activity_buscador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText mTexto = (EditText)findViewById(R.id.buscador);

        final ListView lista = (ListView) findViewById(R.id.listBusqueda);
        mItems = new ArrayList<Ingredient>();
        mListAdapter = new SearchAdapter(this, mItems,lista);
        lista.setAdapter(mListAdapter);
        Button add = (Button) findViewById(R.id.button);
        final EditText texto = (EditText) findViewById((R.id.buscador));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(texto.getText().toString())) {
                    mListAdapter.add(new Ingredient(-1, texto.getText().toString(), "", null));
                    texto.setText("");
                }
                else{
                    mTexto.setError("No se ha introducido ning\u00FAn ingrediente");
                }
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