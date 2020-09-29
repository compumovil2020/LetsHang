package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.google.android.material.navigation.NavigationView;

public class PrincipalActivity extends AppCompatActivity {

    private ImageView ivFiltrar;
    private ConstraintLayout eventLayout;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // set action bar title
        getSupportActionBar().setTitle("Feed");

        //inflate elements
        ivFiltrar = findViewById(R.id.ivFiltrarPrincipal);
        eventLayout = findViewById(R.id.eventLayout);
        drawerLayout = findViewById(R.id.principal_drawer_layout);
        navView = findViewById(R.id.principal_nav_view);
        btnMap = findViewById(R.id.btnMapaPrincipal);

        //setup side menu
        setupMenu();

        //setup listeners
        ivFiltrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , FiltersActivity.class);
                startActivity(intent);
            }
        });

        eventLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , InscripcionEventoActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(intent);
            }
        });


    }

    void setupMenu(){

        menuToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.menu_open_reader,
                R.string.menu_close_reader);

        drawerLayout.setDrawerListener(menuToggle);
        menuToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() ==  R.id.item_menu_administrar_perfil){
                    Intent intent = new Intent(getApplicationContext() , InformacionPerfilActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_sugerir){
                    Intent intent = new Intent(getApplicationContext() , CrearSugerenciaActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    //does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_logout){
                    Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_pago){
                    Toast.makeText(getApplicationContext(), item.getTitle() ,Toast.LENGTH_SHORT ).show();

                }
                return true;
            }
        });
    }


    // needed to setup menu toggle button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (menuToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}