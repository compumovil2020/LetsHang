package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.letshang.R;

public class ModificarEventoActivity extends AppCompatActivity {

    private EditText etNombre, etLugar, etPrecio, etInicio, etFin;
    private CheckBox cbDeportivo, cbEmpresarial, cbFerias, cbExhibiciones, cbCongresos, cbSocial, cbCatering, cbConciertos, cbEspectaculos, cbConvenciones;
    private Button btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_evento);

        etNombre = findViewById(R.id.etNombreModificarEvento);
        etLugar = findViewById(R.id.etLugarModificarEvento);
        etPrecio = findViewById(R.id.etPrecioModificarEvento);
        etInicio = findViewById(R.id.etFechaInicioModificarEvento);
        etFin = findViewById(R.id.etFechaFinModificarEvento);

        cbDeportivo = findViewById(R.id.cbDeportivoModificarEvento);
        cbEmpresarial = findViewById(R.id.cbEmpresarialModificarEvento);
        cbFerias = findViewById(R.id.cbFeriaModificarEvento);
        cbExhibiciones = findViewById(R.id.cbExhibicionesModificarEvento);
        cbCongresos = findViewById(R.id.cbCongresosModificarEvento);
        cbSocial = findViewById(R.id.cbSocialModificarEvento);
        cbCatering = findViewById(R.id.cbCateringModificarEvento);
        cbConciertos = findViewById(R.id.cbConciertosModificarEvento);
        cbEspectaculos = findViewById(R.id.cbEspectaculosModificarEvento);
        cbConvenciones = findViewById(R.id.cbConvencionesModificarEvento);

        btnModificar = findViewById(R.id.btnModificarEvento);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}