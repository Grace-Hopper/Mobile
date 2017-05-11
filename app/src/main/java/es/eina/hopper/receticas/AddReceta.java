package es.eina.hopper.receticas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.eina.hopper.adapter.IngredientsAdapter;
import es.eina.hopper.adapter.RecipesAdapter;
import es.eina.hopper.adapter.UtensilAdapter;
import es.eina.hopper.models.Ingredient;
import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.Step;
import es.eina.hopper.models.User;
import es.eina.hopper.models.Utensil;
import es.eina.hopper.util.UtilRecipes;

import static android.R.attr.fragment;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;
import static es.eina.hopper.receticas.R.id.imageView;

public class AddReceta extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    public Activity yo;
    private ViewPager mViewPager;
    private  TabLayout tabLayout;
    User user;
    public static Recipe rec;
    int numPasos = 0;

    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        yo=this;
        Bundle b = getIntent().getExtras();
        user = new User(-1, "","");
        rec = new Recipe(-1,"",0,0,0,new byte[]{},user,new ArrayList<Utensil>(),new ArrayList<Ingredient>(),new ArrayList<Step>());
        if(b != null)
            user = (User)b.getSerializable("user");
            rec = (Recipe)b.getSerializable("receta");
        System.out.println(rec.getName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Añadir receta");
        ImageButton guardar = (ImageButton)toolbar.findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.requestFocus();
                DescripcionReceta a = (DescripcionReceta)((ViewPagerAdapter)mViewPager.getAdapter()).getItem(0);
                ArrayList<Step> pasitos = new ArrayList<Step>();
                ViewPagerAdapter adap = (ViewPagerAdapter)mViewPager.getAdapter();
                for(int i=1;i<adap.getCount();i++){
                    pasitos.add(((PlaceholderFragment)adap.getItem(i)).getPasos());
                }
                rec.setSteps(pasitos);
                rec.setPerson(a.getPerson());
                rec.setTotal_time(a.getTime());
                rec.setName(a.getNombre());
                rec.setUtensils(a.getUtensilios());
                rec.setIngredients(a.getIngredientes());
                System.out.println("RECETA: " + rec.getName());
                System.out.println("COMENSALES: " + rec.getPerson());
                System.out.println("TIEMPO: " + rec.getTotal_time());
                System.out.println("INGREDIENTES:");
                for(int i=0;i<rec.getIngredients().size();i++){
                    System.out.println(rec.getIngredients().get(i).getName() + ": " + rec.getIngredients().get(i).getQuantity());
                }
                System.out.println("UTENSILIOS:");
                for(int i=0;i<rec.getUtensils().size();i++){
                    System.out.println(rec.getUtensils().get(i).getName());
                }
                System.out.println("PASOS:");
                for(int i=0;i<rec.getSteps().size();i++){
                    System.out.println(rec.getSteps().get(i).getInformation());
                    System.out.println(rec.getSteps().get(i).getTime());
                }

                //COMPROBAR SI HAY CAMPOS VACIOS
                int numeroErrores = 0;
                String error = new String();
                if("".equals(rec.getName())){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else{
                        numeroErrores++;
                        error = error + "La receta debe tener un nombre.\n";
                    }

                }
                if(rec.getTotal_time() <= 0){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else {
                        numeroErrores++;
                        error = error + "El tiempo de la receta debe ser mayor que cero.\n";
                    }

                }
                if(rec.getPerson() <= 0){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else {
                        numeroErrores++;
                        error = error + "El numero de comensales ha de ser mayor que cero.\n";
                    }
                }
                if(rec.getIngredients().size() <= 0){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else {
                        numeroErrores++;
                        error = error + "La receta debe tener al menos un ingrediente.\n";
                    }
                }
                if(rec.getUtensils().size() <= 0){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else {
                        numeroErrores++;
                        error = error + "La receta debe tener al menos un utensilio.\n";
                    }
                }
                if(rec.getSteps().size() <= 0){
                    if(numeroErrores >= 6){
                        error = error + "Existen mas campos por rellenar.\n";
                    }else {
                        numeroErrores++;
                        error = error + "La receta debe tener al menos un paso.\n";
                    }
                }
                List<Ingredient> listaIngredientes = rec.getIngredients();
                for(int i = 0; i < listaIngredientes.size(); i++) {
                    if("".equals(listaIngredientes.get(i).getQuantity()) && numeroErrores < 6){
                        numeroErrores++;
                        error = error + "La cantidad de " + listaIngredientes.get(i).getName() +" debe ser mayor que cero.\n";
                    }
                    if("".equals(listaIngredientes.get(i).getName()) && !"".equals(listaIngredientes.get(i).getQuantity()) && numeroErrores < 6){
                        numeroErrores++;
                        error = error + "Se ha introducido una cantidad sin su correspondiente ingrediente.\n";
                    }
                }
                List<Step> listaPasos = rec.getSteps();
                for(int i = 0; i < listaPasos.size(); i++) {
                    if("".equals(listaPasos.get(i).getInformation()) && numeroErrores < 6){
                        numeroErrores++;
                        error = error + "El paso " + (i+1) + " debe tener informacion.\n";
                    }
                    if(listaPasos.get(i).getTime() <= 0 && numeroErrores < 6){
                        numeroErrores++;
                        error = error + "El tiempo del paso " + (i+1) + " debe ser mayor que cero.\n";

                    }
                }
                
                if(numeroErrores > 0){
                    new AlertDialog.Builder(yo).setTitle("Error").setMessage(error)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(R.drawable.ic_report_problem_black_24dp)
                            .show();
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager, rec.getSteps(),rec);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = mViewPager.getCurrentItem();
                ViewPagerAdapter adapter = (ViewPagerAdapter)mViewPager.getAdapter();
                rec.setIngredients(((DescripcionReceta)adapter.getItem(0)).getIngredientes());
                rec.setUtensils(((DescripcionReceta)adapter.getItem(0)).getUtensilios());
                Step a = new Step(0,rec,0,"", new ArrayList<Utensil>(),new ArrayList<Ingredient>());
                rec.getSteps().add(current,a);
                PlaceholderFragment b = new PlaceholderFragment();
                b.setArguments(a,numPasos);
                numPasos++;
                adapter.addFrag(b, "PASO " + (numPasos));
                mViewPager.setAdapter(adapter);
                mViewPager.setCurrentItem(current+1,true);
            }
        });
        final FloatingActionButton borrar = (FloatingActionButton) findViewById(R.id.borrar);
        borrar.setVisibility(View.GONE);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = mViewPager.getCurrentItem();
                rec.getSteps().remove(current-1);
                ViewPagerAdapter adapter = (ViewPagerAdapter)mViewPager.getAdapter();
                numPasos--;
                getSupportFragmentManager().beginTransaction().remove(adapter.getItem(current)).commitNow();
                adapter.remove(current);
                adapter.actualizarNom();
                mViewPager.setAdapter(adapter);
                mViewPager.setCurrentItem(current-1,true);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int numTab = tab.getPosition();
                mViewPager.setCurrentItem(numTab);
                ViewPagerAdapter adapter = (ViewPagerAdapter)mViewPager.getAdapter();
                for(int i=0;i<adapter.getCount();i++){
                    try{
                        PlaceholderFragment aux = (PlaceholderFragment)adapter.getItem(numTab);
                        aux.update();
                    }
                    catch (ClassCastException a){

                    }
                }
                if(numTab==numPasos && numTab>=1){
                    borrar.setVisibility(View.VISIBLE);
                }
                else{
                    borrar.setVisibility(View.GONE);
                }
                if(numTab==numPasos){
                    fab.setVisibility(View.VISIBLE);
                }
                else{
                    fab.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do something useful
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }
    private void setupViewPager(ViewPager viewPager, List<Step> lp, Recipe rec) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DescripcionReceta a = new DescripcionReceta();
        a.setArguments(rec);
        adapter.addFrag(a, "INFORMACION");
        for(int i=0;i<lp.size();i++) {
            PlaceholderFragment b = new PlaceholderFragment();
            b.setArguments(lp.get(i),i);
            adapter.addFrag(b, "PASO " + (i+1));
        }
        viewPager.removeAllViews();
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("JODER ENTRA AQUI OSTIASSSSSs");
        System.out.println(requestCode + " -- " + resultCode + " -- " + data==null);
        if (null != data) {
            System.out.println("JDEBERIA FUNCIONAR");
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ((DescripcionReceta)((ViewPagerAdapter)mViewPager.getAdapter()).getItem(0)).setImagen(picturePath);
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<View> mListView = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            for(int i=0;i<mFragmentTitleList.size();i++){
                System.out.println(mFragmentTitleList.get(i));
            }
            if (mFragmentList.contains((Fragment) object)) {
                return mFragmentList.indexOf((Fragment) object);
            } else {
                return POSITION_NONE;
            }
        }

        public void actualizarNom(){
            for(int i=1;i<mFragmentTitleList.size();i++){
                mFragmentTitleList.set(i,"PASO " + i);
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mListView.add(fragment.getView());
            notifyDataSetChanged();
        }
        public void remove(int position){
            mFragmentList.remove(position);
            mFragmentTitleList.remove(position);
            mListView.remove(position);
            notifyDataSetChanged();
        }
        public void removeAll(){
            while(mFragmentList.size()!=0){
                mFragmentList.remove(0);
                mFragmentTitleList.remove(0);
                notifyDataSetChanged();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        EditText tiempo;
        EditText desc;
        MultiSelectionSpinner utensilios;
        MultiSelectionSpinner ingredientes;
        Step pd;
        int yo;
        public PlaceholderFragment() {
        }
        public void setArguments(Step pd, int i) {
            this.pd = pd;
            yo=i;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_receta, container, false);
            //TextView aux = (TextView) rootView.findViewById(R.id.section_label)

            //System.out.println("VOY A MOSTRAR " + pd.tiempo);
            //aux.setText(pd.contenido + "\n TIEMPO = " + pd.tiempo + " - YO:" + yo);
            //EditText tiempo = (EditText) rootView.findViewById(R.id.tiempo);
            // tiempo.setText(pd.tiempo);
            tiempo = (EditText) rootView.findViewById(R.id.tiempoReceta);
            desc = (EditText) rootView.findViewById(R.id.descripcionPasos);
            desc.setText(pd.getInformation());
            if(pd.getTime()!=0) {
                tiempo.setText(Objects.toString(pd.getTime(), null));
            }
            utensilios = (MultiSelectionSpinner) rootView.findViewById(R.id.spinnerUtensilios);
            final List<Utensil> ute = pd.getRecipe().getUtensils();
            utensilios.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    pd.setIngredients(new ArrayList<Ingredient>());
                    System.out.println("INGREDIENTES: " + indices.size());
                    for(int i=0;i<indices.size();i++){
                        pd.getIngredients().add(pd.getRecipe().getIngredients().get(indices.get(i)));
                    }
                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });
            List<String> luten = new ArrayList<>();
            for(int i=0;i< ute.size();i++){
                luten.add(ute.get(i).getName());
            }
            if(luten.size()>0) {
                utensilios.setItems(luten);
            }
            ingredientes = (MultiSelectionSpinner) rootView.findViewById(R.id.spinnerIngredientes);
            final List<Ingredient> ingr = pd.getRecipe().getIngredients();
            ingredientes.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    pd.setUtensils(new ArrayList<Utensil>());
                    for(int i=0;i<indices.size();i++){
                        pd.getUtensils().add(pd.getRecipe().getUtensils().get(indices.get(i)));
                    }
                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });
            List<String> gr = new ArrayList<>();
            for(int i=0;i< ingr.size();i++){
                gr.add(ingr.get(i).getName());
            }
            if(gr.size()>0) {
                ingredientes.setItems(gr);
            }
            //desc.setText("COJON DE PUTAS " + pd.tiempo);
            return rootView;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void update(){
            pd = getPasos();
            desc.setText(pd.getInformation());
            if(pd.getTime()!=0) {
                tiempo.setText(Objects.toString(pd.getTime(), null));
            }
            final List<Utensil> ute = pd.getRecipe().getUtensils();
            utensilios.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    pd.setUtensils(new ArrayList<Utensil>());
                    for(int i=0;i<indices.size();i++){
                        pd.getUtensils().add(pd.getRecipe().getUtensils().get(indices.get(i)));
                    }
                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });
            List<String> luten = new ArrayList<>();
            for(int i=0;i< ute.size();i++){
                luten.add(ute.get(i).getName());
            }
            if(luten.size()>0) {
                utensilios.setItems(luten);
            }
            final List<Ingredient> ingr = pd.getRecipe().getIngredients();
            ingredientes.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    pd.setIngredients(new ArrayList<Ingredient>());
                    System.out.println("INGREDIENTES: " + indices.size());
                    for(int i=0;i<indices.size();i++){
                        pd.getIngredients().add(pd.getRecipe().getIngredients().get(indices.get(i)));
                    }
                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });
            List<String> gr = new ArrayList<>();
            for(int i=0;i< ingr.size();i++){
                gr.add(ingr.get(i).getName());
            }
            if(gr.size()>0) {
                ingredientes.setItems(gr);
            }
        }
        public Step getPasos(){
            if(!tiempo.getText().toString().equals("")){
                pd.setTime(Integer.parseInt(tiempo.getText().toString()));
            }
            else{
                pd.setTime(0);
            }
            if(!desc.getText().toString().equals("")){
                pd.setInformation(desc.getText().toString());
            }
            else{
                pd.setInformation("");
            }
            return pd;
        }
    }
    public static class DescripcionReceta extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Recipe receta;
        EditText nComensales;
        EditText tiempo;
        AutoCompleteTextView nombreReceta;
        ListView mListUten;
        ListView mListIngr;
        ImageButton imagen;
        public DescripcionReceta() {

        }
        public void setArguments(Recipe a) {
            receta=a;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_descripcion_receta, container, false);
            imagen = (ImageButton)rootView.findViewById(R.id.imagen_rec);
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });
            nombreReceta = (AutoCompleteTextView) rootView.findViewById(R.id.titulo_receta);
            MultiAutoCompleteTextView Descripcion = (MultiAutoCompleteTextView) rootView.findViewById(R.id.descripcion);
            nComensales = (EditText) rootView.findViewById(R.id.comensales);
            tiempo = (EditText) rootView.findViewById(R.id.tiempo);
            nombreReceta.setText(receta.getName());
            Descripcion.setText("");

            mListUten = (ListView)rootView.findViewById(R.id.listUtensilios);
            final UtensilAdapter adapter = new UtensilAdapter(getContext(), (ArrayList)receta.getUtensils(),mListUten,getActivity());
            mListUten.setAdapter(adapter);
            mListUten.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    receta.setUtensils(adapter.getUtensilios());
                }
            });
            mListIngr = (ListView)rootView.findViewById(R.id.listIngredientes);
            final IngredientsAdapter adapterIngr = new IngredientsAdapter(getContext(), (ArrayList)receta.getIngredients(),mListIngr,getActivity());
            mListIngr.setAdapter(adapterIngr);
            mListIngr.setOnFocusChangeListener(new View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    receta.setIngredients(adapterIngr.getIngredients());
                }
            });
            adapterIngr.setCogerDatos(false);
            setListViewHeightBasedOnChildren(mListIngr);
            adapterIngr.setCogerDatos(true);
            adapter.setCogerDatos(false);
            setListViewHeightBasedOnChildren(mListUten);
            adapter.setCogerDatos(true);
            if(receta.getPerson()!=0) {
                nComensales.setText(Objects.toString(receta.getPerson()));
            }
            if(receta.getTotal_time()!=0) {
                tiempo.setText(Objects.toString(receta.getTotal_time()));
            }
            return rootView;
        }

        public long getTime(){
            if(!tiempo.getText().toString().equals("")){
                return Integer.parseInt(tiempo.getText().toString());
            }
            else{
                return 0;
            }
        }
        public long getPerson(){
            if(!nComensales.getText().toString().equals("")){
                return Integer.parseInt(nComensales.getText().toString());
            }
            else{
                return 0;
            }
        }
        public String getNombre(){
            return nombreReceta.getText().toString();
        }

        public List<Utensil> getUtensilios(){
            ((UtensilAdapter)mListUten.getAdapter()).notifyDataSetChanged();
            return ((UtensilAdapter)mListUten.getAdapter()).getUtensilios();
        }
        public List<Ingredient> getIngredientes(){
            ((IngredientsAdapter)mListIngr.getAdapter()).notifyDataSetChanged();
            return ((IngredientsAdapter)mListIngr.getAdapter()).getIngredients();
        }
        public void setImagen(String path){
            System.out.println("VAYA IMAGEN GUAPA");
            imagen.setImageBitmap(getScaledBitmap(path, 800, 800));
        }
        private Bitmap getScaledBitmap(String picturePath, int width, int height) {
            BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
            sizeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, sizeOptions);

            int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

            sizeOptions.inJustDecodeBounds = false;
            sizeOptions.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFile(picturePath, sizeOptions);
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                // Calculate ratios of height and width to requested height and
                // width
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                // Choose the smallest ratio as inSampleSize value, this will
                // guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            return inSampleSize;
        }
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
                return;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }
}