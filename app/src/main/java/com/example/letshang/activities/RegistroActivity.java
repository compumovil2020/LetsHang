package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.letshang.R;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etTelefono, etPassword, etVerify, etNuevo;
    private ToggleButton tbDeportes, tbJuegosMesa, tbConciertos, tbFiesta, tbCharlar, tbConferencias;
    private Button btnSiguiente, btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etNombreRegistro);
        etCorreo = findViewById(R.id.etCorreoRegistro);
        etTelefono = findViewById(R.id.etTelefonoRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        etVerify = findViewById(R.id.etVerifyRegistro);
        etNuevo = findViewById(R.id.etNuevoRegistro);

        tbDeportes = findViewById(R.id.tbDeportesRegistro);
        tbJuegosMesa = findViewById(R.id.tbJuegosMesaRegistro);
        tbConciertos = findViewById(R.id.tbConciertosRegistro);
        tbFiesta = findViewById(R.id.tbFiestasRegistro);
        tbCharlar = findViewById(R.id.tbCharlarRegistro);
        tbConferencias = findViewById(R.id.tbConferenciasRegistro);

        btnSiguiente = findViewById(R.id.btnSiguienteRegistro);
        btnAgregar = findViewById(R.id.btnAgregarRegistro);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistroAdicionalActivity.class);
                startActivity(intent);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}