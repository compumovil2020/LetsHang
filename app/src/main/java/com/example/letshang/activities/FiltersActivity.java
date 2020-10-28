package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.letshang.R;
import com.example.letshang.ui.dialog.DatePickerFragment;

import java.util.Date;
import java.util.GregorianCalendar;

public class FiltersActivity extends AppCompatActivity {

    private SeekBar sbDistancia;
    private EditText etInicio, etFin;
    private CheckBox cbDeportivo, cbJuegos, cbAcademico, cbMusical, cbSocial;
    private Button btnAplicar;
    private GregorianCalendar startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        sbDistancia = findViewById(R.id.sbDistanciaFilters);

        etInicio = findViewById(R.id.etFechaInicioFilters);
        etFin = findViewById(R.id.etFechaFinFilters);

        cbDeportivo = findViewById(R.id.cbDeportivoFilters);
        cbJuegos = findViewById(R.id.cbJuegoFilters);
        cbAcademico = findViewById(R.id.cbAcademicoFilters);
        cbSocial = findViewById(R.id.cbSocialFilters);
        cbMusical = findViewById(R.id.cbMusicalFilters);

        btnAplicar = findViewById(R.id.btnAplicarFilters);

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                intent.putExtra("cbDeportivo", cbDeportivo.isChecked());
                intent.putExtra("cbJuego", cbJuegos.isChecked());
                intent.putExtra("cbAcademico", cbAcademico.isChecked());
                intent.putExtra("cbSocial", cbSocial.isChecked());
                intent.putExtra("cbMusical", cbMusical.isChecked());
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("");

        btnAplicar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(true);
            }
        });

        etFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(false);
            }
        });

    }

    /**
     * calls a fragment to choose date and time
     */
    private void showDatePickerDialog(final boolean isStart) {
        DatePickerFragment dateFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                String selectedDate = day + "/" + (month+1) + "/" + year + " ";


                if(isStart) {
                    startDate = new GregorianCalendar(year, month, day);
                    etInicio.setText(selectedDate);
                }
                else{
                    endDate = new GregorianCalendar(year, month, day);
                    etFin.setText(selectedDate);
                }


            }
        });

        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }
}