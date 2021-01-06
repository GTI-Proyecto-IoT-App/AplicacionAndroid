package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.servicios.ServicioNotificacionesMqtt;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Clase contenedora de los fragmentes del botton navigation bar
 */
public class HomeActivity extends AppCompatActivity {

    public static final String INTENT_KEY_ABRIR_NOTIFICACIONES = "abrir-notificaciones";
    private static final int CODIGO_PERMISO_SERVICIO_PRIMER_PLANO = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setUp();
        arrancarSerivicio();

    }

    private void arrancarSerivicio() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED){

            if(!Utility.isMyServiceRunning(ServicioNotificacionesMqtt.class,this)){

                startService(new Intent(this, ServicioNotificacionesMqtt.class));
            }
        }else{
            Utility.permissionAsk(Manifest.permission.FOREGROUND_SERVICE,this,CODIGO_PERMISO_SERVICIO_PRIMER_PLANO);
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // reenviar los activity result a los fragments hijo
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment !=null) {

            List<Fragment> childFragments = navHostFragment.getChildFragmentManager().getFragments();
            for (Fragment fragment: childFragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void setUp() {

        // get extras para ver si se abrio desde notificacion
        Bundle extras = getIntent().getExtras();
        boolean abrirNotificaciones = false;
        if(extras!=null){
            abrirNotificaciones = extras.getBoolean(INTENT_KEY_ABRIR_NOTIFICACIONES);
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //Necesario para que cuando vuelvas a clicar en un item del Navigation no se llame al onDestroy
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_mapa_basuras_municipales,
                R.id.navigation_huella_c02,
                R.id.navigation_notificaciones,
                R.id.navigation_perfil)
                .build();

        NavHostFragment fragmentNavHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment) ;
        NavController navController = fragmentNavHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(abrirNotificaciones){
            navController.navigate(R.id.navigation_notificaciones);
        }

        // TODO VER COMO HACER QUE SE PUEDA PONER EN TODA LA APP
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CODIGO_PERMISO_SERVICIO_PRIMER_PLANO){
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                arrancarSerivicio();

            } else {
                Toast.makeText(this, "Sin el permiso, no se podrá recibir notificaciones", Toast.LENGTH_SHORT).show();
            }

        }
        // reenviar los activity result a los fragments hijo
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment !=null) {

            List<Fragment> childFragments = navHostFragment.getChildFragmentManager().getFragments();
            for (Fragment fragment: childFragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}