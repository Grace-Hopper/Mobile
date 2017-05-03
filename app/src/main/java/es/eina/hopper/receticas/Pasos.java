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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;

public class Pasos extends AppCompatActivity {

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
    public class PasosDetalle{
        public String contenido;
        public int tiempo;
        public PasosDetalle(String c,int t){
            tiempo=t;
            contenido=c;
        }
    }
    ArrayList<PasosDetalle> lp;
    User user;
    Recipe rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        yo=this;
        Bundle b = getIntent().getExtras();
        user = new User("","");
        if(b != null)
            user = (User)b.getSerializable("user");
            rec = (Recipe)b.getSerializable("receta");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);
        lp=new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(rec.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<15;i++){
            lp.add(new PasosDetalle("CONTENIDO DEL PASO " + i, i*10));
        }
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager,lp);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
    private void setupViewPager(ViewPager viewPager, ArrayList<PasosDetalle> lp) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<lp.size();i++) {
            PlaceholderFragment a = new PlaceholderFragment();
            a.setArguments(lp.get(i));
            adapter.addFrag(a, "PASO " + (i+1));
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
        String contenido;
        int tiempo;
        boolean crono=false;
        CountDownTimer contador;
        long seg=0;
        TextView chrono;
        public PlaceholderFragment() {
            contenido="VACIO ESTE PASO LOCO";
            tiempo=10;
        }
        public void setArguments(PasosDetalle pd) {
            contenido=pd.contenido;
            tiempo=pd.tiempo;
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
            View rootView = inflater.inflate(R.layout.fragment_pasos, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            chrono = (TextView) rootView.findViewById(R.id.tempo);
            final Button comen = (Button) rootView.findViewById(R.id.iniciar);
            final Button reini = (Button) rootView.findViewById(R.id.restablecer);
            final Button pararAlarma = (Button) rootView.findViewById(R.id.pararAlarma);
            if(tiempo>0) {
                //chrono.setCountDown(true);
                seg = tiempo;
                long resto = seg % 60;
                if (resto > 9)
                    chrono.setText(seg / 60 + ":" + seg % 60);
                else
                    chrono.setText(seg / 60 + ":0" + resto);
                contador = new CountDownTimer(tiempo * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        seg = millisUntilFinished / 1000;
                        long resto = seg % 60;
                        if (resto > 9)
                            chrono.setText(seg / 60 + ":" + seg % 60);
                        else
                            chrono.setText(seg / 60 + ":0" + resto);
                    }

                    public void onFinish() {
                        //mTextField.setText("done!");
                        chrono.setText("00:00");
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        final Ringtone r = RingtoneManager.getRingtone(getContext().getApplicationContext(), notification);
                        r.play();
                        pararAlarma.setVisibility(View.VISIBLE);
                        reini.setVisibility(View.GONE);
                        comen.setVisibility(View.GONE);
                        pararAlarma.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pararAlarma.setVisibility(View.GONE);
                                reini.setVisibility(View.VISIBLE);
                                comen.setVisibility(View.VISIBLE);
                                r.stop();
                            }
                        });
                        new CountDownTimer(5 * 1000, 500) {
                            boolean a=true;
                            public void onTick(long millisUntilFinished) {
                                if(a)
                                    chrono.setVisibility(View.INVISIBLE);
                                else
                                    chrono.setVisibility(View.VISIBLE);
                                a=!a;
                            }

                            public void onFinish() {
                                //mTextField.setText("done!");
                                chrono.setVisibility(View.VISIBLE);
                            }
                        }.start();
                        crono = !crono;
                        comen.setText("INICIAR");
                    }
                };
                comen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!crono) {
                            contador.start();
                            crono = !crono;
                            comen.setText("PAUSA");
                        } else {
                            contador.cancel();
                            contador = new CountDownTimer(seg * 1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    seg = millisUntilFinished / 1000;
                                    long resto = seg % 60;
                                    if (resto > 9)
                                        chrono.setText(seg / 60 + ":" + seg % 60);
                                    else
                                        chrono.setText(seg / 60 + ":0" + resto);
                                }

                                public void onFinish() {
                                    //mTextField.setText("done!");
                                    chrono.setText("00:00");
                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                                    final Ringtone r = RingtoneManager.getRingtone(getContext().getApplicationContext(), notification);
                                    r.play();
                                    pararAlarma.setVisibility(View.VISIBLE);
                                    reini.setVisibility(View.GONE);
                                    comen.setVisibility(View.GONE);
                                    pararAlarma.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            pararAlarma.setVisibility(View.GONE);
                                            reini.setVisibility(View.VISIBLE);
                                            comen.setVisibility(View.VISIBLE);
                                            r.stop();
                                        }
                                    });
                                    new CountDownTimer(5 * 1000, 500) {
                                        boolean a=true;
                                        public void onTick(long millisUntilFinished) {
                                            if(a)
                                                chrono.setVisibility(View.INVISIBLE);
                                            else
                                                chrono.setVisibility(View.VISIBLE);
                                            a=!a;
                                        }

                                        public void onFinish() {
                                            //mTextField.setText("done!");
                                            chrono.setVisibility(View.VISIBLE);
                                        }
                                    };
                                }
                            }.start();

                            crono = !crono;
                            comen.setText("INICIAR");
                        }
                    }
                });
                reini.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chrono.setText(tiempo / 60 + ":" + tiempo % 60);
                        contador.cancel();
                        contador = new CountDownTimer(tiempo * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                seg = millisUntilFinished / 1000;
                                long resto = seg % 60;
                                if (resto > 9)
                                    chrono.setText(seg / 60 + ":" + seg % 60);
                                else
                                    chrono.setText(seg / 60 + ":0" + resto);
                            }

                            public void onFinish() {
                                //mTextField.setText("done!");
                                chrono.setText("00:00");
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                                final Ringtone r = RingtoneManager.getRingtone(getContext().getApplicationContext(), notification);
                                r.play();
                                pararAlarma.setVisibility(View.VISIBLE);
                                reini.setVisibility(View.GONE);
                                comen.setVisibility(View.GONE);
                                pararAlarma.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        pararAlarma.setVisibility(View.GONE);
                                        reini.setVisibility(View.VISIBLE);
                                        comen.setVisibility(View.VISIBLE);
                                        r.stop();
                                    }
                                });
                                new CountDownTimer(5 * 1000, 500) {
                                    boolean a=true;
                                    public void onTick(long millisUntilFinished) {
                                        if(a)
                                            chrono.setVisibility(View.INVISIBLE);
                                        else
                                            chrono.setVisibility(View.VISIBLE);
                                        a=!a;
                                    }

                                    public void onFinish() {
                                        //mTextField.setText("done!");
                                        chrono.setVisibility(View.VISIBLE);
                                    }
                                }.start();
                            }
                        };
                        crono = false;
                        comen.setText("INICIAR");
                    }
                });
            }
            else{
                comen.setVisibility(View.INVISIBLE);
                reini.setVisibility(View.INVISIBLE);
                chrono.setVisibility(View.INVISIBLE);
            }
            textView.setText(contenido);
            return rootView;
        }
    }

}
