package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.example.androidappgestionbasura.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Clase contenedora de los fragmentes del botton navigation bar
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        setUp();
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
                R.id.navigation_home, R.id.navigation_notificaciones, R.id.navigation_perfil)
                .build();

        NavHostFragment fragmentNavHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment) ;
        NavController navController = fragmentNavHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        // TODO VER COMO HACER QUE SE PUEDA PONER EN TODA LA APP
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }




}