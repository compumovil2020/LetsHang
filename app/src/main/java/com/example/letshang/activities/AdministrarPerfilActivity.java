package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letshang.R;
import com.google.android.material.navigation.NavigationView;

public class AdministrarPerfilActivity extends AppCompatActivity {

    EditText etNombre, etEmail, etTelefono, etLocation, etFecha;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_perfil);

        etNombre = findViewById(R.id.etNombreAdministrarPerfil);
        etEmail = findViewById(R.id.etEmailAdministrarPerfil);
        etTelefono = findViewById(R.id.etTelefonoAdministrarPerfil);
        etLocation = findViewById(R.id.etLocationAdministrarPerfil);
        etFecha = findViewById(R.id.etCalendarAdministrarPerfil);

        btnGuardar = findViewById(R.id.btnGuardarAdministrarPerfil);

        getSupportActionBar().setTitle("Mi perfil");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


}