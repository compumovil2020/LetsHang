package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.google.android.material.navigation.NavigationView;

public class InformacionPerfilActivity extends AppCompatActivity {

    private TextView deportesTag, conciertosTag, conferenciasTag;
    private Button eventBtn, editPerfil;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_perfil);

        //Inflates
        deportesTag = findViewById(R.id.deportesInfoPerfil);
        conciertosTag = findViewById(R.id.conciertosInfoPerfil);
        conferenciasTag = findViewById(R.id.conferenciasInfoPerfil);
        eventBtn = findViewById(R.id.eventOneInfoPerfil);
        editPerfil = findViewById(R.id.editarInfoPerfil);
        drawerLayout = findViewById(R.id.informacion_perfil_drawer_layout);
        navView = findViewById(R.id.informacion_perfil_nav_view);

        //Set underline
        deportesTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        conciertosTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        conferenciasTag.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        setupMenu();


        //Listeners
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModificarEvento.class);
                startActivity(intent);
            }
        });

        editPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdministrarPerfilActivity.class);
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
                    //does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_sugerir){
                    Intent intent = new Intent(getApplicationContext() , CrearSugerencia.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritos.class);
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