package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.letshang.R;

public class FiltersActivity extends AppCompatActivity {

    private SeekBar sbDistancia;
    private EditText etInicio, etFin;
    private CheckBox cbDeportivo, cbEmpresarial, cbFerias, cbExhibiciones, cbCongresos, cbSocial, cbCatering, cbConciertos, cbEspectaculos, cbConvenciones;
    private Button btnAplicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        sbDistancia = findViewById(R.id.sbDistanciaFilters);

        etInicio = findViewById(R.id.etFechaInicioFilters);
        etFin = findViewById(R.id.etFechaFinFilters);

        cbDeportivo = findViewById(R.id.cbDeportivoFilters);
        cbEmpresarial = findViewById(R.id.cbEmpresarialFilters);
        cbFerias = findViewById(R.id.cbFeriaFilters);
        cbExhibiciones = findViewById(R.id.cbExhibicionesFilters);
        cbCongresos = findViewById(R.id.cbCongresosFilters);
        cbSocial = findViewById(R.id.cbSocialFilters);
        cbCatering = findViewById(R.id.cbCateringFilters);
        cbConciertos = findViewById(R.id.cbConciertosFilters);
        cbEspectaculos = findViewById(R.id.cbEspectaculosFilters);
        cbConvenciones = findViewById(R.id.cbConvencionesFilters);

        btnAplicar = findViewById(R.id.btnAplicarFilters);

        getSupportActionBar().setTitle("");

        btnAplicar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}