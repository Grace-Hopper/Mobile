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

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPagerAdapter m = (ViewPagerAdapter)mViewPager.getAdapter();
                PlaceholderFragment b = new PlaceholderFragment();
                b.setArguments(new PasosDetalle("TU PUTA MADRE", 0));
                m.addFrag(b, "PASO " + (0));
                mViewPager.setAdapter(m);
                tabLayout.setupWithViewPager(mViewPager);
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
            b.setArguments(lp.get(i));
            adapter.addFrag(b, "PASO " + (i+1));
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
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
        public PlaceholderFragment() {
        }
        public void setArguments(PasosDetalle pd) {
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
