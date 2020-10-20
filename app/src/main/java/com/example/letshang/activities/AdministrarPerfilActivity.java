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
import com.example.letshang.model.Participant;
import com.example.letshang.model.User;
import com.example.letshang.providers.UserProvider;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.util.Log;

public class AdministrarPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etTelefono, etLocation, etFecha;
    private Button btnGuardar;

    private UserProvider usrProvider;
    private Participant part;

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

        usrProvider = new UserProvider();
        part = (Participant)usrProvider.getCurrentUser();
        etNombre.setText(part.getName());
        etEmail.setText(part.getEmail());
        etTelefono.setText(part.getPhone());
        //etLocation.setText(usrProvider.getCurrentUser().getLocation());
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String today = formatter.format(part.getBirthDate());
        etFecha.setText(today);



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


}