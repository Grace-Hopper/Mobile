package es.eina.hopper.receticas;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;

import static android.R.attr.fragment;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;

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
    public static class PasosDetalle{
        public String contenido;
        public int tiempo;
        public PasosDetalle(String c, int t){
            tiempo=t;
            contenido=c;
        }
    }

    public Activity yo;
    private ViewPager mViewPager;
    private  TabLayout tabLayout;
    User user;
    Recipe rec;
    ArrayList<PasosDetalle> lp;
    int numPasos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        yo=this;
        Bundle b = getIntent().getExtras();
        user = new User("","");
        rec = new Recipe(-1,"",0,0,new byte[]{},user);
        if(b != null)
            user = (User)b.getSerializable("user");
            rec = (Recipe)b.getSerializable("receta");
        System.out.println(rec.getName());
        lp=new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Añadir receta");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager,lp,rec);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = mViewPager.getCurrentItem();
                PasosDetalle a = new PasosDetalle("EL TROLEITO ES REAL", numPasos);
                lp.add(current, a);
                PlaceholderFragment b = new PlaceholderFragment();
                b.setArguments(a,numPasos);
                ViewPagerAdapter adapter = (ViewPagerAdapter)mViewPager.getAdapter();
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
                lp.remove(current-1);
                ViewPagerAdapter adapter = (ViewPagerAdapter)mViewPager.getAdapter();
                numPasos--;
                System.out.println("FRAGMENTS: " + numPasos + " ____ REALES: " + getSupportFragmentManager().getFragments().size());
                getSupportFragmentManager().beginTransaction().remove(adapter.getItem(current)).commitNow();
                System.out.println("FRAGMENTS: " + numPasos + " ____ REALES: " + getSupportFragmentManager().getFragments().size());
                adapter.remove(current);
                adapter.actualizarNom();
                mViewPager.setAdapter(adapter);
                mViewPager.setCurrentItem(current-1,true);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int numTab = tab.getPosition();
                mViewPager.setCurrentItem(numTab);
                if(numTab==numPasos && numTab>=1){
                    borrar.setVisibility(View.VISIBLE);
                }
                else{
                    borrar.setVisibility(View.GONE);
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
    private void setupViewPager(ViewPager viewPager, ArrayList<PasosDetalle> lp, Recipe rec) {
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
        adapter.compro();
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<View> mListView = new ArrayList<>();
        int baseId=0;


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            System.out.println("BORRRAAAAR");
            for(int i=0;i<mFragmentTitleList.size();i++){
                System.out.println(mFragmentTitleList.get(i));
            }
            if (mFragmentList.contains((Fragment) object)) {
                return mFragmentList.indexOf((Fragment) object);
            } else {
                System.out.println("JODER DEBERIA DE IR OSTIA PUTA");
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
        public void compro(){
            for(int i=0;i<mFragmentList.size();i++){
                System.out.println("ESTA MIERDA FUNCA " + mFragmentList.get(i));
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
        private static final String ARG_SECTION_NUMBER = "section_number";
        PasosDetalle pd;
        int yo;
        public PlaceholderFragment() {
        }
        public void setArguments(PasosDetalle pd, int i) {
            this.pd = pd;
            yo=i;
            System.out.println("SOY EL i=" + i + " y deberia mostrar" + pd.tiempo);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
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
            EditText tiempo = (EditText) rootView.findViewById(R.id.tiempoReceta);
            EditText desc = (EditText) rootView.findViewById(R.id.descripcionPasos);
            //desc.setText("COJON DE PUTAS " + pd.tiempo);
            return rootView;
        }
    }
    public static class DescripcionReceta extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Recipe receta;
        public DescripcionReceta() {

        }
        public void setArguments(Recipe a) {
            receta=a;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DescripcionReceta newInstance(int sectionNumber) {
            DescripcionReceta fragment = new DescripcionReceta();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_descripcion_receta, container, false);
            AutoCompleteTextView NombreReceta = (AutoCompleteTextView) rootView.findViewById(R.id.titulo_receta);
            MultiAutoCompleteTextView Descripcion = (MultiAutoCompleteTextView) rootView.findViewById(R.id.descripcion);
            EditText nComensales = (EditText) rootView.findViewById(R.id.comensales);
            EditText tiempo = (EditText) rootView.findViewById(R.id.tiempo);
            NombreReceta.setText(receta.getName());
            Descripcion.setText("");
            if(receta.getPerson()!=0) {
                nComensales.setText(Objects.toString(receta.getPerson()));
            }
            if(receta.getTotal_time()!=0) {
                tiempo.setText(Objects.toString(receta.getTotal_time()));
            }
            return rootView;
        }
    }
}
