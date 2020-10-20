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
import com.example.letshang.providers.UserProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class InformacionPerfilActivity extends AppCompatActivity {

    private TextView tvDeportes, tvConciertos, tvConferencias;
    private Button btnEvento, btnEditar;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private TextView tvNombre, tvCorreo;

    private UserProvider usProv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_perfil);
        mAuth = FirebaseAuth.getInstance();

        tvDeportes = findViewById(R.id.tvDeportesInformacionPerfil);
        tvConciertos = findViewById(R.id.tvDeportesInformacionPerfil);
        tvConferencias = findViewById(R.id.tvConferenciasInformacionPerfil);

        btnEvento = findViewById(R.id.btnEvento1InformacionPerfil);
        btnEditar = findViewById(R.id.btnEditarInformacionPerfil);

        drawerLayout = findViewById(R.id.informacion_perfil_drawer_layout);
        navView = findViewById(R.id.informacion_perfil_nav_view);

        tvDeportes.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvConciertos.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvConferencias.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvNombre = findViewById(R.id.tvNombreInformacionPerfil);
        tvCorreo = findViewById(R.id.tvCorreoInformacionPerfil);

        usProv = new UserProvider();

        tvNombre.setText(usProv.getCurrentUser().getName());
        tvCorreo.setText(usProv.getCurrentUser().getEmail());

        setupMenu();

        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModificarEventoActivity.class);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(getApplicationContext() , CrearSugerenciaActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    Intent intent = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_logout){

                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext() , StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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