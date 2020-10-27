package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.letshang.R;

public class FiltersActivity extends AppCompatActivity {

    private SeekBar sbDistancia;
    private EditText etInicio, etFin;
    private CheckBox cbDeportivo, cbEmpresarial, cbFerias, cbExhibiciones, cbCongresos, cbSocial, cbCatering, cbConciertos, cbEspectaculos, cbConvenciones;
    private Button btnAplicar;
    private TextView tvSeekValue;

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
        tvSeekValue = findViewById(R.id.tvSeekValue);

        btnAplicar = findViewById(R.id.btnAplicarFilters);

        getSupportActionBar().setTitle("");

        btnAplicar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sbDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                tvSeekValue.setText("" + progress*2+" Km");
                tvSeekValue.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                tvSeekValue.setY(sbDistancia.getY()+50); //just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }
}