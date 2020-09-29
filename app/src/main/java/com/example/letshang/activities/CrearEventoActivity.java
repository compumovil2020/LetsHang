package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.letshang.R;

public class CrearEventoActivity extends AppCompatActivity {

    private EditText etNombre, etLugar, etPrecio, etInicio, etFin;
    private CheckBox cbDeportivo, cbEmpresarial, cbFerias, cbExhibiciones, cbCongresos, cbSocial, cbCatering, cbConciertos, cbEspectaculos, cbConvenciones;
    private Button btnCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        etNombre = findViewById(R.id.etNombreCrearEvento);
        etLugar = findViewById(R.id.etLugarCrearEvento);
        etPrecio = findViewById(R.id.etPrecioCrearEvento);
        etInicio = findViewById(R.id.etFechaFinCrearEvento);
        etFin = findViewById(R.id.etFechaFinCrearEvento);

        cbDeportivo = findViewById(R.id.cbDeportivoCrearEvento);
        cbEmpresarial = findViewById(R.id.cbEmpresarialCrearEvento);
        cbFerias = findViewById(R.id.cbFeriaCrearEvento);
        cbExhibiciones = findViewById(R.id.cbExhibicionesCrearEvento);
        cbCongresos = findViewById(R.id.cbCongresosCrearEvento);
        cbSocial = findViewById(R.id.cbSocialCrearEvento);
        cbCatering = findViewById(R.id.cbCateringCrearEvento);
        cbConciertos = findViewById(R.id.cbConciertosCrearEvento);
        cbEspectaculos = findViewById(R.id.cbEspectaculosCrearEvento);
        cbConvenciones = findViewById(R.id.cbConvencionesCrearEvento);

        btnCrear = findViewById(R.id.btnCrearEvento);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}