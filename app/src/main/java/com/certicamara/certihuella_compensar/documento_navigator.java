package com.certicamara.certihuella_compensar;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.List;

        import com.certicamara.certihuella_compensar.R;
        import com.certicamara.certihuella_compensar.access.Imagen;
        import com.certicamara.certihuella_compensar.access.sincronizar;
        import com.certicamara.certihuella_compensar.access.sqliteHelper;

public class documento_navigator extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, documento_main.OnFragmentInteractionListener,
        documento_lista.OnFragmentInteractionListener,documento_cargar.OnFragmentInteractionListener{


    sqliteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento_vavigator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = documento_main.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnFinalizar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                String resultado=sincronizar.CumpleCondicionesSolicitud(context,1);
                if(resultado.equals("OK")) {
                    //if (sincronizar.TransferirDocumento(1, "1")) {
                        Snackbar.make(view, "Se ha marcado la solicitud para transferir los documentos a OnBase", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    //}

                    final ProgressDialog ringProgressDialog = ProgressDialog.show(documento_navigator.this, "Por favor espere ...", "Guardando Información ...", true);
                    ringProgressDialog.setCancelable(false);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            sincronizar.TransferirDocumento(1, "1");
                            ringProgressDialog.dismiss();
                        }
                    }).start();



                }
                else
                {

                    Toast toast = Toast.makeText(context, resultado, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    @Override
    protected void onResume() {
        super.onResume();

        Fragment frActual=getVisibleFragment();
        bd= new sqliteHelper(this);
        if(frActual instanceof  documento_main || frActual == null) {

           // EventosMainFragment();
        }

    }







    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = documento_navigator.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
       /* int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }*/

        Fragment frActual=getVisibleFragment();
        if(frActual instanceof  documento_main)
        {
            super.onBackPressed();
        }
        else
        {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = documento_main.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.documentos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_camera) {
            fragmentClass = documento_main.class;
        } else if (id == R.id.nav_gallery) {
            fragmentClass = documento_lista.class;
        } else if (id == R.id.nav_slideshow) {
            fragmentClass = documento_main.class;
        } else if (id == R.id.nav_manage) {
            fragmentClass = documento_main.class;
        } else if (id == R.id.nav_share) {
            fragmentClass = documento_main.class;
        } else if (id == R.id.nav_send) {
            fragmentClass = documento_main.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}