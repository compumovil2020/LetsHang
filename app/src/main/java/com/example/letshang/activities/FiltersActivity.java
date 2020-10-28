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
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.ui.dialog.DatePickerFragment;

import java.util.Date;
import java.util.GregorianCalendar;

public class FiltersActivity extends AppCompatActivity {

    private SeekBar sbDistancia;
    private EditText etInicio, etFin;
    private CheckBox cbDeportivo, cbJuegos, cbAcademico, cbMusical, cbSocial;
    private Button btnAplicar;
    private TextView tvSeekValue;
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

        tvSeekValue = findViewById(R.id.tvSeekValue);


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